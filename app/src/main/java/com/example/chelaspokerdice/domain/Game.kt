package com.example.chelaspokerdice.domain

import com.google.firebase.firestore.Exclude

enum class HandType(val rank: Int, val type: String){
    BUST(1, "Bust"),
    PAIR(2, "Pair"),
    TWO_PAIR(3, "Two Pair"),
    THREE_OF_A_KIND(4, "Three of a kind"),
    STRAIGHT(5, "Straight"),
    FULL_HOUSE(6, "Full House"),
    FOUR_OF_A_KIND(7, "Four of a kind"),
    FIVE_OF_A_KIND(8, "Five of a kind")
}
data class Game (
    val name: String = "",
    val players: List<Player> = listOf(),
    val numberOfRounds: Int = 0,
    val currentPlayer: Player = Player(""),
    val currentRound: Int = 1,
    val rerolls: Int = 0,
    val id: String = "",
    val state: String = "PLAYING",
    val turnPhase: String = "FIRST_ROLL",
    val keptDice: List<Dice> = listOf(),
    val rerollDice: List<Dice> = List(5){ index -> Dice(0, "", index + 1).roll()},
){

    fun isRoundFinished(): Boolean = currentPlayer.id == players.last().id
    fun isGameFinished(): Boolean = currentRound == numberOfRounds
    fun rollDice(): Game {
        val rolled = rerollDice.map { it.roll() }
        return this.copy(rerollDice = rolled)
    }

    fun toggleDice(dice: Dice): Game {
        if (turnPhase != "REROLLS") return this
        if(keptDice.contains(dice)) return this.copy(keptDice = keptDice - dice, rerollDice = rerollDice + dice)
        if(rerollDice.contains(dice)) return this.copy(keptDice = keptDice + dice, rerollDice = rerollDice - dice)
        return this
    }

    fun confirmDice(): Game {
        val confirmedDice = this.keptDice + this.rerollDice
        return this.copy(keptDice = confirmedDice, rerollDice = listOf())

    }

    fun startTurn(): Game {
        return this.rollDice().copy( rerolls = 2, turnPhase = "REROLLS")
    }

    fun processReroll(): Game {
        if (rerolls <= 0) return this

        val rolled = this.rollDice()
        val newRerolls = rerolls - 1

        return if (newRerolls == 0) {
            rolled.confirmDice().copy(rerolls = 0, turnPhase = "NO_REROLLS")
        } else {
            rolled.copy(rerolls = newRerolls)
        }
    }

    fun nextStep(): Game {
        val gameAfterTurn = this.finishTurn()

        return if (this.isRoundFinished()){
            val winner = gameAfterTurn.getRoundWinner()
            gameAfterTurn.updatePlayerScore(winner).copy(state = "END_OF_ROUND")
        } else {
            gameAfterTurn.copy(turnPhase = "FIRST_ROLL")
        }
    }

    fun finishTurn(): Game {
        val allDice = keptDice + rerollDice
        val currentPlayerIndex = players.indexOfFirst { it.id == currentPlayer.id }
        if (currentPlayerIndex == -1) return this

        val updatedPlayers = players.toMutableList().apply {
            this[currentPlayerIndex] = currentPlayer.copy(currentHand = allDice)
        }

        val nextPlayerIndex = (currentPlayerIndex + 1) % players.size

        return this.copy(
            currentPlayer = updatedPlayers[nextPlayerIndex],
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
            keptDice = listOf(),
            turnPhase = "FIRST_ROLL"
        )
    }

    fun updatePlayerScore(player: Player): Game {
        val updatedPlayers = players.toMutableList().apply {
            this[players.indexOf(player)] = player.copy(score = player.score.inc())
        }

        return this.copy( players = updatedPlayers)
    }

    @Exclude
    fun getRoundWinner(): Player {
        return players.maxWith(Comparator { p1, p2 ->
            compareHands(p1, p2)
        })
    }

    fun analyzeHand(hand: List<Dice>): List<Int>{
        val values: List<Int> = hand.map { it.number }.sortedDescending()
        val counts: Map<Int, Int> = values.groupBy { it }.mapValues { it.value.size }
        val sortedCounts = counts.entries.sortedByDescending { it.value * 100 + it.key }
        val maxCount = sortedCounts.firstOrNull()?.value ?: 0
        val countSize = counts.size
        val strength = mutableListOf<Int>()

        when (maxCount){
            5 -> {
                strength.add(HandType.FIVE_OF_A_KIND.rank)
                strength.add(sortedCounts.first().key)
            }

            4 -> {
                strength.add(HandType.FOUR_OF_A_KIND.rank)
                strength.add(sortedCounts[0].key)
                strength.add(sortedCounts[1].key)
            }

            3 -> {
                if (countSize == 2){
                    strength.add(HandType.FULL_HOUSE.rank)
                    strength.add(sortedCounts[0].key)
                    strength.add(sortedCounts[1].key)
                } else {
                    strength.add(HandType.THREE_OF_A_KIND.rank)
                    strength.add(sortedCounts[0].key)
                    strength.addAll(values.filter { it != sortedCounts[0].key }.sortedDescending())
                }
            }

            2 -> {
                if (countSize == 3) {
                    strength.add(HandType.TWO_PAIR.rank)
                    strength.addAll(sortedCounts.filter { it.value == 2 }.map { it.key }.sortedDescending())
                    strength.addAll(sortedCounts.filter { it.value == 1 }.map { it.key })
                } else {
                    strength.add(HandType.PAIR.rank)
                    strength.add(sortedCounts[0].key)
                    strength.addAll(values.filter { it != sortedCounts[0].key }.sortedDescending())
                }
            }

            else -> {
                val uniqueSortedValues = values.distinct().sortedDescending()
                if (uniqueSortedValues.size == 5 && (uniqueSortedValues.first() - uniqueSortedValues.last() == 4)){
                    strength.add(HandType.STRAIGHT.rank)
                    strength.add(uniqueSortedValues.first())
                }
                else {
                    strength.add(HandType.BUST.rank)
                    strength.addAll(values.sortedDescending())
                }
            }
        }

        return strength
    }

    @Exclude
    fun getHandType(hand: List<Dice>): HandType {
        val strength = analyzeHand(hand)
        val rank = strength.firstOrNull() ?: HandType.BUST.rank
        return HandType.entries.firstOrNull { it.rank == rank } ?: HandType.BUST
    }

    fun compareHands(player1: Player, player2: Player): Int{
        val hand1 = analyzeHand(player1.currentHand)
        val hand2 = analyzeHand(player2.currentHand)

        val max = maxOf(hand1.size, hand2.size)
        val padded1 = hand1 + List(max - hand1.size){0}
        val padded2 = hand2 + List(max - hand2.size){0}

        return padded1.zip(padded2).firstOrNull { it.first != it.second }?.let {
            it.first - it.second
        }?: 0

    }

    fun removePlayer(playerId: String): Game {
        val updatedPlayers = players.filter { it.id != playerId }
        val newCurrentPlayer = if (currentPlayer.id == playerId && updatedPlayers.isNotEmpty()) {
            updatedPlayers.first()
        } else {
            currentPlayer
        }
        return this.copy(players = updatedPlayers, currentPlayer = newCurrentPlayer)
    }

    fun prepareForLobby(): Game {
        return this.copy(
            state = "LOBBY",
            players = players.map { it.copy(score = 0, currentHand = listOf()) }
        )
    }
}