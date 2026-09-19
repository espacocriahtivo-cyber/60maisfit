package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = FitLime,
    onPrimary = Color.Black,
    primaryContainer = FitLimeDark,
    onPrimaryContainer = Color.White,
    secondary = FitLimeBright,
    onSecondary = Color.Black,
    background = FitDarkBackground,
    onBackground = FitTextPrimaryLight,
    surface = FitDarkSurface,
    onSurface = FitTextPrimaryLight,
    surfaceVariant = FitDarkSurfaceVariant,
    onSurfaceVariant = FitTextSecondaryLight,
    outline = Color(0xFF374151)
)

val LightColorScheme = lightColorScheme(
    primary = FitLime,
    onPrimary = Color.Black,
    primaryContainer = FitLimeContainer,
    onPrimaryContainer = FitLimeDark,
    secondary = FitLimeDark,
    onSecondary = Color.White,
    background = FitLightBackground,
    onBackground = FitTextPrimaryDark,
    surface = FitLightSurface,
    onSurface = FitTextPrimaryDark,
    surfaceVariant = FitLightSurfaceVariant,
    onSurfaceVariant = FitTextSecondaryDark,
    outline = BorderSubtle
)

val HighContrastColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF000000),
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color.Black,
    outline = Color.Black
)

data class AccessibilityState(
    val highContrast: Boolean = false,
    val largeFont: Boolean = false,
    val voiceAssistance: Boolean = false
)

val LocalAccessibility = compositionLocalOf { AccessibilityState() }

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accessibilityState: AccessibilityState = AccessibilityState(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        accessibilityState.highContrast -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalAccessibility provides accessibilityState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
