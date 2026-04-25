package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    // This will hold the "state" of the registration number globally
    private val _studentRegNumber = MutableStateFlow("")
    val studentRegNumber: StateFlow<String> = _studentRegNumber

    fun updateRegNumber(newNumber: String) {
        _studentRegNumber.value = newNumber
    }

    fun performLogin(): Boolean {
        // Here we would eventually check against a Database or API
        return _studentRegNumber.value.isNotEmpty()
    }
}