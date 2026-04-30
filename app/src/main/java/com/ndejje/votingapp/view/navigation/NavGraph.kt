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
    // Runs once on app start to ensure candidates exist in the local Room DB
    LaunchedEffect(Unit) {
        val sampleCandidates = listOf(
            CandidateEntity(
                name = "Ssenono John",
                position = "Guild President",
                partyName = "Progressive Students Front",
                manifesto = "Better WiFi for all hostels!",
                imageUrl = ""
            ),
            CandidateEntity(
                name = "Nakamya Sharifah",
                position = "Guild President",
                partyName = "Unity Alliance",
                manifesto = "Transparent accountability and modern labs.",
                imageUrl = ""
            )
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
            // Trigger data fetch when the student enters the dashboard
            LaunchedEffect(Unit) {
                candidateViewModel.fetchCandidates()
            }
            DashboardScreen(navController, candidateViewModel)
        }
    }
}