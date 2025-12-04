package com.example.chelaspokerdice.domain

import java.util.UUID

data class Game (
    val name: String = "",
    val players: List<Player> = listOf<Player>(),
    val numberOfRounds: Int = 0,
    val currentPlayer: Player = Player(""),
    val currentRound: Int = 1,
    val rerolls: Int = 0,
    val id: String = UUID.randomUUID().toString(),
    val keptDice: List<Dice> = listOf<Dice>(),
    val rerollDice: List<Dice> = List(5){ index -> Dice(0, "", index + 1).roll()},
){

    fun rollDice(): Game {
        val rolled = rerollDice.map { it.roll() }
        return this.copy(rerollDice = rolled)
    }

    fun toggleDice(dice: Dice): Game {
        if(keptDice.contains(dice)) return this.copy(keptDice = keptDice - dice, rerollDice = rerollDice + dice)
        if(rerollDice.contains(dice)) return this.copy(keptDice = keptDice + dice, rerollDice = rerollDice - dice)
        return this
    }

    fun confirmDice(): Game {
        val confirmedDice = this.keptDice + this.rerollDice
        return this.copy(keptDice = confirmedDice, rerollDice = listOf())

    }

    fun finishTurn(): Game {
        val allDice = keptDice + rerollDice
        val currentPlayerIndex = players.indexOf(currentPlayer)
        val updatedPlayers = players.toMutableList().apply {
            this[currentPlayerIndex] = currentPlayer.copy(currentHand = allDice)
        }

        if(currentPlayer == players.last()){
            return finishRound()
        }

        return this.copy(
            currentPlayer = players.elementAt(currentPlayerIndex + 1),
            players = updatedPlayers.toList(),
            rerollDice = allDice,
            keptDice = listOf())
    }

    fun finishRound(): Game {
        return this
    }
}