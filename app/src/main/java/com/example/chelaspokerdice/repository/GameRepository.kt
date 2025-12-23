package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface GameRepository {
    fun getGame(gameId: String): Flow<Game?>
    suspend fun saveGame(game: Game)
}

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