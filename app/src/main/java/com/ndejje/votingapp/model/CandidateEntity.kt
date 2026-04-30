package com.ndejje.votingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class CandidateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val position: String,
    val partyName: String, // This can stay for internal records
    val course: String,    // New: e.g., "BSc. Computer Science"
    val motto: String,     // New: e.g., "Together We Build"
    val imageUrl: String,
    val voteCount: Int = 0
)