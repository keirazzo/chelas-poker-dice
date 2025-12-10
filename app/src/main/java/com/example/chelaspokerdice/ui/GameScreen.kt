package com.example.chelaspokerdice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.viewmodel.GameState
import com.example.chelaspokerdice.viewmodel.GameViewModel
import com.example.chelaspokerdice.viewmodel.TurnState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigate: ()-> Unit = {}){
    val viewModel = hiltViewModel<GameViewModel>()
    val _game by viewModel.game.collectAsState()
    val turnState by viewModel.turnState.collectAsState()
    val gameState by viewModel.gameState.collectAsState()

    if (_game ==null){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.loading))
        }
        return
    }

    val game = _game!!

    if (gameState is GameState.PlayingRound) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                ),
                title = {
                    Text(game.name)
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(stringResource(R.string.playing, game.currentPlayer.name))
                Text(stringResource(R.string.current_round, game.currentRound, game.numberOfRounds))
            }

            if (turnState is TurnState.Rerolls) {
                Text(
                    stringResource(R.string.rerolls_left, game.rerolls),
                    Modifier.padding(start = 10.dp)
                )
            }

            Column(Modifier.padding(10.dp)) {
                game.players.forEach { player -> PlayerHand(player) }
            }

            if (turnState is TurnState.FirstRoll) {
                Button(
                    onClick = { viewModel.rollDice() }, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(250.dp, 75.dp)
                ) {
                    Text(stringResource(R.string.roll), style = MaterialTheme.typography.bodyLarge)
                }
            }

            if (turnState is TurnState.Rerolls || turnState is TurnState.NoRerolls) {

                if (turnState is TurnState.Rerolls) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        game.rerollDice.forEach { dice ->
                            DiceButton(
                                dice.symbol,
                                { viewModel.toggleDice(dice) })
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    game.keptDice.forEach { dice ->
                        DiceButton(
                            dice.symbol,
                            { viewModel.toggleDice(dice) })
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.Center, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    if (turnState is TurnState.Rerolls) {
                        Button(
                            onClick = { viewModel.rerollDice() },
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(stringResource(R.string.reroll))
                        }
                    }

                    Button(onClick = { viewModel.confirmHand() }) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }


        }
    }

    if (gameState is GameState.EndOfRound){
        Column( modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text("End of round ${game.currentRound}", style = MaterialTheme.typography.headlineLarge)
            val sortedPlayers = game.players.sortedByDescending{ 0 }
            val roundWinner = sortedPlayers.first()
            Text("Winner: ${roundWinner.name}", style = MaterialTheme.typography.headlineSmall)

            sortedPlayers.forEach { player ->  PlayerHand(player)}
        }
    }

    if (gameState is GameState.EndOfGame){
        Column (modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            val sortedPlayers = game.players.sortedByDescending { it.score }
            Text("Winner: ${sortedPlayers.first().name}", style = MaterialTheme.typography.headlineLarge)

            sortedPlayers.forEach { player -> Text("${player.score}    ${player.name}") }
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GameScreenPreview(){
    GameScreen()
}