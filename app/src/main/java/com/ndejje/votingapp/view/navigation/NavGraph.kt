package com.ndejje.votingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndejje.votingapp.model.*
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
        factory = CandidateViewModelFactory(candidateRepository, userRepository)
    )

    val notificationRepository = NotificationRepository(database.notificationDao())
    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(notificationRepository)
    )

    // Sync AuthViewModel user state after voting
    val userByAuth by authViewModel.currentUser.collectAsState()
    LaunchedEffect(candidateViewModel.lastVoteTime.collectAsState().value) {
        userByAuth?.registrationNumber?.let {
            authViewModel.refreshUser(it)
        }
    }

    // 3. SEED DATA LOGIC
    LaunchedEffect(Unit) {
        val sampleCandidates = listOf(
            // Guild President
            CandidateEntity(id = 1, name = "Kato Brian", position = "Guild President", course = "BSc. Computer Science", motto = "Leadership for Unity", imageUrl = "kato_brian", partyName = "PSF"),
            CandidateEntity(id = 2, name = "Namirembe Faith", position = "Guild President", course = "BBA", motto = "Your Voice, Our Responsibility", imageUrl = "namirembe_faith", partyName = "UA"),
            CandidateEntity(id = 3, name = "Mugisha Ivan", position = "Guild President", course = "BEd. Science", motto = "Together We Build", imageUrl = "mugisha_ivan", partyName = "IND"),

            // Guild Speaker
            CandidateEntity(id = 4, name = "Aksam Kalungi", position = "Guild Speaker", course = "BIT", motto = "Order and Progress", imageUrl = "aksam_kalungi", partyName = "PSF"),
            CandidateEntity(id = 5, name = "Irene Namanda", position = "Guild Speaker", course = "BSc. Engineering", motto = "The Voice of Reason", imageUrl = "irene_namanda", partyName = "UA"),
            CandidateEntity(id = 6, name = "Muhammad Muyanja", position = "Guild Speaker", course = "B. Law", motto = "Justice for All", imageUrl = "muhammad_muyanja", partyName = "IND"),

            // GRC
            CandidateEntity(id = 7, name = "Sharifah Namaganda", position = "GRC", course = "BIT", motto = "Innovation in Representation", imageUrl = "sharifah_namaganda", partyName = "PSF"),
            CandidateEntity(id = 8, name = "Ssenono Francis Xavier", position = "GRC", course = "BIT", motto = "Lead with Action", imageUrl = "xavier_ssenono", partyName = "UA"),
            CandidateEntity(id = 9, name = "Nakalyango Florence", position = "GRC", course = "B. Nursing", motto = "Service Above Self", imageUrl = "ndejje_student", partyName = "IND")
        )
        candidateRepository.insertInitialCandidates(sampleCandidates)

        // Seed initial notification
        notificationViewModel.addNotification(
            title = "Election Started",
            message = "The 2026 Student Leadership election is now live. Cast your vote!",
            type = "ALERT"
        )
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
            val user by authViewModel.currentUser.collectAsState()
            HomeScreen(
                navController = navController,
                userName = user?.fullName ?: "Grace",
                candidateViewModel = candidateViewModel,
                notificationViewModel = notificationViewModel
            )
        }
        composable("vote") {
            val user by authViewModel.currentUser.collectAsState()
            // Fetch "Guild President" candidates by default when entering the screen
            LaunchedEffect(Unit) {
                candidateViewModel.fetchCandidates("Guild President")
            }
            VoteScreen(navController, candidateViewModel, notificationViewModel, user)
        }
        composable("notifications") {
            NotificationScreen(navController, notificationViewModel)
        }
        composable("results") {
            ResultsScreen(navController, candidateViewModel)
        }
        composable("profile") {
            val user by authViewModel.currentUser.collectAsState()
            ProfileScreen(navController, user)
        }
        composable("edit_profile") {
            val user by authViewModel.currentUser.collectAsState()
            EditProfileScreen(navController, authViewModel, user)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController, authViewModel)
        }
        composable("vote_success") {
            VoteSuccessScreen(navController)
        }
    }
}
