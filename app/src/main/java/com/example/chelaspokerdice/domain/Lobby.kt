package com.example.chelaspokerdice.domain

import java.util.UUID

data class Lobby(
    val name: String,
    val description: String,
    val numberOfPlayers: Int,
    val maxNumberOfPlayers: Int,
    val numberOfRounds: Int,
    val players: List<Player>,
    val id: String = UUID.randomUUID().toString()
){
    init{
        require(isNameValid(name)) { "Name must not be empty or blank"}
        require(isDescriptionValid(description)) { "Description must not be empty or blank"}
        require(isNumberOfRoundsValid(numberOfRounds)) { "Number of rounds should be between 1 and 60"}
        require(isMaxNumberOfPlayersValid(maxNumberOfPlayers)) { "Number of players should be between 2 and 6"}
    }
    fun isEmpty(): Boolean = numberOfPlayers == 0;
    fun isFull(): Boolean = numberOfPlayers == maxNumberOfPlayers;

    companion object {
        fun isNameValid(name: String): Boolean = name.isNotBlank();
        fun isDescriptionValid(description: String): Boolean = description.isNotBlank();
        fun isNumberOfRoundsValid(numberOfRounds: Int): Boolean = numberOfRounds > 0 && numberOfRounds < 61;
        fun isMaxNumberOfPlayersValid(maxNumberOfPlayers: Int): Boolean = maxNumberOfPlayers > 1 && maxNumberOfPlayers < 7;
    }
}
