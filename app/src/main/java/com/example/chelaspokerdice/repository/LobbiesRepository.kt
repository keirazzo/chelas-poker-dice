package com.example.chelaspokerdice.repository
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player

interface LobbiesRepository {
    suspend fun getLobbies(): List<Lobby>
    suspend fun getLobby(lobbyId: String): Lobby?
    suspend fun addLobby(lobby: Lobby)
    suspend fun joinLobby(lobby: Lobby, player: Player)
    suspend fun leaveLobby(lobby: Lobby, player: Player)

}

class FakeLobbiesRepository : LobbiesRepository {
    private val lobbies = mutableListOf(
        Lobby("Lobby 1", "first created lobby", 1, 3, 5, listOf(Player("Alice"))),
        Lobby("Lobby 2", "second created lobby", 1, 3, 5, listOf(Player("Jack"))),
        Lobby("Full Lobby", "full lobby that should not show on screen", 6, 6, 5, listOf(Player("Alice")))
    )

    override suspend fun getLobbies(): List<Lobby> = lobbies

    override suspend fun getLobby(lobbyId: String): Lobby? = lobbies.find { lobby -> lobby.id == lobbyId }

    override suspend fun addLobby(lobby: Lobby) { lobbies.add(lobby)}

    override suspend fun joinLobby(lobby: Lobby, player: Player) {
        val index = lobbies.indexOf(lobby)
        if (index != -1){
            val updatedLobby = lobby.copy(
                players = lobby.players + player,
                numberOfPlayers = lobby.numberOfPlayers + 1
            )
            lobbies[index] = updatedLobby
        }
    }

    override suspend fun leaveLobby(lobby: Lobby, player: Player) {
        val index = lobbies.indexOf(lobby)
        if (index != -1){
            val updatedLobby = lobby.copy(
                players = lobby.players - player,
                numberOfPlayers = lobby.numberOfPlayers - 1
            )
            if (updatedLobby.isEmpty()) lobbies.removeAt(index)
            else lobbies[index] = updatedLobby
        }
    }
}