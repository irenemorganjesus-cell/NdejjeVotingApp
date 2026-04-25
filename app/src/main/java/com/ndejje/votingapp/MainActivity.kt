package com.ndejje.votingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ndejje.votingapp.ui.theme.NdejjeVotingAppTheme
import com.ndejje.votingapp.view.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NdejjeVotingAppTheme {
                // Initialize the navigation driver
                val navController = rememberNavController()

                // Invoke the root Composable (NavGraph)
                NavGraph(navController = navController)
            }
        }
    }
}