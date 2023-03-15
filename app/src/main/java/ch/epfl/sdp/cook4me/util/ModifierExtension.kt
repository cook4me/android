package ch.epfl.sdp.cook4me.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.basicButton(): Modifier =
    this.fillMaxWidth().padding(16.dp, 8.dp)

fun Modifier.fieldModifier(): Modifier =
    this.fillMaxWidth().padding(16.dp, 4.dp)
