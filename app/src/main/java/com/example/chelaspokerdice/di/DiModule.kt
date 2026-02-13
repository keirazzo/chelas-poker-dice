package com.example.chelaspokerdice.di

import android.content.Context
import com.example.chelaspokerdice.repository.DataStoreUserRepository
import com.example.chelaspokerdice.repository.FireStoreGameRepository
import com.example.chelaspokerdice.repository.FirestoreLobbiesRepository
import com.example.chelaspokerdice.repository.GameRepository
import com.example.chelaspokerdice.repository.HybridGameRepository
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.Multiplayer
import com.example.chelaspokerdice.repository.Solo
import com.example.chelaspokerdice.repository.SoloGameRepository
import com.example.chelaspokerdice.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideLobbiesRepository( db: FirebaseFirestore): LobbiesRepository {
        return FirestoreLobbiesRepository(db)
    }

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository {
        return DataStoreUserRepository(context)
    }

    @Provides
    @Singleton
    @Solo
    fun provideSoloGameRepository(): GameRepository {
        return SoloGameRepository()
    }

    @Provides
    @Singleton
    @Multiplayer
    fun provideFirestoreGameRepository(db: FirebaseFirestore): GameRepository {
        return FireStoreGameRepository(db)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        @Solo soloRepo: GameRepository,
        @Multiplayer multiRepo: GameRepository
    ): GameRepository {
        return HybridGameRepository(soloRepo, multiRepo)
    }
}