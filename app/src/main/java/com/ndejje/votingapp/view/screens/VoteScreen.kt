package com.ndejje.votingapp.view.screens

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
import com.ndejje.votingapp.viewmodel.CandidateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(navController: NavController, viewModel: CandidateViewModel) {
    val candidates by viewModel.candidates.collectAsState()
    var selectedPosition by remember { mutableStateOf("Guild President") }
    var selectedCandidateId by remember { mutableStateOf<Int?>(null) }
    val ndejjeDarkBlue = Color(0xFF001F3F)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Cast Your Vote", fontWeight = FontWeight.Bold, fontSize = 24.sp) // Increased
                        Text("Select your preferred in elections", fontSize = 14.sp, color = Color.Gray) // Increased
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("login") { popUpTo(0) } }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Logout", modifier = Modifier.size(28.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Guild President", "Guild Speaker", "GRC").forEach { pos ->
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
                        modifier = Modifier.weight(1f).height(48.dp), // Taller buttons
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(pos, fontSize = 12.sp, fontWeight = FontWeight.Bold) // Increased & Bold
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                Text(
                    "Select ONE candidate",
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = 12.dp),
                    fontSize = 15.sp, // Increased
                    color = Color.DarkGray
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(candidates) { candidate ->
                    CandidateVoteCard(
                        candidate = candidate,
                        isSelected = selectedCandidateId == candidate.id,
                        ndejjeDarkBlue = ndejjeDarkBlue,
                        onSelect = {
                            // UNCHECK LOGIC: If already selected, set to null. Otherwise, select it.
                            selectedCandidateId = if (selectedCandidateId == candidate.id) null else candidate.id
                        }
                    )
                }
            }

            Button(
                onClick = { /* Submit */ },
                enabled = selectedCandidateId != null,
                modifier = Modifier.fillMaxWidth().height(60.dp), // Taller Submit Button
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ndejjeDarkBlue, disabledContainerColor = Color.LightGray)
            ) {
                Text("Submit Vote", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Increased
            }

            Text(
                "You can only vote once per position",
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                fontSize = 13.sp, // Increased
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CandidateVoteCard(candidate: CandidateEntity, isSelected: Boolean, ndejjeDarkBlue: Color, onSelect: () -> Unit) {
    val context = LocalContext.current
    
    // Resolve the image resource ID. 
    // It looks for a drawable matching candidate.imageUrl (e.g., "kato_brian")
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

            // --- UPDATED IMAGE BOX ---
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "${candidate.name}'s Profile",
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop // Ensures the face fills the box nicely
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