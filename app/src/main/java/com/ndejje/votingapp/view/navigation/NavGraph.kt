package com.ndejje.votingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndejje.votingapp.model.AppDatabase
import com.ndejje.votingapp.model.UserRepository
import com.ndejje.votingapp.model.CandidateRepository
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.view.screens.*
import com.ndejje.votingapp.viewmodel.*

@Composable
fun VotingNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Initialize Database & Repositories
    val database = AppDatabase.getDatabase(context)
    val userRepository = UserRepository(database.userDao())
    val candidateRepository = CandidateRepository(database.candidateDao())

    // 2. Initialize ViewModels using their respective Factories
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )

    val candidateViewModel: CandidateViewModel = viewModel(
        factory = CandidateViewModelFactory(candidateRepository)
    )

    // 3. SEED DATA LOGIC
    // This populates the DB with the candidates from the "vote.png" design
    LaunchedEffect(Unit) {
        // Inside NavGraph.kt -> LaunchedEffect(Unit)
        val sampleCandidates = listOf(
            // Guild President Section
            CandidateEntity(name = "Kato Brian", position = "Guild President", course = "BSc. Computer Science", motto = "Leadership for Unity", imageUrl = "", partyName = "PSF"),
            CandidateEntity(name = "Namirembe Faith", position = "Guild President", course = "BBA", motto = "Your Voice, Our Responsibility", imageUrl = "", partyName = "UA"),
            CandidateEntity(name = "Mugisha Ivan", position = "Guild President", course = "BEd. Science", motto = "Together We Build", imageUrl = "", partyName = "IND"),

            // Guild Speaker Section
            CandidateEntity(name = "Orombi Allan", position = "Guild Speaker", course = "BIT", motto = "Order and Progress", imageUrl = "", partyName = "PSF"),
            CandidateEntity(name = "Nakku Annet", position = "Guild Speaker", course = "BSc. Engineering", motto = "The Voice of Reason", imageUrl = "", partyName = "UA"),
            CandidateEntity(name = "Zzimula Ambrose", position = "Guild Speaker", course = "B. Law", motto = "Justice for All", imageUrl = "", partyName = "IND"),

            // GRC Section
            CandidateEntity(name = "Nalugwa Annet", position = "GRC", course = "BIT", motto = "Innovation in Representation", imageUrl = "", partyName = "PSF"),
            CandidateEntity(name = "Namuyimba Mathias", position = "GRC", course = "BIT", motto = "Lead with Action", imageUrl = "", partyName = "UA"),
            CandidateEntity(name = "Nakalyango Florence", position = "GRC", course = "B. Nursing", motto = "Service Above Self", imageUrl = "", partyName = "IND")
        )
        candidateRepository.insertInitialCandidates(sampleCandidates)
    }

    // 4. Navigation Routes
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("register") {
            RegisterScreen(navController, authViewModel)
        }
        composable("home") {
            // Fetch "Guild President" candidates by default when entering the screen
            LaunchedEffect(Unit) {
                candidateViewModel.fetchCandidates("Guild President")
            }
            VoteScreen(navController, candidateViewModel)
        }
    }
}