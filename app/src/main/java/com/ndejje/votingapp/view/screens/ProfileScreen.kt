package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.ndejje.votingapp.view.components.BottomNavigationBar

@Composable
fun ProfileScreen(navController: NavController, user: UserEntity?) {
    val ndejjeDarkBlue = Color(0xFF001F3F)
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Profile Icon
            Surface(
                modifier = Modifier.size(90.dp),
                shape = CircleShape,
                color = ndejjeDarkBlue
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(18.dp).size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user?.fullName ?: "Grace",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ndejjeDarkBlue
            )

            Text(
                text = user?.registrationNumber ?: "23/2/314/D/001",
                fontSize = 15.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoRow("Course", user?.course ?: "B.IT")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.LightGray.copy(alpha = 0.4f))
                    
                    ProfileInfoRow("Year of Study", "Year ${user?.yearOfStudy ?: "1"}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.LightGray.copy(alpha = 0.4f))
                    
                    ProfileInfoRow("Campus", user?.campus ?: "Kampala Campus")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.LightGray.copy(alpha = 0.4f))
                    
                    val emailDisplay = user?.email?.let { if (it.isEmpty()) "Not set" else it } ?: "Not set"
                    ProfileInfoRow("Email", emailDisplay)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.LightGray.copy(alpha = 0.4f))
                    
                    val phoneDisplay = user?.phoneNumber?.let { if (it.isEmpty()) "Not set" else it } ?: "Not set"
                    ProfileInfoRow("Phone", phoneDisplay)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.LightGray.copy(alpha = 0.4f))

                    ProfileInfoRow("Voting Status", if (user?.hasVoted == true) "Voted" else "Not Voted")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            OutlinedButton(
                onClick = { /* TODO: Implement Edit Profile */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ndejjeDarkBlue)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp), tint = ndejjeDarkBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile", fontSize = 16.sp, color = ndejjeDarkBlue, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ndejjeDarkBlue),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
    }
}
