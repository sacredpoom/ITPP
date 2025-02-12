package p.ITPP.ui.theme

import android.app.Activity
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

private val GreenColorScheme = lightColorScheme(
    primary = greenPrimary,
    secondary = greenSecondary,
    tertiary =  greenTertiary,
    onPrimary = greenOnPrimary,
    onSecondary = black,
    primaryContainer = greenPrimaryContainer,
    onPrimaryContainer = greenOnPrimaryContainer,
    surface = greenSurface,
    surfaceVariant = greenSurfaceDim
)

private val BlueColorScheme = lightColorScheme(
    primary = bluePrimary,
    secondary = blueSecondary,
    tertiary =  blueTertiary,
    onPrimary = blueOnPrimary,
    primaryContainer = bluePrimaryContainer,
    onPrimaryContainer = blueOnPrimaryContainer,
    surface = blueSurface,
    surfaceVariant = blueSurfaceDim
)

private val RedColorScheme = lightColorScheme(
    primary = redPrimary,
    secondary = redSecondary,
    tertiary =  redTertiary,
    onPrimary = redOnPrimary,
    primaryContainer = redPrimaryContainer,
    onPrimaryContainer = redOnPrimaryContainer,
    surface = redSurface,
    surfaceVariant = redSurfaceDim
)

@Composable
fun ITPPTheme(themeIndex: Int, content: @Composable () -> Unit) {
    val colorScheme = when (themeIndex) {
        0 -> GreenColorScheme
        1 -> BlueColorScheme
        else -> RedColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}