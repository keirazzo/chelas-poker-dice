package com.example.chelaspokerdice

import com.example.chelaspokerdice.domain.Dice
import com.example.chelaspokerdice.domain.Game
import com.example.chelaspokerdice.domain.HandType
import com.example.chelaspokerdice.domain.Player
import org.junit.Test

import org.junit.Assert.*

class GameTest {
    private fun createDice(vararg values: Int): List<Dice> {
        return values.mapIndexed { index, number ->
            Dice(number, number.toString(), index + 1)
        }
    }

    private fun createPlayer(name: String, hand: List<Dice>): Player {
        return Player(name = name, score = 0, currentHand = hand)
    }
    private val game = Game()

    @Test
    fun analyzeHand_FiveOfAKind() {
        val hand = createDice(14, 14, 14, 14, 14)
        assertEquals(listOf(HandType.FIVE_OF_A_KIND.rank, 14), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_FourOfAKind() {
        val hand = createDice(13, 13, 13, 13, 10)
        assertEquals(listOf(HandType.FOUR_OF_A_KIND.rank, 13, 10), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_FullHouse() {
        val hand = createDice(12, 12, 12, 9, 9)
        assertEquals(listOf(HandType.FULL_HOUSE.rank, 12, 9), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_Straight() {
        val hand = createDice(14, 13, 12, 11, 10)
        assertEquals(listOf(HandType.STRAIGHT.rank, 14), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_ThreeOfAKind() {
        val hand = createDice(11, 11, 11, 14, 9)
        assertEquals(listOf(HandType.THREE_OF_A_KIND.rank, 11, 14, 9), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_TwoPair() {
        val hand = createDice(14, 14, 13, 13, 10)
        assertEquals(listOf(HandType.TWO_PAIR.rank, 14, 13, 10), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_Pair() {
        val hand = createDice(10, 10, 14, 12, 11)
        assertEquals(listOf(HandType.PAIR.rank, 10, 14, 12, 11), game.analyzeHand(hand))
    }

    @Test
    fun analyzeHand_Bust() {
        val hand = createDice(14, 13, 12, 11, 9)
        assertEquals(listOf(HandType.BUST.rank, 14, 13, 12, 11, 9), game.analyzeHand(hand))
    }


    @Test
    fun compareHands_TypeVS_Type() {
        val player1 = createPlayer("Full", createDice(14, 14, 14, 10, 10))
        val player2 = createPlayer("3 of a kind", createDice(13, 13, 13, 14, 12))

        assertTrue(game.compareHands(player1, player2) > 0)
        assertTrue(game.compareHands(player2, player1) < 0)
    }

    @Test
    fun compareHands_TieBreaker_PairValue() {
        val player1 = createPlayer("Pair 14", createDice(14, 14, 13, 12, 11))
        val player2 = createPlayer("Pair 13", createDice(13, 13, 14, 12, 11))

        assertTrue(game.compareHands(player1, player2) > 0)
    }

    @Test
    fun compareHands_TieBreaker_Kicker() {
        val player1 = createPlayer("P13 A", createDice(13, 13, 14, 11, 10))
        val player2 = createPlayer("P13 Q", createDice(13, 13, 12, 11, 10))

        assertTrue(game.compareHands(player1, player2) > 0)
    }

    @Test
    fun compareHands_PerfectTie() {
        val handValues = createDice(12, 12, 11, 11, 9)
        val player1 = createPlayer("Tie 1", handValues)
        val player2 = createPlayer("Tie 2", handValues)

        assertEquals(0, game.compareHands(player1, player2))
    }
}