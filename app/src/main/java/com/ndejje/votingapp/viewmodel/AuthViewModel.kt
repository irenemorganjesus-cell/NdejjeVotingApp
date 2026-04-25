package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    // holds the "state" of the registration number globally
    private val _studentRegNumber = MutableStateFlow("")
    val studentRegNumber: StateFlow<String> = _studentRegNumber

    fun updateRegNumber(newNumber: String) {
        _studentRegNumber.value = newNumber
    }

    fun performLogin(): Boolean {
        //check against a Database
        return _studentRegNumber.value.isNotEmpty()
    }
}