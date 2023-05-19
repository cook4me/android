package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

private val CORNER_SIZE = 15.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onSearch: () -> Unit = {},
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val isFocused = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val color = if (isFocused.value) MaterialTheme.colors.primary else Color.LightGray

    Box(
        modifier = modifier
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(CORNER_SIZE))
            .background(Color.White, shape = RoundedCornerShape(CORNER_SIZE))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isFocused.value) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            }
            BasicTextField(
                value = text,
                onValueChange = {
                    onTextChange(it.filter { c -> c != '\n' }) // singleLine = true doesn't seem to work
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .onFocusChanged { isFocused.value = it.isFocused },
                singleLine = true,
                textStyle = textStyle,
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    if (text.isBlank() && !isFocused.value) {
                        Text(
                            text = "Search...",
                            color = TextFieldDefaults.outlinedTextFieldColors().placeholderColor(
                                enabled = true
                            ).value,
                            modifier = Modifier.padding(start = 8.dp),
                            style = textStyle
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                        isFocused.value = false
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    Cook4meTheme {
        SearchBar(text = "", onTextChange = {})
    }
}
