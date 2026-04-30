package com.ndejje.votingapp.navigation

import androidx.compose.material3.Text
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
import com.ndejje.votingapp.viewmodel.AuthViewModel
import com.ndejje.votingapp.viewmodel.AuthViewModelFactory

@Composable
fun VotingNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Initialize Database components
    val database = AppDatabase.getDatabase(context)
    val userRepository = UserRepository(database.userDao())
    val candidateRepository = CandidateRepository(database.candidateDao())

    // AuthViewModel uses the AuthViewModelFactory for dependency injection
    val factory = AuthViewModelFactory(userRepository)
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    // 2. SEED DATA LOGIC
    // This side-effect ensures our "Database Table" isn't empty
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
        // Insert initial data. Room ignores duplicates based on our DAO config.
        candidateRepository.insertInitialCandidates(sampleCandidates)
    }

    // 3. Navigation Routes
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
            // Placeholder for the Dashboard we will build next
            Text("Welcome to the Voting Dashboard! Candidates are now in the DB.")
        }
    }
}