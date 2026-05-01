package com.ndejje.votingapp.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CandidateRepository(private val candidateDao: CandidateDao) {

    suspend fun getCandidatesByPosition(position: String): List<CandidateEntity> {
        return withContext(Dispatchers.IO) {
            candidateDao.getCandidatesByPosition(position)
        }
    }

    suspend fun castVote(candidateId: Int) {
        withContext(Dispatchers.IO) {
            candidateDao.incrementVote(candidateId)
        }
    }

    suspend fun getTotalVotes(): Int {
        return withContext(Dispatchers.IO) {
            candidateDao.getTotalVotes() ?: 0
        }
    }

    // This now correctly calls 'insertCandidates' in the DAO
    suspend fun insertInitialCandidates(candidates: List<CandidateEntity>) {
        withContext(Dispatchers.IO) {
            candidateDao.insertCandidates(candidates)
        }
    }
}