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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.domain.Dice
import com.example.chelaspokerdice.domain.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigate: ()-> Unit = {}){
//    val viewModel = hiltViewModel<GameViewModel>()
//    val game = viewModel.loadGame()

    Column (modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer)){
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
            ),
            title = {
                Text("Lobby 1") //game.name
            },
        )

        Row(modifier = Modifier.fillMaxWidth().padding(start= 10.dp, end= 10.dp, top= 10.dp), horizontalArrangement = Arrangement.Absolute.SpaceBetween){
            Text(stringResource(R.string.playing, "Keira"))
            Text(stringResource(R.string.current_round, 1, 2))
        }

        Text(stringResource(R.string.rerolls_left, 2),Modifier.padding(start= 10.dp))


        Column(Modifier.padding(10.dp)){
            PlayerHand(Player("Keira", listOf(
                Dice(9, "nine"),
                Dice(10, "ten"),
                Dice(10, "ten"),
                Dice(10, "ten"),
                Dice(10, "ten")
            )))
            PlayerHand(Player("Alice"))
        }
        Button(onClick = {}, modifier = Modifier.align(Alignment.CenterHorizontally).size(250.dp, 75.dp)) {
            Text(stringResource(R.string.roll), style = MaterialTheme.typography.bodyLarge)
        }

        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth() ){
            Dice("king")
            Dice("queen")
            Dice("jack")
            Dice("ten")
            Dice("nine")

        }


        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth() ){
            Dice("queen")
            Dice("jack")
            Dice("ten")
            Dice("nine")
            Dice("ace")

        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top=10.dp)){
            Button(onClick = {}, modifier = Modifier.padding(end=10.dp)) {
                Text(stringResource(R.string.reroll))
            }
            Button(onClick = {}) {
                Text(stringResource(R.string.confirm))
            }
        }


    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GameScreenPreview(){
    GameScreen()
}