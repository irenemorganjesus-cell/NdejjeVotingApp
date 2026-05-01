package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(4000L)
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //make the logo round shaped
        Surface(
            modifier = Modifier
                .size(dimensionResource(R.dimen.logo_badge_size)) // Total size of the badge
                .clip(CircleShape)
                .border(
                    width = dimensionResource(R.dimen.logo_border_thickness),
                    color = Color.White.copy(alpha = 0.5f),
                    shape = CircleShape
                ),
            color = Color.White
        ) {

            //university logo
            Image(
                painter = painterResource(id = R.drawable.university_logo),
                contentDescription = stringResource(R.string.logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_standard))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard_size)))

        //University name
        Text(
            text = stringResource(R.string.university_name),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = dimensionResource(R.dimen.font_size_title).value.sp,
            fontWeight = FontWeight.Bold
        )

        //System name
        Text(
            text = stringResource(R.string.system_name),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = dimensionResource(R.dimen.font_size_subtitle).value.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        //motto
        Text(
            text = stringResource(R.string.motto),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            fontSize = dimensionResource(R.dimen.font_size_body).value.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.loading_indicator_spacing)))

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = dimensionResource(R.dimen.indicator_stroke).value.dp
        )

        // "Loading..." here
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard_size)))

        Text(
            text = stringResource(R.string.loading_text),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = dimensionResource(R.dimen.font_size_body).value.sp,
            fontWeight = FontWeight.Normal
        )
    }
}