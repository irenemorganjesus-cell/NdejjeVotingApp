package com.ndejje.votingapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CandidateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidates(candidates: List<CandidateEntity>) // This must match!

    @Query("SELECT * FROM candidates WHERE position = :pos")
    suspend fun getCandidatesByPosition(pos: String): List<CandidateEntity>

    @Query("UPDATE candidates SET voteCount = voteCount + 1 WHERE id = :candidateId")
    suspend fun incrementVote(candidateId: Int)

    @Query("SELECT SUM(voteCount) FROM candidates")
    suspend fun getTotalVotes(): Int?
}