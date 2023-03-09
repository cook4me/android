package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntegerSlider(
    modifier: Modifier = Modifier,
    text: String = "",
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    var value by remember { mutableStateOf(min) }
    Text(
        text = "$text:$value",
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Slider(
        value = value.toFloat(),
        onValueChange = { newValue ->
            value = newValue.toInt()
            onValueChange(newValue.toInt())
        },
        valueRange = min.toFloat()..max.toFloat(),
        steps = (max - min),
        modifier = modifier
    )
}