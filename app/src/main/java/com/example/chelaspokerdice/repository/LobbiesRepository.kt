package com.example.chelaspokerdice.repository
import androidx.compose.runtime.snapshotFlow
import com.example.chelaspokerdice.domain.Lobby
import com.example.chelaspokerdice.domain.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

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
            Lobby("Lobby 1", "first created lobby", 2, 3, 2, listOf(Player("Alice"), Player("Max"))),
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

class FirestoreLobbiesRepository @Inject constructor(
    private val db: FirebaseFirestore
): LobbiesRepository {
    private val lobbies = db.collection("lobbies")

    override fun getLobbies(): Flow<List<Lobby>> = callbackFlow {
        val subscription = lobbies.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObjects<Lobby>() ?: emptyList())
        }
        awaitClose { subscription.remove() }
    }

    override fun getLobbyStream(lobbyId: String): Flow<Lobby?> = callbackFlow {
        val subscription = lobbies.document(lobbyId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject<Lobby>())
        }
        awaitClose { subscription.remove() }
    }

    override suspend fun getLobby(lobbyId: String): Lobby? {
        return lobbies.document(lobbyId).get().await().toObject<Lobby>()
    }

    override suspend fun addLobby(lobby: Lobby) {
        lobbies.document(lobby.id).set(lobby).await()
    }

    override suspend fun joinLobby(lobby: Lobby, player: Player) {
        val lobbyId = lobbies.document(lobby.id)
        db.runTransaction { transaction ->
            val currentLobby = transaction.get(lobbyId).toObject<Lobby>()
            if (currentLobby != null){
                val updatedPlayers = currentLobby.players + player
                transaction.update(lobbyId, "players", updatedPlayers)
                transaction.update(lobbyId, "numberOfPlayers", updatedPlayers.size)
            }
        }.await()
    }

    override suspend fun leaveLobby(lobby: Lobby, player: Player) {
        val lobbyId = lobbies.document(lobby.id)
        val updatedPlayers = lobby.players.filter { it.name != player.name }
        if (updatedPlayers.isEmpty()){
            lobbyId.delete().await()
        } else {
            lobbies.document(lobby.id).update(
                "players", updatedPlayers,
                "numberOfPlayers", updatedPlayers.size
            ).await()
        }
    }
}