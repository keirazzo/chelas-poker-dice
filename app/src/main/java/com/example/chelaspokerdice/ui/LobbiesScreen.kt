package com.example.chelaspokerdice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.viewmodel.LobbiesViewModel
import com.example.chelaspokerdice.viewmodel.LobbiesViewModelFactory

sealed class LobbiesScreenNavigationIntent {
    data class NavigateToLobby(val lobbyId: String) : LobbiesScreenNavigationIntent()
    data object NavigateToLobbyCreation: LobbiesScreenNavigationIntent()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbiesScreen(onNavigate: (LobbiesScreenNavigationIntent) -> Unit = { }) {

    val viewModel = viewModel<LobbiesViewModel>(factory = LobbiesViewModelFactory())
    val lobbies by viewModel.lobbies.collectAsState()

    Column (modifier = Modifier.fillMaxSize()){
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                Text(stringResource(R.string.lobbies_screen_title))
            },
            actions = {
                IconButton(onClick = {onNavigate(LobbiesScreenNavigationIntent.NavigateToLobbyCreation)}) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = stringResource(R.string.create_lobby)
                    )
                }
            }
        )

        LaunchedEffect(key1 = true) {
            viewModel.loadLobbies()
        }
        lobbies.forEach { lobby ->
            LobbyInfoCard(lobby.name, lobby.description, lobby.numberOfPlayers, lobby.maxNumberOfPlayers, lobby.numberOfRounds, {
                val currentPlayer = Player("TemporaryPlayer")
                viewModel.joinLobby(lobby, currentPlayer)
                onNavigate(LobbiesScreenNavigationIntent.NavigateToLobby(lobby.id)) })
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbiesScreenPreview() {
    LobbiesScreen()
}