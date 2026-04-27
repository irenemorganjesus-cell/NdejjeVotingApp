package com.ndejje.votingapp.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    // Runs on IO Dispatcher to keep the UI smooth
    suspend fun register(user: UserEntity): Long {
        return withContext(Dispatchers.IO) {
            userDao.registerUser(user)
        }
    }

    suspend fun login(regNo: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.loginUser(regNo)
        }
    }
}