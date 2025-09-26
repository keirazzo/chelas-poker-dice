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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbiesScreen(onNavigate: () -> Unit = { }) {

    Column (modifier = Modifier.fillMaxSize()){
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                Text("Select a lobby")
            },
            actions = {
                IconButton(onClick = {onNavigate()}) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "create lobby"
                    )
                }
            }
        )

        LobbyInfoCard("Lobby 1", "fjsiroòjreiofjefieosvnjfieodjf", 10, 8)
        LobbyInfoCard("Lobby 2", "fjsiroòjreiofjefieosvnjfieodjf", 5, 15)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbiesScreenPreview() {
    LobbiesScreen()
}