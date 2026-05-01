package com.ndejje.votingapp.viewmodel

import app.cash.turbine.test
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.CandidateRepository
import com.ndejje.votingapp.model.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CandidateViewModelTest {

    private lateinit var viewModel: CandidateViewModel
    private val candidateRepository: CandidateRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock default flows to avoid NPE on initialization
        every { candidateRepository.getCandidatesByPositionFlow(any()) } returns flowOf(emptyList())
        every { candidateRepository.getTotalVotesFlow() } returns flowOf(0)
        
        viewModel = CandidateViewModel(candidateRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCandidates updates candidates flow with data from repository`() = runTest {
        val candidates = listOf(
            CandidateEntity(1, "John Doe", "Guild President", "DP", "B.IT", "Together We Build", "uri", 10),
            CandidateEntity(2, "Jane Smith", "Guild President", "NRM", "B.Law", "Service Above Self", "uri", 5)
        )
        
        every { candidateRepository.getCandidatesByPositionFlow("Guild President") } returns flowOf(candidates)

        viewModel.fetchCandidates("Guild President")
        
        viewModel.candidates.test {
            assertEquals(candidates, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirmVotes casts votes and marks user as voted`() = runTest {
        val candidateIds = listOf(1, 2)
        val regNo = "25/2/303/D/001"
        
        coEvery { candidateRepository.castVote(any()) } returns Unit
        coEvery { userRepository.markUserAsVoted(any()) } returns Unit

        var completed = false
        viewModel.confirmVotes(candidateIds, regNo) {
            completed = true
        }

        // Advance dispatcher to execute the coroutine
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { candidateRepository.castVote(1) }
        coVerify(exactly = 1) { candidateRepository.castVote(2) }
        coVerify(exactly = 1) { userRepository.markUserAsVoted(regNo) }
        assert(completed)
    }
}
