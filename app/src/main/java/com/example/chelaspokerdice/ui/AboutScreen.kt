package com.example.chelaspokerdice.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun AboutScreen() {
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text("About",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 150.dp)
            )

        Text("Welcome to Chelas Poker Dice!",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 30.dp))

        Text("To start playing, click on 'START MATCH' and either join or create a new lobby.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp))

        Text("Once the game starts, the goal is to roll the highest-ranking poker " +
                "hand among all players within a single round. You get three re-rolls in total.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp))

        Text("Dice combinations are ranked in descending order of strength as follows: Five of " +
                "a Kind, Four of a Kind, Full House, Straight, Three of a Kind, Two Pair, One Pair, " +
                "and finally Bust ",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp))

        val context = LocalContext.current

        Text("Full gameplay rules",
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable{
            val intent = Intent(Intent.ACTION_VIEW,
                "https://gathertogethergames.com/poker-dice#google_vignette".toUri())
            context.startActivity(intent)
        })

        Text("Made by: Keira Kabongo-Barazzoli, nr.(unknown)",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 30.dp))

        IconButton(onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:01106950@pw.edu.pl".toUri()
            }

            context.startActivity(intent)
        }) {
            Icon(imageVector = Icons.Outlined.Email,
                contentDescription = "Profile"
            )
        }

    }

}

@Composable
@Preview
fun AboutScreenPreview() {
    AboutScreen()
}