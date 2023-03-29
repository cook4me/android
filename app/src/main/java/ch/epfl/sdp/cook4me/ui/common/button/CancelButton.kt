package ch.epfl.sdp.cook4me.ui.common.button

import androidx.annotation.StringRes
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun CancelButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
) {
    OutlinedButton(onCancelClick, modifier) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}
