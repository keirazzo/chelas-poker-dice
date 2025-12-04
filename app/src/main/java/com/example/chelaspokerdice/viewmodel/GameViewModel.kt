package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Dice
import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TurnState {
    data object FirstRoll : TurnState
    data object Rerolls : TurnState
    data object NoRerolls : TurnState
}
@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
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
    fun rollDice(){
        val currentGame = game.value ?: return

        viewModelScope.launch {
            val rolled = currentGame.rollDice()
            val updatedGame = rolled.copy(rerolls = 2)

            gameRepository.saveGame(updatedGame)
            _turnState.value = TurnState.Rerolls
        }
    }

    fun rerollDice(){
        val currentGame = game.value ?: return
        viewModelScope.launch {
            if (currentGame.rerolls > 0) {
                val rolled = currentGame.rollDice()
                var updatedGame = rolled.copy(rerolls = currentGame.rerolls.dec())
                if (updatedGame.rerolls == 0) {
                    updatedGame = updatedGame.confirmDice()
                    _turnState.value = TurnState.NoRerolls
                }
                gameRepository.saveGame(updatedGame)
            }
            else _turnState.value = TurnState.NoRerolls
        }
    }

    fun toggleDice(dice: Dice) {
        val currentGame = game.value ?: return
        viewModelScope.launch {
            if (_turnState.value == TurnState.Rerolls) {
                val updatedGame = currentGame.toggleDice(dice)
                gameRepository.saveGame(updatedGame)
            }
        }
    }

    fun confirmHand(){
        val currentGame = game.value ?: return
        viewModelScope.launch {
            val updatedGame = currentGame.finishTurn()
            gameRepository.saveGame(updatedGame)

            _turnState.value = TurnState.FirstRoll
        }
    }
}