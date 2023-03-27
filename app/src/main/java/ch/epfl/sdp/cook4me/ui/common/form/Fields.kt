package ch.epfl.sdp.cook4me.ui.common.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

/**
 * A simple component that displays a question and a text field
 * @param question the question to be displayed
 * @param label the label of the text field
 * @param onTextChanged the function to be called when the text field is changed
 */
@Composable
fun InputField(
    question: String,
    label: String = "",
    onTextChanged: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        TextField(
            value = text.value,
            onValueChange = {
                onTextChanged(it)
                text.value = it
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .testTag(stringResource(R.string.TAG_EMAIL_FIELD)),
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text("Email") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier
            .testTag(stringResource(R.string.TAG_PASSWORD_FIELD)),
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = "Password") },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}
