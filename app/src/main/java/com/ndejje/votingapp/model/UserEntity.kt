package com.ndejje.votingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val registrationNumber: String,
    val fullName: String,
    val course: String,
    val password: String,
    val yearOfStudy: String = "1",
    val campus: String = "Kampala Campus",
    val email: String = "",
    val phoneNumber: String = "",
    val profilePictureUri: String? = null,
    val hasVoted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
