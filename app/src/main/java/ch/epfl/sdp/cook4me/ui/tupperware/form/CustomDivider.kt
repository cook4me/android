package ch.epfl.sdp.cook4me.ui.tupperware.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

private val defaultColor = Color(0f, 0f, 0f, 0.25f)

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier,
    color: Color = defaultColor,
    thickness: Dp = 0.5.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier.testTag("CustomDivider"),
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

@Preview(showBackground = true)
@Composable
fun DividerPreview() {
    Cook4meTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CustomDivider()
        }
    }
}
