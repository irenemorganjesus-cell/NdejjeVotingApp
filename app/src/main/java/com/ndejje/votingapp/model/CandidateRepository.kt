package com.ndejje.votingapp.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CandidateRepository(private val candidateDao: CandidateDao) {

    fun getCandidatesByPositionFlow(position: String): Flow<List<CandidateEntity>> {
        return candidateDao.getCandidatesByPositionFlow(position)
    }

    fun getTotalVotesFlow(): Flow<Int?> {
        return candidateDao.getTotalVotesFlow()
    }

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