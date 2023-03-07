package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun Header(
    title: String = "",
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    color: Color = MaterialTheme.colors.primary,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        elevation = elevation,
        color = color,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Cook4meTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Header", style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}