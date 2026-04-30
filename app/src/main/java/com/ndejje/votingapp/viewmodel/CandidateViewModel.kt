package com.ndejje.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.CandidateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. THE VIEWMODEL
class CandidateViewModel(private val repository: CandidateRepository) : ViewModel() {

    private val _candidates = MutableStateFlow<List<CandidateEntity>>(emptyList())
    val candidates: StateFlow<List<CandidateEntity>> = _candidates

    fun fetchCandidates(position: String = "Guild President") {
        viewModelScope.launch {
            _candidates.value = repository.getCandidatesByPosition(position)
        }
    }

    fun castVote(candidateId: Int) {
        viewModelScope.launch {
            repository.voteForCandidate(candidateId)
            fetchCandidates() // Refresh list to show new vote count
        }
    }
}

// 2. THE FACTORY (Put it right here!)
class CandidateViewModelFactory(private val repository: CandidateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}