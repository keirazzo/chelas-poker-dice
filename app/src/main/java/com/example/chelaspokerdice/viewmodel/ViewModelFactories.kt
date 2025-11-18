package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chelaspokerdice.repository.FakeLobbiesRepository
import com.example.chelaspokerdice.repository.LobbiesRepository

class LobbiesViewModelFactory(private val repository: LobbiesRepository = FakeLobbiesRepository()): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LobbiesViewModel::class.java)){
            return LobbiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}