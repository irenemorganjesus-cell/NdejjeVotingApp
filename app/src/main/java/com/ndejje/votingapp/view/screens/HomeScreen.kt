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
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    candidateViewModel: com.ndejje.votingapp.viewmodel.CandidateViewModel,
    notificationViewModel: com.ndejje.votingapp.viewmodel.NotificationViewModel
) {
    // Defined colors based on your branding requirements
    val ndejjeDarkBlue = Color(0xFF001F3F)
    val lightBlueAccent = Color(0xFFE3F2FD)
    val pearlWhite = Color(0xFFF5F5F5)

    val totalVotes by candidateViewModel.totalVotes.collectAsState()
    val lastVoteTime by candidateViewModel.lastVoteTime.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    // Countdown logic
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
                .background(Color.White)
        ) {
            // --- HEADER SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightBlueAccent)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello, $userName 👋",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = ndejjeDarkBlue
                    )
                    Box {
                        IconButton(
                            onClick = { 
                                notificationViewModel.markAllAsRead()
                                navController.navigate("notifications") 
                            },
                            modifier = Modifier
                                .background(ndejjeDarkBlue, CircleShape)
                                .size(44.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .border(2.dp, lightBlueAccent, CircleShape)
                            )
                        }
                    }
                }
                Text(
                    text = "Ready to make a difference today?",
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Election Banner (Dark Blue)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ndejjeDarkBlue),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NDEJJE UNIVERSITY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("STUDENT LEADERSHIP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text("ELECTIONS 2026", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Your Vote Shapes Our Tomorrow",
                            color = Color(0xFFBBDEFB),
                            fontSize = 14.sp,
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
                    fontSize = 20.sp,
                    color = ndejjeDarkBlue
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionCard("Vote", "Cast your Vote", Icons.Default.HowToVote, Modifier.weight(1f), ndejjeDarkBlue) {
                        navController.navigate("vote")
                    }
                    QuickActionCard("Results", "view live results", Icons.Default.BarChart, Modifier.weight(1f), ndejjeDarkBlue) {
                        navController.navigate("results")
                    }
                    QuickActionCard("Profile", "My information", Icons.Default.Person, Modifier.weight(1f), ndejjeDarkBlue) {
                        navController.navigate("profile")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Election Status Section
                Text(
                    text = "Election Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = pearlWhite),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Voting is Currently Active",
                            color = Color.Blue,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ends in: $timeLeft",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Total Votes: $totalVotes",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = ndejjeDarkBlue
                )

                Text(
                    text = "Last updated: $lastVoteTime",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, darkBlue: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, darkBlue.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = darkBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray.copy(alpha = 0.8f),
                lineHeight = 12.sp
            )
        }
    }
}
