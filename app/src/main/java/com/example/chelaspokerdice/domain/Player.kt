package com.example.chelaspokerdice.domain

class Player (
    val name: String,
    val currentHand: List<Dice> = emptyList<Dice>(),
    val currentHandName: String = ""
){
    fun handIsEmpty(): Boolean = currentHand == emptyList<Dice>()
}