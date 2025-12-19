package com.example.chelaspokerdice.di

import com.example.chelaspokerdice.repository.FakeGameRepository
import com.example.chelaspokerdice.repository.FakeUserRepository
import com.example.chelaspokerdice.repository.FirestoreLobbiesRepository
import com.example.chelaspokerdice.repository.GameRepository
import com.example.chelaspokerdice.repository.LobbiesRepository
import com.example.chelaspokerdice.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideLobbiesRepository( db: FirebaseFirestore): LobbiesRepository {
        return FirestoreLobbiesRepository(db)
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return FakeUserRepository()
    }

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository {
        return FakeGameRepository()
    }
}