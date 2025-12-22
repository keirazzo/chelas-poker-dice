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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val _user = MutableStateFlow<Player?>(null)
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = userRepository.getPlayer()
        }
    }
    private val _state = MutableStateFlow<LobbyState>(LobbyState.Waiting)
    val state: StateFlow<LobbyState> = _state.asStateFlow()
    private val lobbyId: String = savedStateHandle.get<String>("lobbyId") ?: ""
    val lobby: StateFlow<Lobby?> = lobbiesRepository.getLobbyStream(lobbyId)
        .onEach { lobby ->
            if (lobby?.isFull() == true) _state.value = LobbyState.Full
            else if (lobby != null) _state.value = LobbyState.Waiting
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun leaveLobby(){
        viewModelScope.launch {
            lobby.value?.let { currentLobby -> lobbiesRepository.leaveLobby(currentLobby, userRepository.getPlayer()) }
        }
    }

    suspend fun createGame(name: String, players: List<Player>, numberOfRounds: Int): String{
        val currentLobby = lobby.value!!
        val game = Game(name, players, numberOfRounds, players.elementAt(0))
        gameRepository.saveGame(game)
        lobbiesRepository.startGame(currentLobby.id)
        return game.id
    }
}