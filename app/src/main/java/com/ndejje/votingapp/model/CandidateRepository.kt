package com.ndejje.votingapp.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CandidateRepository(private val candidateDao: CandidateDao) {

    // Fetch candidates based on their position (e.g., Guild President)
    suspend fun getCandidatesByPosition(position: String): List<CandidateEntity> {
        return withContext(Dispatchers.IO) {
            candidateDao.getCandidatesByPosition(position)
        }
    }

    // Records a vote for a specific candidate
    suspend fun voteForCandidate(candidateId: Int) {
        withContext(Dispatchers.IO) {
            candidateDao.incrementVote(candidateId)
        }
    }

    // This is useful for first-time setup (Seeding the database)
    suspend fun insertInitialCandidates(candidates: List<CandidateEntity>) {
        withContext(Dispatchers.IO) {
            candidateDao.insertCandidates(candidates)
        }
    }
}