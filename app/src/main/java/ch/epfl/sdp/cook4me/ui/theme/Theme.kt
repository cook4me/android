package ch.epfl.sdp.cook4me.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = primaryBlue,
    onPrimary = Color.White,
    secondary = secondaryTurquoise,
    onSecondary = Color.White,
    error = errorRed,
    onError = errorRed
)

@Composable
fun Cook4meTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
