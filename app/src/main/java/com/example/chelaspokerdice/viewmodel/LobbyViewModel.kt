package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.repository.GameRepository
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LobbyState {
    data object Waiting : LobbyState
    data object Full : LobbyState
}
@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val lobbiesRepository: LobbiesRepository,
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow<LobbyState>(LobbyState.Waiting)
    val state: StateFlow<LobbyState> = _state.asStateFlow()
    private val lobbyId: String = savedStateHandle.get<String>("lobbyId") ?: ""
    private val _lobby = MutableStateFlow<Lobby?>(null)
    val lobby: StateFlow<Lobby?> = _lobby
    init {
        if (lobbyId.isNotEmpty()) {
            loadLobby()
        }
    }
    fun loadLobby(){
        viewModelScope.launch {
            _lobby.value = lobbiesRepository.getLobby(lobbyId)
            if (lobby.value?.isFull() ?: false)_state.value = LobbyState.Full
        }
    }

    fun leaveLobby(){
        viewModelScope.launch {
            _lobby.value?.let { currentLobby ->
                lobbiesRepository.leaveLobby(currentLobby, userRepository.getPlayer())
            }
        }
    }

    fun createGame(name: String, players: List<Player>, numberOfRounds: Int): String{
        var newGameId = ""
        viewModelScope.launch {
            val game = Game(
                name,
                players,
                numberOfRounds,
                players.elementAt(0),
                1,
                2
            )
            newGameId = game.id
            gameRepository.addGame(game)
        }
        return newGameId
    }
}