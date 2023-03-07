package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    onCancelPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    val padding = 20.dp
    val buttonShape = RoundedCornerShape(20.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        elevation = 20.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=padding, end=padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onClick = onCancelPressed,
                shape = buttonShape,
                colors = buttonColors(backgroundColor = Color(0.6f, 0.1f, 0.1f, 1f)),
            ) {
                Text(text = "Cancel", color = Color.White)
            }

            Spacer(modifier = Modifier.size(padding))

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onClick = onDonePressed,
                shape = buttonShape,
            ) {
                Text(text = "Done")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonRowPreview() {
    Cook4meTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) { Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            ButtonRow(modifier = Modifier.height(80.dp), onCancelPressed = { /*TODO*/ }) {
            }
        }

        }
    }
}