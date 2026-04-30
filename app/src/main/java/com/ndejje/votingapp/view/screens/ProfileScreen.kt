package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.model.UserEntity

@Composable
fun ProfileScreen(navController: NavController, user: UserEntity?) {
    val ndejjeDarkBlue = Color(0xFF001F3F)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Profile Icon
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = ndejjeDarkBlue
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(24.dp).size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = user?.fullName ?: "Grace",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = ndejjeDarkBlue
            )

            Text(
                text = user?.registrationNumber ?: "23/2/314/D/001",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Profile Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ProfileInfoRow("Course", user?.course ?: "B.IT")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    ProfileInfoRow("Voting Status", if (user?.hasVoted == true) "Voted" else "Not Voted")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ndejjeDarkBlue),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Text("Logout", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Black)
    }
}
