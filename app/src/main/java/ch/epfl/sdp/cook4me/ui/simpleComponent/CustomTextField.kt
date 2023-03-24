package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    contentDescription: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    isError: Boolean = false,
    shape: Shape = RoundedCornerShape(30.dp),
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val stateDescription = if (isError) { "Error" } else { "" }

    TextField(
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            this.stateDescription = stateDescription
        },
        textStyle = textStyle,
        value = value, onValueChange = onValueChange,
        isError = isError,
        shape = shape,
        singleLine = singleLine,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        visualTransformation = visualTransformation
    )
}
