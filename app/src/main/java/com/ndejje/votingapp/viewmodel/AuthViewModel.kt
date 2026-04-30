package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // UI State for tracking registration/login results
    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authState: StateFlow<AuthResult> = _authState

    // --- REGISTRATION LOGIC ---
    fun registerUser(user: UserEntity) {
        _authState.value = AuthResult.Loading
        viewModelScope.launch {
            val result = repository.register(user)
            if (result != -1L) {
                _authState.value = AuthResult.Success("Registration Successful!")
            } else {
                _authState.value = AuthResult.Error("User already exists!")
            }
        }
    }

    // --- LOGIN LOGIC ---
    fun loginUser(regNo: String, pass: String) {
        _authState.value = AuthResult.Loading
        viewModelScope.launch {
            val user = repository.login(regNo)
            if (user != null && user.password == pass) {
                _authState.value = AuthResult.Success("Welcome, ${user.fullName}")
            } else {
                _authState.value = AuthResult.Error("Invalid credentials!")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthResult.Idle
    }
}

// A sealed class to represent different UI states cleanly
sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}