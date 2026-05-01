package com.ndejje.votingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ndejje.votingapp.view.navigation.VotingNavGraph
import com.ndejje.votingapp.ui.theme.NdejjeVotingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NdejjeVotingAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    VotingNavGraph()
                }
            }
        }
    }
}
