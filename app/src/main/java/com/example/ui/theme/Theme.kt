package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDarkGreen,
    onPrimary = TextOnDark,
    background = ScreenBackgroundDark,
    onBackground = TextOnDark,
    surface = CardBackgroundDark,
    onSurface = TextOnDark,
    secondary = SecondaryGreenDark,
    tertiary = AccentGold
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    background = ScreenBackgroundLight,
    onBackground = TextOnLight,
    surface = CardBackgroundLight,
    onSurface = TextOnLight,
    secondary = UserBlue,
    tertiary = OwnerDarkGreen
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
