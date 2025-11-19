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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.viewmodel.LobbiesViewModel

sealed class LobbiesScreenNavigationIntent {
    data class NavigateToLobby(val lobbyId: String) : LobbiesScreenNavigationIntent()
    data object NavigateToLobbyCreation: LobbiesScreenNavigationIntent()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbiesScreen(onNavigate: (LobbiesScreenNavigationIntent) -> Unit = { }) {

    val viewModel = hiltViewModel<LobbiesViewModel>()
    val lobbies by viewModel.lobbies.collectAsState()

    Column (modifier = Modifier.fillMaxSize()){
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
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
            LobbyInfoCard(lobby.name, lobby.description, lobby.numberOfPlayers, lobby.maxNumberOfPlayers, lobby.numberOfRounds) {
                viewModel.joinLobby(lobby)
                onNavigate(LobbiesScreenNavigationIntent.NavigateToLobby(lobby.id))
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbiesScreenPreview() {
    LobbiesScreen()
}