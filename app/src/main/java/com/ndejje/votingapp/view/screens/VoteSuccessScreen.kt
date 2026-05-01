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

import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.votingapp.R

@Composable
fun VoteSuccessScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(dimensionResource(R.dimen.padding_standard)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Success Tick Image
        Image(
            painter = painterResource(id = R.drawable.ic_tick_rounded),
            contentDescription = stringResource(R.string.success_tick_desc),
            modifier = Modifier.size(dimensionResource(R.dimen.success_icon_size))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

        // 2. Main Heading
        Text(
            text = stringResource(R.string.success_title),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = dimensionResource(R.dimen.font_size_h1).value.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // 3. Faint Thank You Message
        Text(
            text = stringResource(R.string.success_thank_you),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            fontSize = dimensionResource(R.dimen.font_size_h6).value.sp
        )

        // 4. Success Subtext
        Text(
            text = stringResource(R.string.success_message),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_standard_size))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        // 5. Ballot Box Image
        Image(
            painter = painterResource(id = R.drawable.img_ballot_box),
            contentDescription = stringResource(R.string.success_ballot_box_desc),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.success_image_height)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.success_bottom_spacer)))

        // 6. Back to Home Button
        Button(
            onClick = {
                // Navigate home and clear the backstack so they can't "Go Back" to the vote screen
                navController.navigate("home") {
                    popUpTo(0)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.success_btn_home),
                fontSize = dimensionResource(R.dimen.button_font_size).value.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
