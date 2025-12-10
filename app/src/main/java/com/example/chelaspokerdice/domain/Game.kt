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

    fun isRoundFinished(): Boolean = currentPlayer == players.last()
    fun isGameFinished(): Boolean = currentRound == numberOfRounds
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
        var currentPlayerIndex = players.indexOf(currentPlayer)
        val updatedPlayers = players.toMutableList().apply {
            this[currentPlayerIndex] = currentPlayer.copy(currentHand = allDice)
        }

        if(currentPlayer == players.last()) currentPlayerIndex = currentPlayerIndex.dec()


        return this.copy(
            currentPlayer = players.elementAt(currentPlayerIndex + 1),
            players = updatedPlayers.toList(),
            rerollDice = allDice,
            keptDice = listOf())
    }

    fun finishRound(): Game {
        val resetPlayers = players.map { it.copy(currentHand = listOf()) }
        val shiftedPlayers = resetPlayers.drop(1) + resetPlayers.first()

        return this.copy(
            currentRound = currentRound.inc(),
            players = shiftedPlayers,
            currentPlayer = shiftedPlayers.first(),
            rerollDice = List(5){ index -> Dice(0, "", index + 1).roll()},
            keptDice = listOf()
        )
    }

    fun updatePlayerScore(player: Player): Game {
        val updatedPlayers = players.toMutableList().apply {
            this[players.indexOf(player)] = player.copy(score = player.score.inc())
        }

        return this.copy( players = updatedPlayers)
    }

    fun getRoundWinner(): Player {
        return players.maxBy { player -> player.currentHand.sumOf { dice -> dice.number }}
    }
}