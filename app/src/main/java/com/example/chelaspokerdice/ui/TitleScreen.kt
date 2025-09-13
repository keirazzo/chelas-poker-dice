package com.example.chelaspokerdice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun TitleScreen() {
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text("Chelas Poker Dice",
            fontSize = 60.sp,
            lineHeight = 1.2.em,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 150.dp))

        Button(onClick = {},
                modifier = Modifier
                    .padding(top = 50.dp)
                    .size(250.dp, 75.dp)) {
            Text("START MATCH",
                fontSize = 20.sp)
        }
    }
}

@Composable
@Preview
fun TitleScreenPreview() {
    TitleScreen()
}