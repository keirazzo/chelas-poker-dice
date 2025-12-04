package com.example.chelaspokerdice.domain

enum class Values(val number: Int, val symbol: String){
    ACE(1, "ace"),
    KING(13, "king"),
    QUEEN(12, "queen"),
    JACK(11, "jack"),
    TEN(10, "ten"),
    NINE(9, "nine")

}
data class Dice(
    val number: Int,
    val symbol: String,
    val id: Int
) {

    fun roll(): Dice{
        val newValue = Values.entries.toTypedArray().random()
        return this.copy(
            number = newValue.number,
            symbol = newValue.symbol
        )
    }
}