package com.example.chelaspokerdice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chelaspokerdice.ui.AboutScreen
import com.example.chelaspokerdice.ui.GameScreen
import com.example.chelaspokerdice.ui.GameScreenNavigationIntent
import com.example.chelaspokerdice.ui.LobbiesScreen
import com.example.chelaspokerdice.ui.LobbiesScreenNavigationIntent
import com.example.chelaspokerdice.ui.LobbyCreationScreen
import com.example.chelaspokerdice.ui.LobbyScreen
import com.example.chelaspokerdice.ui.LobbyScreenNavigationIntent
import com.example.chelaspokerdice.ui.ProfileScreen
import com.example.chelaspokerdice.ui.TitleScreen
import com.example.chelaspokerdice.ui.TitleScreenNavigationIntent

enum class AppScreen{
    Title,
    Profile,
    About,
    Lobbies,
    LobbyCreation,
    Lobby,
    Game

}

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreen.Title.name, builder = {

        composable(AppScreen.Title.name){ TitleScreen(
            onNavigate = { intent -> when (intent) {
                TitleScreenNavigationIntent.NavigateToSoloGame -> navController.navigate("${AppScreen.Game.name}/SOLO")
                TitleScreenNavigationIntent.NavigateToLobbies -> navController.navigate(AppScreen.Lobbies.name)
                TitleScreenNavigationIntent.NavigateToProfile -> navController.navigate(AppScreen.Profile.name)
                TitleScreenNavigationIntent.NavigateToAbout -> navController.navigate(AppScreen.About.name)
            } }
        )}

        composable (AppScreen.Profile.name){ ProfileScreen() }

        composable (AppScreen.Lobbies.name){ LobbiesScreen(
           onNavigate = { intent -> when (intent) {
               LobbiesScreenNavigationIntent.NavigateToLobbyCreation -> navController.navigate(AppScreen.LobbyCreation.name)
               is LobbiesScreenNavigationIntent.NavigateToLobby -> navController.navigate("${AppScreen.Lobby.name}/${intent.lobbyId}")
               LobbiesScreenNavigationIntent.NavigateToTitle -> navController.navigate(AppScreen.Title.name)
           } }


        )}

        composable (AppScreen.About.name){ AboutScreen() }
        composable(AppScreen.LobbyCreation.name){ LobbyCreationScreen(
            onNavigate = { intent -> navController.navigate("${AppScreen.Lobby.name}/${intent.lobbyId}") }
        ) }

        composable("${ AppScreen.Lobby.name}/{lobbyId}",
            listOf(
                navArgument("lobbyId"){
                    type = NavType.StringType
                    nullable = false
                }
            )){ backStackEntry ->
                LobbyScreen(onNavigate = { intent -> when (intent){
                    LobbyScreenNavigationIntent.NavigateToLobbies -> navController.navigate(AppScreen.Lobbies.name)
                    is LobbyScreenNavigationIntent.NavigateToGame -> navController.navigate("${AppScreen.Game.name}/${intent.gameId}")
                }
                    })
            }

        composable ("${ AppScreen.Game.name}/{gameId}",
            listOf(
                navArgument("gameId"){
                    type = NavType.StringType
                    nullable = false
                }
            )
            ){ backStackEntry ->
            GameScreen(onNavigate = { intent -> when (intent){
                GameScreenNavigationIntent.NavigateToTitle -> navController.navigate(AppScreen.Title.name)
                is GameScreenNavigationIntent.NavigateToLobby -> navController.popBackStack()
            } })
        }
    })
}