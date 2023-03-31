package ch.epfl.sdp.cook4me.ui.common.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val dropDownMenuCornerSize = 8.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    options: List<String> = listOf(),
    textStyle: TextStyle = MaterialTheme.typography.body1,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    contentDescription: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            CustomTextField(
                shape = RoundedCornerShape(dropDownMenuCornerSize),
                value = value,
                readOnly = true,
                // trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier,
                contentDescription = contentDescription,
                textStyle = textStyle,
                onValueChange = {}
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(item)
                            expanded = false
                        },
                        content = { Text(text = item) },
                    )
                }
            }
        }
    }
}

@Composable
fun DropDownMenuWithTitle(
    onValueChange: (String) -> Unit = {},
    options: List<String> = listOf(),
    value: String = "",
    titleText: String = "",
    textPadding: Dp = 5.dp,
    contentDescription: String,
    height: Dp? = null,
    width: Dp? = null,
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = textPadding),
            text = titleText
        )
        val dropDownHeight = if (height == null) Modifier else Modifier.height(height)
        val dropDownModifier = if (width == null) dropDownHeight else dropDownHeight.width(width)

        CustomDropDownMenu(
            textStyle = MaterialTheme.typography.caption,
            modifier = dropDownModifier,
            options = options,
            onValueChange = onValueChange,
            value = value,
            contentDescription = contentDescription
        )
    }
}
