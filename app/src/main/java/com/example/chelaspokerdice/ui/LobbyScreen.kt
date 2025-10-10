package com.example.chelaspokerdice.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LobbyScreen (){
    Text("LOBBY SCREEN");
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LobbyScreenPreview(){
    LobbyScreen()
}