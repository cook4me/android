package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.TupperwareService
import kotlinx.coroutines.launch

class TupCreationViewModel(private val service: TupperwareService) : ViewModel() {

    private var _titleText = mutableStateOf("")
    private var _descText = mutableStateOf("")
    private var _tags = mutableStateListOf<String>()
    private val _images = mutableStateListOf<Uri>()
    private var _formError = mutableStateOf(false)

    val titleText: State<String> = _titleText
    val descText: State<String> = _descText
    val tags: List<String> = _tags
    val images: List<Uri> = _images
    val formError: State<Boolean> = _formError

    fun addImage(uri: Uri) {
        _images.add(uri)
    }

    fun updateTitle(title: String) {
        _titleText.value = title
    }

    fun updateDesc(desc: String) {
        _descText.value = desc
    }

    // TODO implement tags

    fun onSubmit() {
        if (_titleText.value.isBlank() || _descText.value.isBlank() || _images.isEmpty()) {
            _formError.value = true
        } else {
            viewModelScope.launch {
                service.submitForm(
                    _titleText.value,
                    _descText.value,
                    _tags,
                    _images.map { uri -> uri.toString() } // TODO pass actual images
                )
            }
        }
    }
}
