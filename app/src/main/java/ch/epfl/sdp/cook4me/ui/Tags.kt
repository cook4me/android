package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val defaultTagColor = Color.Gray
private val defaultTextColor = Color.White

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = defaultTagColor,
    textColor: Color = defaultTextColor,
    elevation: Dp = 0.dp
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color,
        elevation = elevation
    ) {
        Text(text = text, color = textColor, modifier = Modifier.padding(5.dp))
    }
}

@Composable
fun Tags(
    tags: List<String>,
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = defaultTagColor,
    textColor: Color = defaultTextColor,
    elevation: Dp = 0.dp
) {
    Column {
        tags.map {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagPreview() {
    val tags: List<String> = listOf(
        "pizza",
        "vegan",
        "italian-mexican",
        "borderline-criminal",
        "fucking-delicious-my-dude",
        "contains-peanuts",
        "sausage",
        "healthy",
        "fitness"
    )
    Row(
    ) {

    }
}