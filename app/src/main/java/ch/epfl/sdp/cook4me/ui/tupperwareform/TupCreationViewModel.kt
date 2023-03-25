package ch.epfl.sdp.cook4me.ui.tupperwareform

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TupCreationViewModel :
    ViewModel() {

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
                // TODO: outcomment
//                repository.add(
//                    Tupperware(
//                        title = _titleText.value,
//                        description = _descText.value,
//                        images = _images.map { uri -> uri.toString() })
//                )
                Log.d("Debug", "$_titleText\n$_descText")
            }
        }
    }
}
