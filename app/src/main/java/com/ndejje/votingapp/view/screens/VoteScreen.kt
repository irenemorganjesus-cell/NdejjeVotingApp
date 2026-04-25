package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ndejje.votingapp.R
import com.ndejje.votingapp.model.Candidate

@Composable
fun VoteScreen(candidates: List<Candidate>) {
    Scaffold(
        topBar = {
            Text(text = "Ndejje University Elections", style = MaterialTheme.typography.titleLarge)
        }
    ) { padding ->
        //Dynamic list
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_standard))
        ) {
            items(candidates) { candidate ->
                CandidateItem(candidate = candidate)
            }
        }
    }
}

@Composable
fun CandidateItem(candidate: Candidate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.spacer_height))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_standard))) {
            Text(text = candidate.fullName, style = MaterialTheme.typography.headlineSmall)
            Text(text = candidate.position, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { }) {
                Text("Vote Now")
            }
        }
    }
}