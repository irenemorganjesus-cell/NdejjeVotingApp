package com.ndejje.votingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class CandidateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val position: String, // e.g., "Guild President", "GRC - IT"
    val partyName: String,
    val manifesto: String,
    val imageUrl: String, // Path to their profile picture
    val voteCount: Int = 0
)