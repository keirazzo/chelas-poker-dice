package com.example.chelaspokerdice.repository

import com.example.chelaspokerdice.domain.Player
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

interface UserRepository {
    suspend fun getPlayer(): Player
    suspend fun savePlayer(player: Player)
    suspend fun editUsername(newName: String)
}

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreUserRepository(private val context: Context): UserRepository {
    private val mutex = Mutex()
    private val gson = Gson()
    private val userKey = stringPreferencesKey("user_data")

    override suspend fun getPlayer(): Player = mutex.withLock {
        val prefs = context.dataStore.data.first()
        val json = prefs[userKey]

        return if (json != null) {
            gson.fromJson(json, Player::class.java)
        } else {
            val newId = java.util.UUID.randomUUID().toString()
            val uniqueName = "Player_${Random.nextInt(1000, 9999)}"
            val newPlayer = Player(id = newId, name = uniqueName)

            val jsonToSave = gson.toJson(newPlayer)
            context.dataStore.edit { it[userKey] = jsonToSave }
            newPlayer
        }
    }

    override suspend fun savePlayer(player: Player) {
        context.dataStore.edit { prefs ->
            val json = gson.toJson(player)
            prefs[userKey] = json
        }
    }

    override suspend fun editUsername(newName: String) {
        val newPlayer = getPlayer().copy(name = newName)
        savePlayer(newPlayer)

    }
}

//class FakeUserRepository : UserRepository {
//    private val user = Player("Keira")
//
//    override suspend fun getPlayer() = user
//
//}
