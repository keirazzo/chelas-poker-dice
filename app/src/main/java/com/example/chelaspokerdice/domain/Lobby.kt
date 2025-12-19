package com.example.chelaspokerdice.domain

import java.util.UUID

data class Lobby(
    val name: String = "",
    val description: String = "",
    val numberOfPlayers: Int = 0,
    val maxNumberOfPlayers: Int = 2,
    val numberOfRounds: Int = 1,
    val players: List<Player> = listOf(),
    val id: String = UUID.randomUUID().toString(),
    val gameStarted: Boolean = false
){
    fun isEmpty(): Boolean = numberOfPlayers == 0;
    fun isFull(): Boolean = numberOfPlayers == maxNumberOfPlayers;

    companion object {
        fun isNameValid(name: String): Boolean = name.isNotBlank();
        fun isDescriptionValid(description: String): Boolean = description.isNotBlank();
        fun isNumberOfRoundsValid(numberOfRounds: Int): Boolean = numberOfRounds > 0 && numberOfRounds < 61;
        fun isMaxNumberOfPlayersValid(maxNumberOfPlayers: Int): Boolean = maxNumberOfPlayers > 1 && maxNumberOfPlayers < 7;
    }
}
