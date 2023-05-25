package ch.epfl.sdp.cook4me.ui.common.form

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.common.button.CancelButton
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton

@Composable
fun FormButtons(
    @StringRes onCancelText: Int,
    @StringRes onSaveText: Int,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Color.White)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CancelButton(onCancelText, Modifier.weight(weight = 1f), onCancelClick)
        Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.05f))
        LoadingButton(onSaveText, Modifier.weight(weight = 1f), isLoading, action = onSaveClick)
    }
}
