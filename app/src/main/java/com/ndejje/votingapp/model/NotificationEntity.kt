package com.ndejje.votingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val type: String, // e.g., "ALERT", "SUCCESS", "INFO", "REMINDER"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
