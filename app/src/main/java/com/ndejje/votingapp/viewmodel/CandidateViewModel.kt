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

    // State to track the currently selected position to ensure refreshes are accurate
    private var currentPosition: String = "Guild President"

    fun fetchCandidates(position: String = "Guild President") {
        currentPosition = position
        viewModelScope.launch {
            _candidates.value = repository.getCandidatesByPosition(position)
        }
    }

    /**
     * Confirms the vote, saves it to the DB, and refreshes the UI.
     * @param candidateId The ID of the selected candidate.
     * @param onComplete Callback to close the dialog or navigate in the UI.
     */
    fun confirmVote(candidateId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1. Increment the vote in the database
            repository.castVote(candidateId)

            // 2. Refresh the current list to reflect the new vote count internally
            fetchCandidates(currentPosition)

            // 3. Signal to the UI that the operation is finished
            onComplete()
        }
    }
}

// 2. THE FACTORY
class CandidateViewModelFactory(private val repository: CandidateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}