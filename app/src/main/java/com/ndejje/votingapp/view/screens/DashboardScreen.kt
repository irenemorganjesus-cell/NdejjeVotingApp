package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue
import com.ndejje.votingapp.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: CandidateViewModel) {
    val candidates by viewModel.candidates.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ndejje Voting Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NdejjeDarkBlue)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                text = "Guild President Candidates",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NdejjeDarkBlue
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(candidates) { candidate ->
                    CandidateCard(candidate) {
                        viewModel.castVote(candidate.id)
                    }
                }
            }
        }
    }
}

@Composable
fun CandidateCard(candidate: CandidateEntity, onVote: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Image
            Surface(
                modifier = Modifier.size(60.dp),
                shape = MaterialTheme.shapes.medium,
                color = Color.LightGray
            ) {}

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = candidate.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = candidate.partyName, color = Color.Gray, fontSize = 14.sp)
                Text(text = "Votes: ${candidate.voteCount}", fontWeight = FontWeight.Medium, color = Color(0xFF2E7D32))
            }

            Button(
                onClick = onVote,
                colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue)
            ) {
                Text("Vote")
            }
        }
    }
}