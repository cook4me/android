package ch.epfl.sdp.cook4me.ui.common.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
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
    @StringRes text: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    val valueChosen = remember { mutableStateOf(min) }
    Text(
        text = "${stringResource(text)}:${valueChosen.value}",
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Slider(
        value = valueChosen.value.toFloat(),
        onValueChange = { newValue ->
            val newValueInt = newValue.toInt()
            if (valueChosen.value != newValueInt) {
                valueChosen.value = newValueInt
                onValueChange(newValueInt)
            }
        },
        valueRange = min.toFloat()..max.toFloat(),
        steps = max - min,
        modifier = modifier.testTag("slider")
    )
}
