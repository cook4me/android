package ch.epfl.sdp.cook4me.ui.common.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

const val BUTTON_VERTICAL_PROPORTION = 0.07f

@Composable
fun CreateNewItemButton(
    itemType: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    canClick: Boolean = true
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(screenHeight * BUTTON_VERTICAL_PROPORTION),
        enabled = canClick,
        colors = if (canClick) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                contentColor = Color.White
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.create_new_item_type) + " " + itemType,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.create_new_item_type) + " " + itemType)
        }
    }
}
