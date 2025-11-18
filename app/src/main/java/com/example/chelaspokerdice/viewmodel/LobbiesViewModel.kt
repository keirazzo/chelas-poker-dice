package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.repository.LobbiesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LobbiesViewModel(private val repository: LobbiesRepository): ViewModel() {
    private val _lobbies = MutableStateFlow<List<Lobby>>(emptyList())
    val lobbies: StateFlow<List<Lobby>> = _lobbies

    fun loadLobbies(){
        viewModelScope.launch {
            _lobbies.value = repository.getLobbies().filter { !it.isFull() }
        }
    }

    fun joinLobby(lobby: Lobby, player: Player){
        viewModelScope.launch {
            repository.joinLobby(lobby, player)
            loadLobbies()
        }
    }
}