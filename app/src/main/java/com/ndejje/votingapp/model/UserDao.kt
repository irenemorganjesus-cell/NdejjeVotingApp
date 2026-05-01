package com.ndejje.votingapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(user: UserEntity): Long

    // Find a user by registration number for Login
    @Query("SELECT * FROM users WHERE registrationNumber = :regNo LIMIT 1")
    suspend fun loginUser(regNo: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE users SET hasVoted = 1 WHERE registrationNumber = :regNo")
    suspend fun markUserAsVoted(regNo: String)

    @Update
    suspend fun updateUser(user: UserEntity)
}
