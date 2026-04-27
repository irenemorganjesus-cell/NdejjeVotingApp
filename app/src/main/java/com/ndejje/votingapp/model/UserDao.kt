package com.ndejje.votingapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(user: UserEntity): Long

    // Find a user by registration number for Login
    @Query("SELECT * FROM users WHERE registrationNumber = :regNo LIMIT 1")
    suspend fun loginUser(regNo: String): UserEntity?
}