package com.example.chelaspokerdice.domain

import java.util.UUID

data class Game (
    val name: String = "",
    val players: List<Player> = listOf<Player>(),
    val numberOfRounds: Int = 0,
    val currentPlayer: Player = Player(""),
    val currentRound: Int = 0,
    val rerolls: Int = 0,
    val id: String = UUID.randomUUID().toString()
){
}