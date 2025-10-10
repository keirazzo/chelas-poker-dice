package com.example.chelaspokerdice.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyCreationScreen(onNavigate: () -> Unit = {}){

    var lobbyName by remember { mutableStateOf("") }
    var lobbyDescription by remember { mutableStateOf("") }
    var numberOfRounds by remember { mutableStateOf("") }
    var numberOfPlayers by remember { mutableStateOf("") }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                Text("Create your lobby")
            }
        )

        OutlinedTextField(
            value = lobbyName,
            onValueChange = { newText -> lobbyName = newText},
            label = { Text("Name")},
            modifier = Modifier.fillMaxWidth().padding(30.dp)
        )

        OutlinedTextField(
            value = lobbyDescription,
            onValueChange = { newText -> lobbyDescription = newText},
            label = { Text("Description")},
            modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
        )

        Row {
            OutlinedTextField(
                value = numberOfRounds,
                onValueChange = { newText -> numberOfRounds = newText},
                label = { Text("N° of rounds")},
                modifier = Modifier.weight(1f).padding(30.dp)
            )
            OutlinedTextField(
                value = numberOfPlayers,
                onValueChange = { newText -> numberOfPlayers = newText},
                label = { Text("N° of players")},
                modifier = Modifier.weight(1f).padding(top = 30.dp, end = 30.dp)
            )
        }

        Button(onClick = {onNavigate()},
            modifier = Modifier
                .padding(top = 50.dp)
                .size(250.dp, 75.dp)) {
            Text("START MATCH", style = MaterialTheme.typography.bodyLarge)
        }

    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbyCreationScreenPreview(){
    LobbyCreationScreen()
}