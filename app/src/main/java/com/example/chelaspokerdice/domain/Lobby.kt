package com.example.chelaspokerdice.domain

data class Lobby(
    val name: String,
    val description: String,
    val numberOfPlayers: Int,
    val maxNumberOfPlayers: Int,
    val numberOfRounds: Int,
    val players: List<Player>
){
    fun isEmpty(): Boolean = numberOfPlayers == 0;
    fun isFull(): Boolean = numberOfPlayers == maxNumberOfPlayers;

    companion object {
        fun isNameValid(name: String): Boolean = name.isNotBlank();
        fun isDescriptionValid(description: String): Boolean = description.isNotBlank();
        fun isNumberOfRoundsValid(numberOfRounds: Int): Boolean = numberOfRounds > 0 && numberOfRounds < 60;
        fun isMaxNumberOfPlayersValid(maxNumberOfPlayers: Int): Boolean = maxNumberOfPlayers > 1 && maxNumberOfPlayers < 6;
    }
}
