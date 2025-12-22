package com.example.chelaspokerdice.domain

data class Player (
    val name: String = "",
    val currentHand: List<Dice> = emptyList<Dice>(),
    val currentHandName: String = "",
    val score: Int = 0,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val handFrequency: Map<String, Int> = HandType.entries.associate { it.name to 0  }
){
    fun handIsEmpty(): Boolean = currentHand == emptyList<Dice>()
}