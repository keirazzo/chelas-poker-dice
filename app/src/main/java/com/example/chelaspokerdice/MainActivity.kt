package com.example.chelaspokerdice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chelaspokerdice.ui.AboutScreen
import com.example.chelaspokerdice.ui.LobbiesScreen
import com.example.chelaspokerdice.ui.LobbyCreationScreen
import com.example.chelaspokerdice.ui.ProfileScreen
import com.example.chelaspokerdice.ui.TitleScreen
import com.example.chelaspokerdice.ui.theme.ChelasPokerDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasPokerDiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "title", builder = {
                        composable("title"){TitleScreen(navController)}
                        composable ("profile"){ ProfileScreen() }
                        composable ("lobbies"){ LobbiesScreen(navController) }
                        composable ("about"){ AboutScreen() }
                        composable("lobby_creation"){ LobbyCreationScreen() }
                    })
                }
            }
        }
    }
}
