package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(navController: NavController, viewModel: CandidateViewModel) {
    val candidates by viewModel.candidates.collectAsState()
    var selectedPosition by remember { mutableStateOf("Guild President") }
    var selectedCandidateId by remember { mutableStateOf<Int?>(null) }

    // Updated brand color: Ndejje Dark Blue
    val ndejjeDarkBlue = Color(0xFF001F3F)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Cast Your Vote", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Select your preferred in elections", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Logout logic: Clears the backstack so the user cannot back into the app
                        navController.navigate("login") { popUpTo(0) }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {

            // 1. Position Selection Tabs (Now in Dark Blue)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val positions = listOf("Guild President", "Guild Speaker", "GRC")
                positions.forEach { pos ->
                    val isSelected = selectedPosition == pos
                    Button(
                        onClick = {
                            selectedPosition = pos
                            selectedCandidateId = null
                            viewModel.fetchCandidates(pos)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) ndejjeDarkBlue else Color(0xFFF1F1F1),
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(pos, fontSize = 11.sp)
                    }
                }
            }

            // 2. Section Divider
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                Text(
                    "Select ONE candidate",
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = 8.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 3. Candidates List (Rounded Rectangles as per vote.png)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(candidates) { candidate ->
                    CandidateVoteCard(
                        candidate = candidate,
                        isSelected = selectedCandidateId == candidate.id,
                        ndejjeDarkBlue = ndejjeDarkBlue,
                        onSelect = { selectedCandidateId = candidate.id }
                    )
                }
            }

            // 4. Submit Button (Disabled until a vote is cast)
            Button(
                onClick = { /* Submit Logic */ },
                enabled = selectedCandidateId != null,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ndejjeDarkBlue,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Submit Vote", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Text(
                "You can only vote once per position",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CandidateVoteCard(
    candidate: CandidateEntity,
    isSelected: Boolean,
    ndejjeDarkBlue: Color,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = if (isSelected) BorderStroke(2.dp, ndejjeDarkBlue) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for image
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(candidate.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(candidate.course, fontSize = 13.sp, color = Color.Gray)
                Text(
                    text = "\"${candidate.motto}\"",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.DarkGray
                )
            }

            // Radio-style selection circle in Dark Blue
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(2.dp, if (isSelected) ndejjeDarkBlue else Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(ndejjeDarkBlue)
                    )
                }
            }
        }
    }
}