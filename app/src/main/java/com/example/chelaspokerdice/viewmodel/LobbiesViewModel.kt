package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbiesViewModel @Inject constructor(
    private val lobbiesRepository: LobbiesRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _lobbies = MutableStateFlow<List<Lobby>>(emptyList())
    val lobbies: StateFlow<List<Lobby>> = _lobbies

    fun loadLobbies(){
        viewModelScope.launch {
            _lobbies.value = lobbiesRepository.getLobbies().filter { !it.isFull() }
        }
    }

    fun joinLobby(lobby: Lobby){
        viewModelScope.launch {
            lobbiesRepository.joinLobby(lobby, userRepository.getPlayer())
            loadLobbies()
        }
    }

    fun createLobby(name: String, description: String, numberOfPlayers: Int, numberOfRounds: Int): String{
        var newLobbyId = ""
        viewModelScope.launch {
            val lobby = Lobby(
                name,
                description,
                1,
                numberOfPlayers,
                numberOfRounds,
                listOf(userRepository.getPlayer()))
            lobbiesRepository.addLobby(lobby)
            newLobbyId = lobby.id
        }
        return newLobbyId
    }
}