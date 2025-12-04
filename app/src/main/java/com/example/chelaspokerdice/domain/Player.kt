package com.example.chelaspokerdice.domain

data class Player (
    val name: String,
    val currentHand: List<Dice> = emptyList<Dice>(),
    val currentHandName: String = ""
){
    fun handIsEmpty(): Boolean = currentHand == emptyList<Dice>()
}