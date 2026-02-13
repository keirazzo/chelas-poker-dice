package com.example.chelaspokerdice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelaspokerdice.R

enum class TitleScreenNavigationIntent {
    NavigateToSoloGame,
    NavigateToLobbies,
    NavigateToProfile,
    NavigateToAbout
}

@Composable
fun TitleScreen(onNavigate: (TitleScreenNavigationIntent) -> Unit = { }) {
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text(stringResource(R.string.app_title),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 200.dp))

        Button(onClick = {onNavigate(TitleScreenNavigationIntent.NavigateToLobbies)},
            modifier = Modifier
                .padding(top = 50.dp)
                .size(250.dp, 75.dp)) {
            Text(text = stringResource(R.string.multiplayer),
                style = MaterialTheme.typography.bodyLarge)
        }

        Button(onClick = {onNavigate(TitleScreenNavigationIntent.NavigateToSoloGame)},
            modifier = Modifier
                .padding(top = 20.dp)
                .size(250.dp, 75.dp)) {
            Text(text = stringResource(R.string.solo),
                style = MaterialTheme.typography.bodyLarge)
        }

        Row (modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center){

            IconButton(onClick = {onNavigate(TitleScreenNavigationIntent.NavigateToProfile)},
                modifier = Modifier.padding(end = 30.dp)) {
                Icon(imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(R.string.profile),
                    modifier = Modifier.size(150.dp))
            }
            IconButton(onClick = {onNavigate(TitleScreenNavigationIntent.NavigateToAbout)}) {
                Icon(imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.about),
                    modifier = Modifier.size(150.dp))
            }
        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TitleScreenPreview() {
    TitleScreen()
}