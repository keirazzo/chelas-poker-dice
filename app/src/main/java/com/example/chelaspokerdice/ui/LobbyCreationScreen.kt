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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.domain.Lobby.Companion.isDescriptionValid
import com.example.chelaspokerdice.domain.Lobby.Companion.isMaxNumberOfPlayersValid
import com.example.chelaspokerdice.domain.Lobby.Companion.isNameValid
import com.example.chelaspokerdice.domain.Lobby.Companion.isNumberOfRoundsValid
import com.example.chelaspokerdice.viewmodel.LobbiesViewModel
import kotlinx.coroutines.launch

data class NavigateToLobby(val lobbyId: String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyCreationScreen(onNavigate: (NavigateToLobby) -> Unit = {}){
    val viewModel = hiltViewModel<LobbiesViewModel>()
    val scope = rememberCoroutineScope()

    var lobbyName by remember { mutableStateOf("") }
    var lobbyDescription by remember { mutableStateOf("") }
    var numberOfRounds by remember { mutableStateOf("") }
    var numberOfPlayers by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }
    var isDescriptionError by remember { mutableStateOf(false) }
    var isNumberOfRoundsError by remember { mutableStateOf(false) }
    var isNumberOfPlayersError by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                Text(stringResource(R.string.lobby_creation_title))
            }
        )

        OutlinedTextField(
            value = lobbyName,
            onValueChange = {  newText ->
                lobbyName = newText
                isNameError = !isNameValid(newText)
                },
            label = { Text(stringResource(R.string.lobby_name_label))},
            isError = isNameError,
            supportingText = {
                if (isNameError) {
                    Text(
                        stringResource(R.string.invalid_name),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        )

        OutlinedTextField(
            value = lobbyDescription,
            onValueChange = { newText ->
                lobbyDescription = newText
                isDescriptionError = !isDescriptionValid(newText)
            },
            label = { Text(stringResource(R.string.lobby_description_label))},
            isError = isDescriptionError,
            supportingText = {
                if (isDescriptionError) {
                    Text(
                        stringResource(R.string.invalid_description),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        )

        Row {
            OutlinedTextField(
                value = numberOfRounds,
                onValueChange = { newText -> numberOfRounds = newText
                    val number = newText.toIntOrNull()
                    isNumberOfRoundsError = number == null || !isNumberOfRoundsValid(number)},
                label = { Text(stringResource(R.string.lobby_rounds_label))},
                isError = isNumberOfRoundsError,
                supportingText = {
                    if (isNumberOfRoundsError) {
                        Text(
                            stringResource(R.string.invalid_number_of_rounds),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(30.dp)
            )
            OutlinedTextField(
                value = numberOfPlayers,
                onValueChange = { newText -> numberOfPlayers = newText
                    val number = newText.toIntOrNull()
                    isNumberOfPlayersError = number == null || !isMaxNumberOfPlayersValid(number)},
                label = { Text(stringResource(R.string.lobby_players_label))},
                isError = isNumberOfPlayersError,
                supportingText = {
                    if (isNumberOfPlayersError) {
                        Text(
                            stringResource(R.string.invalid_number_of_players),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 30.dp, end = 30.dp)
            )
        }

        Button(onClick = {
            scope.launch{
                val lobbyId = viewModel.createLobby(
                    lobbyName,
                    lobbyDescription,
                    numberOfPlayers.toIntOrNull()!!,
                    numberOfRounds.toIntOrNull()!!
                )
                onNavigate(NavigateToLobby(lobbyId))
            }},
            modifier = Modifier
                .padding(top = 50.dp)
                .size(250.dp, 75.dp)) {
            Text(stringResource(R.string.start_match),
                style = MaterialTheme.typography.bodyLarge)
        }

    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbyCreationScreenPreview(){
    LobbyCreationScreen()
}