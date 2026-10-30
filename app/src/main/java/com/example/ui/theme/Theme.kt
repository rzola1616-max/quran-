package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SoftJade,
    onPrimary = DeepEmerald,
    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,
    secondary = MutedGold,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF3D321A),
    onSecondaryContainer = Color(0xFFFFDF9E),
    tertiary = Color(0xFF8AD4B2),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    error = Color(0xFFF0867A),
    onError = Color(0xFF490003)
)

private val LightColorScheme = lightColorScheme(
    primary = DeepEmerald,
    onPrimary = Color.White,
    primaryContainer = SoftJade,
    onPrimaryContainer = DeepEmerald,
    secondary = ForestGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6F4EA),
    onSecondaryContainer = ForestGreen,
    tertiary = MutedGold,
    onTertiary = Color.White,
    background = WarmIvory,
    onBackground = Charcoal,
    surface = Color(0xFFFFFFFF),
    onSurface = Charcoal,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = SecondaryGrey,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun QuranCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
