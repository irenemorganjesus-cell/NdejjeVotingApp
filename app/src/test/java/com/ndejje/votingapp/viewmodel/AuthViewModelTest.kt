package com.ndejje.votingapp.viewmodel

import app.cash.turbine.test
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.model.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val repository: UserRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loginUser with valid credentials updates state to Success`() = runTest {
        val testUser = UserEntity(
            registrationNumber = "25/2/303/D/001",
            fullName = "Test User",
            password = "password123",
            email = "test@student.ndejje.ac.ug",
            course = "B.IT",
            yearOfStudy = "3",
            campus = "Main Campus"
        )

        coEvery { repository.login("25/2/303/D/001") } returns testUser

        viewModel.loginUser("25/2/303/D/001", "password123")
        
        // Use Turbine to test StateFlow
        viewModel.authState.test {
            // First item is Idle (initial state)
            assertEquals(AuthResult.Idle, awaitItem())
            // Second item should be Success after login (skipping Loading if it happens too fast or using advanceUntilIdle)
            val result = awaitItem()
            assert(result is AuthResult.Success)
            assertEquals("Welcome, Test User", (result as AuthResult.Success).message)
            cancelAndIgnoreRemainingEvents()
        }
        
        assertEquals(testUser, viewModel.currentUser.value)
    }

    @Test
    fun `loginUser with invalid credentials updates state to Error`() = runTest {
        coEvery { repository.login("wrong_reg") } returns null

        viewModel.loginUser("wrong_reg", "any")

        viewModel.authState.test {
            assertEquals(AuthResult.Idle, awaitItem())
            val result = awaitItem()
            assert(result is AuthResult.Error)
            assertEquals("Invalid credentials!", (result as AuthResult.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registerUser with new user updates state to Success`() = runTest {
        val newUser = UserEntity(
            registrationNumber = "25/2/303/D/002",
            fullName = "New User",
            password = "password123",
            email = "new@student.ndejje.ac.ug",
            course = "B.Law",
            yearOfStudy = "1",
            campus = "Kampala Campus"
        )

        coEvery { repository.register(any()) } returns 1L

        viewModel.registerUser(newUser)

        viewModel.authState.test {
            assertEquals(AuthResult.Idle, awaitItem())
            val result = awaitItem()
            assert(result is AuthResult.Success)
            assertEquals("Registration Successful!", (result as AuthResult.Success).message)
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(newUser, viewModel.currentUser.value)
    }
}
