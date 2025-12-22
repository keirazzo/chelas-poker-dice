package com.example.chelaspokerdice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chelaspokerdice.domain.Player
import com.example.chelaspokerdice.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _user = MutableStateFlow<Player?>(null)
    val user = _user.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.getPlayer()
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            userRepository.editUsername(newName)
            loadUser()
        }
    }


}