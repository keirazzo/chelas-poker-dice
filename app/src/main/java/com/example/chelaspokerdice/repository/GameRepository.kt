package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Qualifier

interface GameRepository {
    fun getGame(gameId: String): Flow<Game?>
    suspend fun saveGame(game: Game)
    suspend fun deleteGame(gameId: String)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Multiplayer

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Solo

@Multiplayer
class FireStoreGameRepository @Inject constructor(
    db: FirebaseFirestore
): GameRepository {
    private val games = db.collection("games")

    override fun getGame(gameId: String): Flow<Game?> = callbackFlow {
        val subscription = games.document(gameId)
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }
                val game = snapshot?.toObject<Game>()
                trySend(game)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun saveGame(game: Game) {
        games.document(game.id).set(game).await()
    }

    override suspend fun deleteGame(gameId: String) {
        games.document(gameId).delete().await()
    }
}

@Solo
class SoloGameRepository @Inject constructor (): GameRepository {
    private val _game = MutableStateFlow<Game?>(null)

    override fun getGame(gameId: String): Flow<Game?> = _game
    override suspend fun saveGame(game: Game){ _game.update { game }}
    override suspend fun deleteGame(gameId: String){ _game.update { null } }

}

class HybridGameRepository @Inject constructor (
    @Solo private val solo: GameRepository,
    @Multiplayer private val multiplayer: GameRepository
): GameRepository {

    override fun getGame(gameId: String): Flow<Game?>{
        return if (gameId.contains("SOLO")) solo.getGame(gameId)
        else multiplayer.getGame(gameId)
    }
    override suspend fun saveGame(game: Game){
        return if (game.id.contains("SOLO")) solo.saveGame(game)
        else multiplayer.saveGame(game)
    }
    override suspend fun deleteGame(gameId: String){
        return if (gameId.contains("SOLO")) solo.deleteGame(gameId)
        else multiplayer.deleteGame(gameId)
    }

}

//class FakeGameRepository : GameRepository {
//    private val games = mutableMapOf<String, MutableStateFlow<Game?>>()
//
//    override fun getGame(gameId: String): Flow<Game?> {
//        return games.getOrPut(gameId){ MutableStateFlow(null)}
//    }
//
//    override suspend fun saveGame(game: Game) {
//        val flow = games.getOrPut(game.id) {MutableStateFlow(null)}
//        flow.value = game
//    }
//}