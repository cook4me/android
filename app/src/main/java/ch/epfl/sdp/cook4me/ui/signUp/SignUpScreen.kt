import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@Preview(showBackground = true)
@Composable
fun SignUpScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        CustomTitleText(stringResource(R.string.tag_singUpTitle))

        continueBack_buttons()

        // Text field for the email
        columnTextBtn_SignUpScreen(
            stringResource(R.string.tag_email),
            stringResource(R.string.default_email)
        )

        // Password field
        Password_signUpScreen()
    }
}

@Composable
fun Password_signUpScreen() {
    var password by rememberSaveable { mutableStateOf("") }

    input_row {
        Text(stringResource(R.string.tag_password), modifier = Modifier.width(100.dp))
        TextField(
            value = password,
            modifier = Modifier.testTag(stringResource(R.string.tag_password)),
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.default_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = colorsTextfield_SignUpScreen()
        )
    }
}

@Composable
fun columnTextBtn_SignUpScreen(
    displayLabel: String,
    defaultText: String
) {
    var textInputVariable by rememberSaveable {
        mutableStateOf("")
    }

    input_row {
        Text(
            text = displayLabel,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            placeholder = {
                Text(
                    defaultText
                )
            },
            value = textInputVariable,
            modifier = Modifier.testTag(displayLabel),
            onValueChange = { textInputVariable = it },
            colors = colorsTextfield_SignUpScreen()
        )
    }
}

@Composable
fun colorsTextfield_SignUpScreen(): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        textColor = Color.Black
    )

@Composable
private fun continueBack_buttons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text_buttons(nameBtn = stringResource(R.string.btn_back))

        text_buttons(nameBtn = stringResource(R.string.btn_continue))
    }
}

@Composable
private fun text_buttons(nameBtn: String) {
    Text(
        text = nameBtn,
        modifier = Modifier
            .testTag(nameBtn)
            .clickable {}
    )
}

@Composable
private fun input_row(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun CustomTitleText(text: String = "") {
    Text(
        modifier = Modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}

