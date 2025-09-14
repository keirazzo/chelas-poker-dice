package com.example.chelaspokerdice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chelaspokerdice.ui.AboutScreen
import com.example.chelaspokerdice.ui.LobbiesScreen
import com.example.chelaspokerdice.ui.ProfileScreen
import com.example.chelaspokerdice.ui.TitleScreen
import com.example.chelaspokerdice.ui.theme.ChelasPokerDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasPokerDiceTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "title", builder = {
                    composable("title"){TitleScreen(navController)}
                    composable ("profile"){ ProfileScreen() }
                    composable ("lobbies"){ LobbiesScreen() }
                    composable ("about"){ AboutScreen() }
                })
            }
        }
    }
}
