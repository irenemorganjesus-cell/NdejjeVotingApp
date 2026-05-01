package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.CandidateRepository
import com.ndejje.votingapp.model.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CandidateViewModel(
    private val candidateRepository: CandidateRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentPosition = MutableStateFlow("Guild President")
    
    // Live candidates list that updates automatically when DB changes
    val candidates: StateFlow<List<CandidateEntity>> = _currentPosition
        .flatMapLatest { position ->
            candidateRepository.getCandidatesByPositionFlow(position)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live total votes count
    val totalVotes: StateFlow<Int> = candidateRepository.getTotalVotesFlow()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _lastVoteTime = MutableStateFlow<String>("Never")
    val lastVoteTime: StateFlow<String> = _lastVoteTime

    fun fetchCandidates(position: String) {
        _currentPosition.value = position
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
