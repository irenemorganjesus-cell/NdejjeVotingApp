package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.view.components.BottomNavigationBar
import com.ndejje.votingapp.view.components.NdejjeQuickActionCard
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    candidateViewModel: com.ndejje.votingapp.viewmodel.CandidateViewModel,
    notificationViewModel: com.ndejje.votingapp.viewmodel.NotificationViewModel
) {
    val totalVotes by candidateViewModel.totalVotes.collectAsState()
    val lastVoteTime by candidateViewModel.lastVoteTime.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    // Countdown logic: Target is 7 days from now
    var timeLeft by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val targetTime = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
        }.timeInMillis
        
        while (true) {
            val currentTime = System.currentTimeMillis()
            val diff = targetTime - currentTime
            
            if (diff > 0) {
                val days = diff / (24 * 60 * 60 * 1000)
                val hours = (diff / (60 * 60 * 1000)) % 24
                timeLeft = "$days days and $hours hours"
            } else {
                timeLeft = "Election Ended"
            }
            delay(60000) // Update every minute
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- HEADER SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello, $userName 👋",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Box {
                        IconButton(
                            onClick = { 
                                notificationViewModel.markAllAsRead()
                                navController.navigate("notifications") 
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .size(44.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(MaterialTheme.colorScheme.error, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            )
                        }
                    }
                }
                Text(
                    text = "Ready to make a difference today?",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Election Banner (Dark Blue)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NDEJJE UNIVERSITY", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("STUDENT LEADERSHIP", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("ELECTIONS 2026", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Your Vote Shapes Our Tomorrow",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // --- QUICK ACTIONS SECTION ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Quick Actions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NdejjeQuickActionCard("Vote", "Cast your Vote", Icons.Default.HowToVote, Modifier.weight(1f)) {
                        navController.navigate("vote")
                    }
                    NdejjeQuickActionCard("Results", "view live results", Icons.Default.BarChart, Modifier.weight(1f)) {
                        navController.navigate("results")
                    }
                    NdejjeQuickActionCard("Profile", "My information", Icons.Default.Person, Modifier.weight(1f)) {
                        navController.navigate("profile")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Election Status Box
                Text(
                    text = "Election Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Voting is Currently Active",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ends in: $timeLeft",
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Total Votes: $totalVotes",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Last updated: $lastVoteTime",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

