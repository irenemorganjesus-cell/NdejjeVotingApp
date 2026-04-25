package com.ndejje.votingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NdejjeBlue,      // Buttons and Headers will be Blue
    secondary = NdejjeYellow,  // Highlights will be Yellow
    background = LightGray,    // Screen background will be light gray
    surface = White,           // Cards/Popups will be white
    onPrimary = White,         // Text on blue buttons will be white
    onSecondary = DarkGray     // Text on yellow buttons will be dark
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