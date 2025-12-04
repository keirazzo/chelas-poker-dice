package com.example.chelaspokerdice.repository
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update

interface LobbiesRepository {
     fun getLobbies(): Flow<List<Lobby>>
     fun getLobbyStream(lobbyId: String): Flow<Lobby?>
    suspend fun getLobby(lobbyId: String): Lobby?
    suspend fun addLobby(lobby: Lobby)
    suspend fun joinLobby(lobby: Lobby, player: Player)
    suspend fun leaveLobby(lobby: Lobby, player: Player)

}

class FakeLobbiesRepository : LobbiesRepository {
    private val lobbies = mutableMapOf<String, MutableStateFlow<Lobby>>()
    init {
        listOf(
            Lobby("Lobby 1", "first created lobby", 2, 3, 5, listOf(Player("Alice"), Player("Max"))),
            Lobby("Lobby 2", "second created lobby", 1, 3, 5, listOf(Player("Jack"))),
            Lobby("Full Lobby", "full lobby that should not show on screen", 6, 6, 5, listOf(Player("Alice")))
        ).forEach { lobby -> lobbies[lobby.id] = MutableStateFlow(lobby) }
    }

    override fun getLobbies(): Flow<List<Lobby>> {
        return lobbies.values.map { it as Flow<Lobby> }.merge().map{lobbies.values.map { it.value }}
    }

    override fun getLobbyStream(lobbyId: String): Flow<Lobby?> {
        return lobbies[lobbyId] ?: MutableStateFlow(null)
    }
    override suspend fun getLobby(lobbyId: String): Lobby? = lobbies[lobbyId]?.value

    override suspend fun addLobby(lobby: Lobby) { lobbies[lobby.id] = MutableStateFlow(lobby)}

    override suspend fun joinLobby(lobby: Lobby, player: Player) {
        lobbies[lobby.id]?.update { currentLobby ->
            currentLobby.copy(
                players = currentLobby.players + player,
                numberOfPlayers = currentLobby.numberOfPlayers + 1
            )
        }
    }

    override suspend fun leaveLobby(lobby: Lobby, player: Player) {
        lobbies[lobby.id]?.update { currentLobby ->
            val updatedLobby = currentLobby.copy(
                players = currentLobby.players - player,
                numberOfPlayers = currentLobby.numberOfPlayers - 1
            )
            updatedLobby
        }
    }
}