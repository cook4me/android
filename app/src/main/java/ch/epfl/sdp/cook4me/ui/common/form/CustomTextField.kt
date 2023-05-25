package ch.epfl.sdp.cook4me.ui.common.form

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    contentDescription: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    isError: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val stateDescription = if (isError) { "Error" } else { "" }

    OutlinedTextField(
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            this.stateDescription = stateDescription
        },
        textStyle = textStyle,
        value = value, onValueChange = onValueChange,
        isError = isError,
        shape = shape,
        singleLine = singleLine,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        visualTransformation = visualTransformation
    )
}
