package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Dice
import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.repository.GameRepository
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface GameState {
    data object PlayingRound : GameState
    data object EndOfRound : GameState
    data object EndOfGame : GameState

}
sealed interface TurnState {
    data object FirstRoll : TurnState
    data object Rerolls : TurnState
    data object NoRerolls : TurnState
}
@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val lobbiesRepository: LobbiesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val gameId: String = savedStateHandle.get<String>("gameId") ?: ""

    val game: StateFlow<Game?> = gameRepository.getGame(gameId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _turnState = MutableStateFlow<TurnState>(TurnState.FirstRoll)
    val turnState: StateFlow<TurnState> = _turnState.asStateFlow()

    private val _currentUser = MutableStateFlow<Player?>(null)
    val currentUser = _currentUser.asStateFlow()
    init {
        viewModelScope.launch {
            _currentUser.value = userRepository.getPlayer()

            if (gameId == "SOLO"){
                val soloGame = Game("Solo", listOf(currentUser.value, Player("ai", "Joe")) as List<Player>, 5, currentUser.value!!, id = "SOLO")
                gameRepository.saveGame(soloGame)
            }
        }
    }

    fun isHost(): Boolean {
        return game.value?.players?.firstOrNull()?.id == currentUser.value?.id
    }
    fun isMyTurn(): Boolean {
        val gameData = game.value
        val userData = _currentUser.value

        val currentId = gameData?.currentPlayer?.id
        val myId = userData?.id

        return currentId == myId
    }

    fun rollDice(){
        if (!isMyTurn()) return
        val currentGame = game.value ?: return

        viewModelScope.launch {
            val updatedGame = currentGame.startTurn()
            gameRepository.saveGame(updatedGame)
        }
    }

    fun rerollDice() {
        val currentGame = game.value ?: return
        if (!isMyTurn() || currentGame.rerolls <= 0) return

        viewModelScope.launch {
            val updatedGame = currentGame.processReroll()
            gameRepository.saveGame(updatedGame)
        }
    }

    fun toggleDice(dice: Dice) {
        if (!isMyTurn()) return
        val currentGame = game.value ?: return
        viewModelScope.launch {
            val updatedGame = currentGame.toggleDice(dice)
            if (updatedGame != currentGame) gameRepository.saveGame(updatedGame)

        }
    }

    fun confirmHand(){
        if (!isMyTurn()) return
        val currentGame = game.value ?: return

        viewModelScope.launch {
            val currentHandDice = currentGame.keptDice + currentGame.rerollDice
            val handName = currentGame.getHandType(currentHandDice).type
            updatePlayerStatsAfterRound(handName)
            val nextState = currentGame.nextStep()
            gameRepository.saveGame(nextState)

            if (nextState.state == "END_OF_ROUND") {
                delay(5000)
                if (nextState.isGameFinished()) {
                    gameRepository.saveGame(nextState.copy(state = "END_OF_GAME"))
                } else {
                    gameRepository.saveGame(nextState.finishRound().copy(state = "PLAYING"))
                }
            }
        }
    }

    private suspend fun updatePlayerStatsAfterRound(handName: String) {
        val user = _currentUser.value ?: return

        val newFrequency = user.handFrequency.toMutableMap()
        newFrequency[handName] = (newFrequency[handName] ?: 0) + 1

        val updatedPlayer = user.copy(handFrequency = newFrequency)

        _currentUser.value = updatedPlayer
        userRepository.savePlayer(updatedPlayer)
    }

    private var statsUpdated = false

    fun updateFinalStats() {
        val currentGame = game.value ?: return
        val user = _currentUser.value ?: return

        if (statsUpdated) return

        viewModelScope.launch {
            val winner = currentGame.players.maxByOrNull { it.score }
            val isWinner = winner?.id == user.id

            val updatedPlayer = user.copy(
                gamesPlayed = user.gamesPlayed + 1,
                gamesWon = if (isWinner) user.gamesWon + 1 else user.gamesWon
            )

            _currentUser.value = updatedPlayer
            userRepository.savePlayer(updatedPlayer)
            statsUpdated = true
        }
    }
    fun leaveGame() {
        val currentGame = game.value ?: return
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            try {
                val player = userRepository.getPlayer()

                val lobby = lobbiesRepository.getLobby(currentGame.id)
                if (lobby != null) {
                    lobbiesRepository.leaveLobby(lobby, player)
                }

                val updatedGame = currentGame.removePlayer(user.id)
                if (updatedGame.players.isEmpty()) {
                    gameRepository.deleteGame(currentGame.id)
                } else {
                    gameRepository.saveGame(updatedGame)
                }
            } catch (e: Exception) {
                android.util.Log.e("GameViewModel", "Error leaving game", e)
            }
        }
    }

    fun playAgain() {
        val currentGame = game.value ?: return
        if (!isHost()) return

        viewModelScope.launch {
            lobbiesRepository.resetLobby(currentGame.id)

            val updatedGame = currentGame.prepareForLobby()
            gameRepository.saveGame(updatedGame)
        }
    }
}