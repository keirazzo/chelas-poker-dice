# 🎲 Chelas Poker Dice
An Android multiplayer Poker Dice game, created as a project for my Programming for Mobile Devices class.

## Technologies
- Kotlin
- Jetpack Compose
- Firebase Firestore
- Android Datastore
- Android Studio
- Hilt

## Features
- Online multiplayer, using Firebase Firestore
- Dice-based gameplay, real-time gameplay updates across devices
- Profile page, to check stats and change your username

## The process
- Created the first screens with simple UI using Jetpack Compose
- Added navigation using NavHost and NavController
- Created data classes for lobbies, games, and players
- Built fake repositories for testing
- Implemented dependency injection using Hilt
- Developed ViewModels for UI-repository communication
- Implemented game logic
- Integrated Firestore and Datastore for real database functionality

## What I learned
- The architecture of an Android app, its different layers and their usages
- The basics of the Kotlin language
- The creation of a UI using Jetpack Compose and the Material Design 3 theme

## How to run the project
1. Clone the repository
2. Add your own 'google-services.json' (Firebase config)
3. Open in Android Studio and run on your own device or emulator
