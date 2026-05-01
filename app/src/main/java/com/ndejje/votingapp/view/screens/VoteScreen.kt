package com.ndejje.votingapp.view.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(navController: NavController, viewModel: CandidateViewModel, user: UserEntity?) {
    val candidates by viewModel.candidates.collectAsState()
    var selectedPosition by remember { mutableStateOf("Guild President") }
    
    // Track selections for all three positions
    var selectedCandidates by remember { mutableStateOf(mapOf<String, Int?>()) }
    
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val ndejjeDarkBlue = Color(0xFF001F3F)

    val positions = listOf("Guild President", "Guild Speaker", "GRC")

    // Check if user has already voted
    LaunchedEffect(user) {
        if (user?.hasVoted == true) {
            Toast.makeText(context, "You have already voted!", Toast.LENGTH_LONG).show()
            navController.navigate("home") {
                popUpTo("vote") { inclusive = true }
            }
        }
    }

    if (user?.hasVoted == true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // --- CONFIRMATION DIALOG LOGIC ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Your Votes", fontWeight = FontWeight.Bold) },
            text = { 
                Column {
                    Text("Are you sure you want to submit your choices for all positions?")
                    Spacer(modifier = Modifier.height(8.dp))
                    positions.forEach { pos ->
                        val candId = selectedCandidates[pos]
                        // We could fetch names here, but for simplicity in the dialog:
                        Text("• $pos selected", fontWeight = FontWeight.Medium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val ids = selectedCandidates.values.filterNotNull()
                    viewModel.confirmVotes(ids, user?.registrationNumber ?: "") {
                        showDialog = false
                        navController.navigate("vote_success")
                    }
                }) {
                    Text("SUBMIT ALL", color = ndejjeDarkBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CANCEL", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Cast Your Vote", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text("Complete all sections to submit", fontSize = 14.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(28.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {

            // 1. Position Navigation Tabs
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                positions.forEach { pos ->
                    val isSelected = selectedPosition == pos
                    val isCompleted = selectedCandidates[pos] != null
                    
                    Button(
                        onClick = {
                            selectedPosition = pos
                            viewModel.fetchCandidates(pos)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) ndejjeDarkBlue else if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFF1F1F1),
                            contentColor = if (isSelected) Color.White else if (isCompleted) Color(0xFF2E7D32) else Color.Black
                        ),
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = if (isCompleted && !isSelected) BorderStroke(1.dp, Color(0xFF2E7D32)) else null
                    ) {
                        Text(pos, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            }

            // 2. Instructions Divider
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                Text(
                    "Voting for: $selectedPosition",
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = 12.dp),
                    fontSize = 15.sp,
                    color = ndejjeDarkBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            // 3. Candidate Selection List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(candidates) { candidate ->
                    CandidateVoteCard(
                        candidate = candidate,
                        isSelected = selectedCandidates[selectedPosition] == candidate.id,
                        ndejjeDarkBlue = ndejjeDarkBlue,
                        onSelect = {
                            selectedCandidates = selectedCandidates.toMutableMap().apply {
                                put(selectedPosition, candidate.id)
                            }
                        }
                    )
                }
            }

            // 4. Submit Button
            val allSelected = positions.all { selectedCandidates[it] != null }
            
            Button(
                onClick = { showDialog = true },
                enabled = allSelected,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ndejjeDarkBlue,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(
                    if (allSelected) "Submit All Votes" else "Complete Selections (${selectedCandidates.size}/3)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Text(
                "Selections: ${selectedCandidates.size} of 3 sections completed",
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = if (allSelected) Color(0xFF2E7D32) else Color.Gray,
                fontWeight = if (allSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun CandidateVoteCard(candidate: CandidateEntity, isSelected: Boolean, ndejjeDarkBlue: Color, onSelect: () -> Unit) {
    val context = LocalContext.current

    val imageResId = remember(candidate.imageUrl) {
        val resourceId = context.resources.getIdentifier(
            candidate.imageUrl.lowercase().trim(),
            "drawable",
            context.packageName
        )
        if (resourceId != 0) resourceId else R.drawable.male_candidate
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        border = if (isSelected) BorderStroke(3.dp, ndejjeDarkBlue) else null
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "${candidate.name}'s Profile",
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(candidate.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(candidate.course, fontSize = 16.sp, color = Color.Gray)
                Text("\"${candidate.motto}\"", fontSize = 14.sp, fontStyle = FontStyle.Italic, color = Color.Black)
            }

            Box(
                modifier = Modifier.size(30.dp).clip(CircleShape)
                    .border(2.dp, if (isSelected) ndejjeDarkBlue else Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(ndejjeDarkBlue))
                }
            }
        }
    }
}
