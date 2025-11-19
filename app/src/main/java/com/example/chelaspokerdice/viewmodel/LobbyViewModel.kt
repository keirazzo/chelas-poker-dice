package com.example.chelaspokerdice.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val lobbiesRepository: LobbiesRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
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
        }
    }

    fun leaveLobby(){
        viewModelScope.launch {
            _lobby.value?.let { currentLobby ->
                lobbiesRepository.leaveLobby(currentLobby, userRepository.getPlayer())
            }
        }
    }
}