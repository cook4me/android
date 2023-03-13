package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.cook4me.application.TupperwareService

class TupCreationViewModel(private val service: TupperwareService = TupperwareService()) : ViewModel() {

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

    fun updateTags(tags: String) {
        _tags = tags.split(" ,.'\"", ignoreCase = false) as SnapshotStateList<String>
    }

    fun onSubmit() {
        if (_titleText.value == "" || _descText.value == "" || _images.isEmpty()) {
            _formError.value = true
        } else {
            service.submitForm(
                _titleText.value,
                _descText.value,
                _tags,
                _images,
            )
        }
    }
}

class TupCreationViewModelFactory(private val service: TupperwareService)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TupCreationViewModel::class.java))
            return TupCreationViewModel(service) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
