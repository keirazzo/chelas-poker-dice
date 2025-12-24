package com.example.chelaspokerdice.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.viewmodel.GameViewModel

sealed class GameScreenNavigationIntent{
    data object NavigateToTitle: GameScreenNavigationIntent()
    data object NavigateToLobby: GameScreenNavigationIntent()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigate: (GameScreenNavigationIntent) -> Unit = {}) {
    val viewModel = hiltViewModel<GameViewModel>()
    val currentGame by viewModel.game.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(currentGame?.state) {
        if (currentGame?.state == "LOBBY") {
            onNavigate(GameScreenNavigationIntent.NavigateToLobby)
        }
    }

    if (currentGame == null || currentUser == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.loading))
        }
        return
    }

    val game = currentGame!!
    val isMyTurn = viewModel.isMyTurn()

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.leave_match)) },
            text = { Text(stringResource(R.string.leave_match_warning)) },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.leaveGame()
                        onNavigate(GameScreenNavigationIntent.NavigateToTitle)
                    }
                ) {
                    Text(stringResource(R.string.leave))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showExitDialog = false }) {
                    Text(stringResource(R.string.stay))
                }
            }
        )
    }

    when (game.state) {
        "PLAYING" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    title = { Text(game.name) },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(if (isMyTurn) stringResource(R.string.your_turn) else stringResource(R.string.playing, game.currentPlayer.name))
                    Text(stringResource(R.string.current_round, game.currentRound, game.numberOfRounds))
                }

                if (game.turnPhase == "REROLLS") {
                    Text(stringResource(R.string.rerolls_left, game.rerolls), Modifier.padding(start = 10.dp))
                }

                Column(Modifier.padding(10.dp)) {
                    game.players.forEach { player -> PlayerHand(player) }
                }

                if (isMyTurn) {
                    if (game.turnPhase == "FIRST_ROLL") {
                        Button(
                            onClick = { viewModel.rollDice() },
                            modifier = Modifier.align(Alignment.CenterHorizontally).size(250.dp, 75.dp)
                        ) {
                            Text(stringResource(R.string.roll), style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    if (game.turnPhase == "REROLLS" || game.turnPhase == "NO_REROLLS") {
                        val currentHand = game.keptDice + game.rerollDice
                        val handType = game.getHandType(currentHand)

                        Text(
                            text = handType.type,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (game.turnPhase == "REROLLS") {
                            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                game.rerollDice.forEach { dice ->
                                    DiceButton(dice.symbol) { viewModel.toggleDice(dice) }
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            game.keptDice.forEach { dice ->
                                DiceButton(dice.symbol) { viewModel.toggleDice(dice) }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                            if (game.turnPhase == "REROLLS") {
                                Button(onClick = { viewModel.rerollDice() }, modifier = Modifier.padding(end = 10.dp)) {
                                    Text(stringResource(R.string.reroll))
                                }
                            }
                            Button(onClick = { viewModel.confirmHand() }) {
                                Text(stringResource(R.string.confirm))
                            }
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            (game.keptDice + game.rerollDice).forEach { dice ->
                                DiceButton(dice.symbol){}
                            }
                        }
                    }
                }
            }
        }

        "END_OF_ROUND" -> {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.end_of_round, game.currentRound), style = MaterialTheme.typography.headlineLarge)
                val roundWinner = game.getRoundWinner()
                Text(stringResource(R.string.winner, roundWinner.name), style = MaterialTheme.typography.headlineSmall)
                val sortedPlayers = remember(game.players) {
                    game.players.sortedWith { p1, p2 ->
                        game.compareHands(p1, p2)
                    }.reversed()
                }
                sortedPlayers.forEach { player -> PlayerHand(player) }
            }
        }

        "END_OF_GAME" -> {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val sortedPlayers = game.players.sortedByDescending { it.score }
                Text(stringResource(R.string.winner, sortedPlayers.first().name), style = MaterialTheme.typography.headlineLarge)
                sortedPlayers.forEach { player -> Text("${player.score}    ${player.name}") }

                Row {
                    Button(onClick = {
                        viewModel.leaveGame()
                        onNavigate(GameScreenNavigationIntent.NavigateToTitle)
                    }) { Text(stringResource(R.string.leave_lobby)) }

                    if (viewModel.isMyTurn()) {
                        Button(onClick = {
                            viewModel.playAgain()
                        }) {
                            Text(stringResource(R.string.new_game))
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GameScreenPreview() {
    GameScreen()
}