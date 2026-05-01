package com.ndejje.votingapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NdejjeDarkBlue,
    onPrimary = White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = NdejjeDarkBlue,
    secondary = NdejjeYellow,
    onSecondary = Color(0xFF333333),
    secondaryContainer = Color(0xFFFFF9C4),
    onSecondaryContainer = Color(0xFF333333),
    background = White,
    onBackground = DarkGray,
    surface = White,
    onSurface = DarkGray,
    surfaceVariant = Color(0xFFF8F9FA),
    onSurfaceVariant = DarkGray,
    outline = Color.LightGray,
    error = AlertRed,
    onError = White,
    errorContainer = AlertRedLight,
    onErrorContainer = Color(0xFFB71C1C)
)

private val DarkColorScheme = darkColorScheme(
    primary = NdejjeLightBlue,
    onPrimary = Color(0xFF001F3F), // Dark blue for contrast against light blue
    primaryContainer = Color(0xFF003366),
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = NdejjeYellow,
    onSecondary = Color(0xFF333333),
    secondaryContainer = Color(0xFF424200),
    onSecondaryContainer = NdejjeYellow,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Color(0xFF252525),
    onSurfaceVariant = DarkOnSurface,
    outline = Color.Gray,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun NdejjeVotingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
