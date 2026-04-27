package com.ndejje.votingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val registrationNumber: String, // e.g. 23/2/314/D/001
    val fullName: String,
    val course: String,
    val password: String,
    val hasVoted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)