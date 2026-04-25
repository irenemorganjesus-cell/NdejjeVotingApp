package com.ndejje.votingapp.view.navigation

import androidx.compose.runtime.Composable
import com.ndejje.votingapp.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ndejje.votingapp.model.Candidate
import com.ndejje.votingapp.view.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    //dummy list of candidates
    val dummyCandidates = listOf(
        Candidate(
            1,
            "Kintu Fredrick",
            "Guild President",
            "Empowering Students",
            R.drawable.male_candidate
        ),
        Candidate(
            2,
            "Nakalyango Florence",
            "Vice President",
            "Quality Education for All",
            R.drawable.female_candidate)
    )
    NavHost(
        navController = navController,
        startDestination = "splash" // Start at the Splash Screen
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("vote") {
            VoteScreen(candidates = dummyCandidates)
         }
    }
}