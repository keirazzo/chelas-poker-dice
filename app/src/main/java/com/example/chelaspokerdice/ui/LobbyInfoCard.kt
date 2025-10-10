package com.example.chelaspokerdice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chelaspokerdice.R

@Composable
fun LobbyInfoCard(name: String, description: String, numberOfPlayers: Int, numberOfRounds: Int, onNavigate: () -> Unit = {}) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth().padding(15.dp)
    ){
        Text(name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(15.dp),
            textAlign = TextAlign.Center,
        )

        Text(description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(15.dp, 0.dp, 0.dp, 10.dp),
        )

        Row(modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, end = 15.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Column{
                Text(stringResource(R.string.players_count, 0, numberOfPlayers),
                    style = MaterialTheme.typography.bodySmall)
                Text(stringResource(R.string.rounds_count, numberOfRounds),
                    style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = {onNavigate()}) {
                Text(stringResource(R.string.join_button))
            }
        }


    }
}

@Composable
@Preview()
fun LobbyInfoCardPreview(){
    LobbyInfoCard("Lobby 1", "First lobby made", 6, 5)
}