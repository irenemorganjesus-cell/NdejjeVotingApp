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

    private val _totalVotes = MutableStateFlow(0)
    val totalVotes: StateFlow<Int> = _totalVotes

    private val _lastVoteTime = MutableStateFlow<String>("Never")
    val lastVoteTime: StateFlow<String> = _lastVoteTime

    private var currentPosition: String = "Guild President"

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _totalVotes.value = candidateRepository.getTotalVotes()
        }
    }

    fun fetchCandidates(position: String = "Guild President") {
        currentPosition = position
        viewModelScope.launch {
            _candidates.value = candidateRepository.getCandidatesByPosition(position)
            refreshStats()
        }
    }

    fun confirmVote(candidateId: Int, regNo: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1. Increment the vote in the database
            candidateRepository.castVote(candidateId)
            
            // 2. Mark user as voted
            userRepository.markUserAsVoted(regNo)

            // 3. Update the last vote timestamp
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy, hh:mm a", java.util.Locale.getDefault())
            _lastVoteTime.value = sdf.format(java.util.Date())

            // 4. Refresh data
            fetchCandidates(currentPosition)
            refreshStats()

            // 5. Signal completion
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
