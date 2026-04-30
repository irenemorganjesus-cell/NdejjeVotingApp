package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.background
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
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun HomeScreen(navController: NavController, userName: String) {
    // Defined colors based on your branding requirements
    val ndejjeDarkBlue = Color(0xFF001F3F)
    val lightBlueHeader = Color(0xFFE3F2FD)
    val pearlWhite = Color(0xFFF5F5F5)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // --- HEADER SECTION (Light Blue) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightBlueHeader)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hello, $userName 👋", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ndejjeDarkBlue)
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier
                            .background(ndejjeDarkBlue, CircleShape)
                            .padding(6.dp)
                            .size(24.dp)
                    )
                }
                Text("Ready to make a difference?", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(20.dp))

                // Election Banner (Dark Blue)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ndejjeDarkBlue)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NDEJJE UNIVERSITY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("STUDENT LEADERSHIP", color = Color.White, fontSize = 16.sp)
                        Text("ELECTIONS 2026", color = Color.White, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Your Vote Shapes Our Tomorrow", color = Color.LightGray, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                    }
                }
            }

            // --- QUICK ACTIONS SECTION ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ndejjeDarkBlue)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionCard("Vote", "Cast your vote", Icons.Default.HowToVote, Modifier.weight(1f), ndejjeDarkBlue) {
                        navController.navigate("vote")
                    }
                    QuickActionCard("Results", "View live results", Icons.Default.BarChart, Modifier.weight(1f), ndejjeDarkBlue) {
                        // Results navigation logic
                    }
                    QuickActionCard("Profile", "My information", Icons.Default.Person, Modifier.weight(1f), ndejjeDarkBlue) {
                        // Profile navigation logic
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Election Status Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = pearlWhite)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Election Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ndejjeDarkBlue)
                        Text("Voting is Currently Active", color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                        Text("Ends in: 2 Days 14 Hours", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, darkBlue: Color, onClick: () -> Unit) {
    // State to toggle background color to dark blue on click
    var isSelected by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(130.dp)
            .clickable {
                isSelected = !isSelected
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) darkBlue else Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else darkBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else darkBlue
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.LightGray else Color.Gray
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem("home", Icons.Default.Home, "Home"),
        NavigationItem("vote", Icons.Default.HowToVote, "Vote"),
        NavigationItem("results", Icons.Default.BarChart, "Results"),
        NavigationItem("profile", Icons.Default.Person, "Profile")
    )

    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001F3F),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color(0xFFE3F2FD)
                )
            )
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val label: String)