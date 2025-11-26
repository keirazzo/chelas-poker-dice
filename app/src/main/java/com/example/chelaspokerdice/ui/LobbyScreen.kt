package com.example.chelaspokerdice.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelaspokerdice.viewmodel.LobbyViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chelaspokerdice.R
import com.example.chelaspokerdice.viewmodel.LobbyState

sealed class LobbyScreenNavigationIntent {
    data class NavigateToGame(val gameId: String): LobbyScreenNavigationIntent()
    data object NavigateToLobbies: LobbyScreenNavigationIntent()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen (onNavigate: (LobbyScreenNavigationIntent) -> Unit = {}){
    val viewModel = hiltViewModel<LobbyViewModel>()
    val lobby by viewModel.lobby.collectAsState()
    val lobbyState by viewModel.state.collectAsState()

    BackHandler {
        viewModel.leaveLobby()
        onNavigate(LobbyScreenNavigationIntent.NavigateToLobbies)
    }

    LaunchedEffect(lobbyState) {
        if (lobbyState is LobbyState.Full){
            val gameId = viewModel.createGame(lobby?.name ?: "", lobby?.players ?: listOf(),
                lobby?.numberOfRounds ?: 0
            )
            onNavigate(LobbyScreenNavigationIntent.NavigateToGame(gameId))
        }
    }

    Column (modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
            ),
            title = { Text(text = lobby?.name ?: stringResource(R.string.loading),
                style = MaterialTheme.typography.titleLarge) }
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(lobby?.description ?: stringResource(R.string.loading),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodyMedium)

            Text(stringResource(R.string.lobby_number_of_rounds, lobby?.numberOfRounds?: 0),
                modifier = Modifier.padding(10.dp))

        }

        Text(stringResource(R.string.lobby_players, lobby?.numberOfPlayers?: 0, lobby?.maxNumberOfPlayers?: 0),
            modifier = Modifier.padding(start = 20.dp),
            style = MaterialTheme.typography.headlineSmall)

        lobby?.players?.forEach { player ->
            Text(player.name,
                modifier = Modifier.padding(start = 25.dp, bottom = 5.dp))
        }
        Button(onClick = {
            viewModel.leaveLobby()
            onNavigate(LobbyScreenNavigationIntent.NavigateToLobbies)},
            modifier = Modifier.align(Alignment.CenterHorizontally).size(250.dp, 75.dp) ) {
                Text(stringResource(R.string.leave_lobby), style = MaterialTheme.typography.bodyLarge)
            }

    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbyScreenPreview(){
    LobbyScreen()
}