package ch.epfl.sdp.cook4me.ui.common.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

/**
 * A simple component that displays a question and a slider
 * @param textRes the question to be displayed
 * @param min the minimum value of the slider
 * @param max the maximum value of the slider
 * @param onValueChange the function to be called when the slider is changed
 * @param modifier the modifier of the slider
 */
@Composable
fun MayParticipantsPicker(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    onValueChange: (Int) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        FieldTitle(
            text = "${stringResource(textRes)}",
            modifier = Modifier.padding(end = 10.dp),
        )
        CustomTextField(
            textStyle = MaterialTheme.typography.caption,
            modifier = Modifier
                .width(50.dp)
                .height(45.dp),
            contentDescription = stringResource(R.string.RecipeCreationServingsTextFieldDesc),
            value = text,
            onValueChange = {
                val max = it.takeWhile { c -> c.isDigit() }.take(2)
                text = max
                onValueChange(
                    max.ifBlank { "0" }.toInt()
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = false
        )
    }
}
