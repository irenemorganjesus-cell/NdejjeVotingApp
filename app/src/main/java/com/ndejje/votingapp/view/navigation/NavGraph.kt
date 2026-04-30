package com.ndejje.votingapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndejje.votingapp.model.AppDatabase
import com.ndejje.votingapp.model.UserRepository
import com.ndejje.votingapp.view.screens.*
import com.ndejje.votingapp.viewmodel.AuthViewModel
import com.ndejje.votingapp.viewmodel.AuthViewModelFactory

@Composable
fun VotingNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Initialize Database components
    val database = AppDatabase.getDatabase(context)
    val repository = UserRepository(database.userDao())
    val factory = AuthViewModelFactory(repository)

    // Create the AuthViewModel (Shared across Login and Register)
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "splash" // Start with your Splash Screen
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
            // This will be the voting dashboard later!
            Text("Welcome to the Voting Dashboard!")
        }
    }
}

