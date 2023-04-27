package ch.epfl.sdp.cook4me.ui.chat

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ChannelViewModel : ViewModel() {
    var selectedChannelId by mutableStateOf("")
}
