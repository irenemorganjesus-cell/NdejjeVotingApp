package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.CandidateRepository
import com.ndejje.votingapp.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CandidateViewModel(
    private val candidateRepository: CandidateRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<CandidateEntity>>(emptyList())
    val candidates: StateFlow<List<CandidateEntity>> = _candidates

    private var currentPosition: String = "Guild President"

    fun fetchCandidates(position: String = "Guild President") {
        currentPosition = position
        viewModelScope.launch {
            _candidates.value = candidateRepository.getCandidatesByPosition(position)
        }
    }

    fun confirmVote(candidateId: Int, regNo: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1. Increment the vote in the database
            candidateRepository.castVote(candidateId)
            
            // 2. Mark user as voted
            userRepository.markUserAsVoted(regNo)

            // 3. Refresh the current list
            fetchCandidates(currentPosition)

            // 4. Signal to the UI that the operation is finished
            onComplete()
        }
    }
}

class CandidateViewModelFactory(
    private val candidateRepository: CandidateRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidateViewModel(candidateRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
