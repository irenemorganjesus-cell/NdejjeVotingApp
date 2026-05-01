package com.ndejje.votingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NdejjeDarkBlue,      // Primary brand color
    secondary = NdejjeYellow,  // Highlights
    background = White,    // Clean background
    surface = White,
    onPrimary = White,
    onSecondary = DarkGray
)

@Composable
fun NdejjeVotingAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}