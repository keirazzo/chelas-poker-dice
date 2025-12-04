package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface GameRepository {
    fun getGame(gameId: String): Flow<Game?>
    suspend fun saveGame(game: Game)
}

class FakeGameRepository : GameRepository {
    private val games = mutableMapOf<String, MutableStateFlow<Game?>>()

    override fun getGame(gameId: String): Flow<Game?> {
        return games.getOrPut(gameId){ MutableStateFlow(null)}
    }

    override suspend fun saveGame(game: Game) {
        val flow = games.getOrPut(game.id) {MutableStateFlow(null)}
        flow.value = game
    }



}