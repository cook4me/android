package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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
                contentDescription = "",
                textStyle = textStyle,
                onValueChange = {}
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        content = { Text(text = item) },
                        onClick = {
                            onValueChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
