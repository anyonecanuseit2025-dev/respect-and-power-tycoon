package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GeometricColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF231B00),
    primaryContainer = Color(0xFF433400),
    onPrimaryContainer = GoldAccent,
    secondary = CyanTactical,
    onSecondary = Color(0xFF00354E),
    secondaryContainer = Color(0xFF004D71),
    onSecondaryContainer = CyanAccent,
    tertiary = CrimsonSoft,
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    background = GeoBackground,
    onBackground = GeoTextPrimary,
    surface = GeoSurface,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoSurfaceElevated,
    onSurfaceVariant = GeoTextSecondary,
    outline = GeoBorder,
    outlineVariant = GeoBorderSubtle,
    error = CrimsonAlert,
    onError = Color(0xFF601410)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GeometricColorScheme,
        typography = Typography,
        content = content
    )
}
