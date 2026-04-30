package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
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
    val lightBlueAccent = Color(0xFFE3F2FD)
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
            // --- HEADER SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightBlueAccent)
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello, $userName 👋",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = ndejjeDarkBlue,
                        lineHeight = 46.sp
                    )
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier
                            .background(ndejjeDarkBlue, CircleShape)
                            .padding(16.dp)
                            .size(44.dp)
                    )
                }
                Text(
                    text = "Ready to make a difference today?",
                    fontSize = 26.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Election Banner (Dark Blue)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(36.dp),
                    colors = CardDefaults.cardColors(containerColor = ndejjeDarkBlue),
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NDEJJE UNIVERSITY", color = Color.White, fontWeight = FontWeight.Black, fontSize = 34.sp)
                        Text("STUDENT LEADERSHIP", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                        Text("ELECTIONS 2026", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            "Your Vote Shapes Our Tomorrow",
                            color = Color(0xFFBBDEFB),
                            fontSize = 22.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // --- QUICK ACTIONS SECTION ---
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Quick Actions",
                    fontWeight = FontWeight.Black,
                    fontSize = 34.sp,
                    color = ndejjeDarkBlue
                )
                Spacer(modifier = Modifier.height(28.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    QuickActionCard("Vote", "Cast your vote", Icons.Default.HowToVote, Modifier.weight(1f), ndejjeDarkBlue) {
                        navController.navigate("vote")
                    }
                    QuickActionCard("Results", "Live updates", Icons.Default.BarChart, Modifier.weight(1f), ndejjeDarkBlue) {
                        // Results navigation logic
                    }
                    QuickActionCard("Profile", "My Info", Icons.Default.Person, Modifier.weight(1f), ndejjeDarkBlue) {
                        // Profile navigation logic
                    }
                }

                Spacer(modifier = Modifier.height(44.dp))

                // Election Status Box (Branded Header)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(4.dp, ndejjeDarkBlue),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ndejjeDarkBlue)
                                .padding(20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Election Status", fontWeight = FontWeight.Black, fontSize = 30.sp, color = Color.White)
                            }
                        }
                        Column(modifier = Modifier.padding(32.dp)) {
                            Text("Voting is Currently Active", color = Color(0xFF1B5E20), fontWeight = FontWeight.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Ends in: 2 Days 14 Hours", color = Color.Gray, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, darkBlue: Color, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(220.dp)
            .clickable {
                isPressed = !isPressed
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isPressed) darkBlue else Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        border = BorderStroke(3.dp, darkBlue.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isPressed) Color.White else darkBlue,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                title,
                fontWeight = FontWeight.Black,
                fontSize = 26.sp,
                color = if (isPressed) Color.White else darkBlue
            )
            Text(
                subtitle,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = if (isPressed) Color(0xFFE3F2FD) else Color.Gray,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold
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

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 20.dp,
        modifier = Modifier.height(110.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(40.dp)) },
                label = { Text(item.label, fontSize = 18.sp, fontWeight = FontWeight.Black) },
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
