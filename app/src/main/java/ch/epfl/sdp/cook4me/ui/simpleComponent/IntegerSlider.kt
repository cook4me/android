package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * A simple component that displays a question and a slider
 * @param text the question to be displayed
 * @param min the minimum value of the slider
 * @param max the maximum value of the slider
 * @param onValueChange the function to be called when the slider is changed
 * @param modifier the modifier of the slider
 */
@Composable
fun IntegerSlider(
    modifier: Modifier = Modifier,
    text: String = "",
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    // initial value is min
    onValueChange(min)

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
        modifier = modifier.testTag("slider")
    )
}