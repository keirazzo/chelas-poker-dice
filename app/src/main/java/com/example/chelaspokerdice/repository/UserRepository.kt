package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Player

interface UserRepository {
    suspend fun getPlayer(): Player
}

class FakeUserRepository : UserRepository {
    private val user = Player("Keira")

    override suspend fun getPlayer() = user
}
