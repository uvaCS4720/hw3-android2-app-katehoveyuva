package edu.nd.pmcburne.hwapp.one.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = NavyPrimary,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    error = ErrorRed
)

// We define a simple light scheme just in case,
// though your MainActivity forces Dark Mode anyway.
private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun HWStarterRepoTheme(
    darkTheme: Boolean = true, // Default to true for your "moody" requirement
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // This uses the standard Typography.kt file
        content = content
    )
}