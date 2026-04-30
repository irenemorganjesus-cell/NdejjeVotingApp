package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun VoteSuccessScreen(navController: NavController) {
    val ndejjeDarkBlue = Color(0xFF001F3F)
    val faintWhite = Color(0xB3FFFFFF) // White with 70% opacity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ndejjeDarkBlue)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Success Tick Image
        Image(
            painter = painterResource(id = com.ndejje.votingapp.R.drawable.ic_tick_rounded),
            contentDescription = "Success Tick",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Main Heading
        Text(
            text = "Vote Submitted",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Faint Thank You Message
        Text(
            text = "Thank you for voting.",
            color = faintWhite,
            fontSize = 18.sp
        )

        // 4. Success Subtext
        Text(
            text = "Your vote has been recorded successfully.",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 5. Ballot Box Image
        Image(
            painter = painterResource(id = com.ndejje.votingapp.R.drawable.img_ballot_box),
            contentDescription = "Ballot Box",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(60.dp))

        // 6. Back to Home Button
        Button(
            onClick = {
                // Navigate home and clear the backstack so they can't "Go Back" to the vote screen
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = ndejjeDarkBlue
            )
        ) {
            Text(
                text = "Back to Home",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}