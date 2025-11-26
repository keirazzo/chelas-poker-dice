package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _games = MutableStateFlow<Game?>(null)
    val games: StateFlow<Game?> = _games
    private val gameId: String = savedStateHandle.get<String>("gameId") ?: ""

    fun loadGame(): Game {
        var game = Game()
        viewModelScope.launch{
            game = gameRepository.getGame(gameId)!!
        }
        return game
    }
}