package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbiesViewModel @Inject constructor(
    private val lobbiesRepository: LobbiesRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val lobbies: StateFlow<List<Lobby>> = lobbiesRepository.getLobbies().map {
        lobbiesList -> lobbiesList.filter { !it.isFull() && !it.gameStarted}
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun joinLobby(lobby: Lobby){
        viewModelScope.launch {
            val currentUser = userRepository.getPlayer()
            if (currentUser.id.isNotEmpty()) {
                lobbiesRepository.joinLobby(lobby, currentUser)
            }
        }
    }

    suspend fun createLobby(name: String, description: String, numberOfPlayers: Int, numberOfRounds: Int): String{
            val lobby = Lobby(
                name,
                description,
                1,
                numberOfPlayers,
                numberOfRounds,
                listOf(userRepository.getPlayer()))
            lobbiesRepository.addLobby(lobby)
        return lobby.id
    }
}