package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenDarkPrimary,
    onPrimary = Color(0xFF00390B),
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = Color(0xFFA6F59F),
    secondary = GreenLight,
    onSecondary = Color(0xFF003A0F),
    tertiary = Color(0xFFFFB74D),
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Color(0xFFE1E4DE),
    onSurface = Color(0xFFE1E4DE)
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = Color(0xFF002204),
    secondary = GreenPrimaryMedium,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7E8D5),
    onSecondaryContainer = Color(0xFF0A200B),
    tertiary = AmberAccent,
    onTertiary = Color.White,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = Color(0xFF3E1200),
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFE2E9DF),
    onSurfaceVariant = TextSecondary,
    outline = OutlineLight
)

@Composable
fun MeraKaamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand dark green / light green palette consistently
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
