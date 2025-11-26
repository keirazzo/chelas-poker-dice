package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.domain.Player

interface GameRepository {
    suspend fun getGame(gameId: String): Game?
    suspend fun addGame(game: Game)
}

class FakeGameRepository : GameRepository {
    private val games = mutableListOf<Game>()

    override suspend fun getGame(gameId: String): Game? = games.find { game -> game.id == gameId }

    override suspend fun addGame(game: Game) { games.add(game) }


}