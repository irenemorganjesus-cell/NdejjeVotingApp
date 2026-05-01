package com.ndejje.votingapp.view.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.ui.theme.LayoutWeights
import com.ndejje.votingapp.view.components.NdejjePrimaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun WelcomeScreen(navController: NavController) {
    //list of images
    val images = listOf(
        R.drawable.university_building,
        R.drawable.university_campus,
        R.drawable.university_kampala
    )

    //Setup Pager State
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Auto-scroll Animation Logic
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(3000) // Stay on image for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 800) // Smooth slide transition
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(dimensionResource(R.dimen.padding_standard_size)),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))

        Text(
            text = stringResource(R.string.welcome_to),
            fontSize = dimensionResource(R.dimen.font_size_welcome).value.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.welcome_app_name),
            fontSize = dimensionResource(R.dimen.font_size_app_title).value.sp,
            lineHeight = dimensionResource(R.dimen.line_height).value.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

        Text(
            text = stringResource(R.string.welcome_tagline),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = dimensionResource(R.dimen.faint_message_size).value.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.welcome_motto),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            fontSize = dimensionResource(R.dimen.together_message_size).value.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(LayoutWeights.StandardWeight))

        // --- ANIMATED IMAGE CAROUSEL SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.university_img_height))
                .graphicsLayer(compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.15f to Color.Black,
                            0.85f to Color.Black,
                            1f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Spacer(modifier = Modifier.weight(LayoutWeights.StandardWeight))

        // Register Button
        NdejjePrimaryButton(
            text = stringResource(R.string.btn_register),
            onClick = { navController.navigate("register") },
            modifier = Modifier.height(dimensionResource(R.dimen.button_height))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_corner_radius)))

        // Login Button
        OutlinedButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            border = androidx.compose.foundation.BorderStroke(
                dimensionResource(R.dimen.border_width),
                MaterialTheme.colorScheme.outline
            )
        ) {
            Text(
                text = stringResource(R.string.btn_login),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = dimensionResource(R.dimen.button_font_size).value.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard_size)))
    }
}