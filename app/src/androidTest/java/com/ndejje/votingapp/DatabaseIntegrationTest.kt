package com.ndejje.votingapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ndejje.votingapp.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var candidateDao: CandidateDao
    private lateinit var notificationDao: NotificationDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Using an in-memory database because the information saved here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        candidateDao = db.candidateDao()
        notificationDao = db.notificationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val user = UserEntity(
            registrationNumber = "25/2/303/D/001",
            fullName = "Ssenono Francis",
            password = "password123",
            email = "ssenono@student.ndejje.ac.ug",
            course = "B.IT",
            yearOfStudy = "3",
            campus = "Main Campus"
        )
        userDao.registerUser(user)
        val byName = userDao.loginUser("25/2/303/D/001")
        assertEquals(byName?.fullName, user.fullName)
    }

    @Test
    @Throws(Exception::class)
    fun testAtomicVoting() = runBlocking {
        val candidate = CandidateEntity(
            id = 1,
            name = "Kato Brian",
            position = "Guild President",
            partyName = "PSF",
            course = "BSc. CS",
            motto = "Unity",
            imageUrl = "url",
            voteCount = 0
        )
        candidateDao.insertCandidates(listOf(candidate))
        
        // Cast 3 votes
        candidateDao.incrementVote(1)
        candidateDao.incrementVote(1)
        candidateDao.incrementVote(1)
        
        val updatedCandidate = candidateDao.getCandidatesByPosition("Guild President").first()
        assertEquals(3, updatedCandidate.voteCount)
        
        val totalVotes = candidateDao.getTotalVotes()
        assertEquals(3, totalVotes)
    }

    @Test
    @Throws(Exception::class)
    fun testNotifications() = runBlocking {
        val notification = NotificationEntity(
            title = "Test",
            message = "Test Message",
            type = "INFO"
        )
        notificationDao.insertNotification(notification)
        
        val unreadCount = notificationDao.getUnreadCount().first()
        assertEquals(1, unreadCount)
        
        notificationDao.markAllAsRead()
        val unreadCountAfter = notificationDao.getUnreadCount().first()
        assertEquals(0, unreadCountAfter)
    }
}
