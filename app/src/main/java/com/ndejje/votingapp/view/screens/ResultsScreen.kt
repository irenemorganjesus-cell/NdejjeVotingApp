package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue
import com.ndejje.votingapp.view.components.BottomNavigationBar
import com.ndejje.votingapp.viewmodel.CandidateViewModel

data class CandidateResult(val name: String, val votes: Int, val percentage: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, candidateViewModel: CandidateViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val positions = listOf("Guild President", "Guild Speaker", "GRC")
    var selectedPosition by remember { mutableStateOf(positions[0]) }

    val candidates by candidateViewModel.candidates.collectAsState()

    // Fetch candidates whenever position changes
    LaunchedEffect(selectedPosition) {
        candidateViewModel.fetchCandidates(selectedPosition)
    }

    val totalVotesForPosition = candidates.sumOf { it.voteCount }
    val currentResults = candidates.map { candidate ->
        val percentage = if (totalVotesForPosition > 0) {
            (candidate.voteCount.toFloat() / totalVotesForPosition) * 100
        } else 0f
        CandidateResult(candidate.name, candidate.voteCount, percentage)
    }

    Scaffold(
        containerColor = NdejjeDarkBlue,
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "Election Results",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Live Updates",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selection and Content Card
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    // Position Dropdown
                    Text(
                        text = "Select Position",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPosition,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedIndicatorColor = NdejjeDarkBlue,
                                unfocusedIndicatorColor = Color.LightGray
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            positions.forEach { position ->
                                DropdownMenuItem(
                                    text = { Text(position) },
                                    onClick = {
                                        selectedPosition = position
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Candidates Results
                    if (currentResults.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No candidates found for this position", color = Color.Gray)
                        }
                    } else {
                        currentResults.forEach { candidate ->
                            CandidateResultItem(candidate)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)
                    
                    Text(
                        text = "Total Votes: $totalVotesForPosition",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NdejjeDarkBlue
                    )
                    
                    val lastVoteTime by candidateViewModel.lastVoteTime.collectAsState()
                    
                    Text(
                        text = "Last updated: $lastVoteTime",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CandidateResultItem(candidate: CandidateResult) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = candidate.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = "${candidate.votes} votes",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(16.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(candidate.percentage / 100f)
                        .fillMaxHeight()
                        .background(NdejjeDarkBlue, RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${candidate.percentage.toInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = NdejjeDarkBlue
            )
        }
    }
}
