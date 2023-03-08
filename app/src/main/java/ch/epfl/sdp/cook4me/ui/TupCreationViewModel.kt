package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.TupperwareService


class TupCreationViewModel : ViewModel() {
    private val service = TupperwareService()

    private var _titleText by mutableStateOf("")
    private var _descText by mutableStateOf("")
    private var _tags = mutableStateListOf<String>()
    private val _images = mutableStateListOf<Uri>()

    val titleText: String = _titleText
    val descText: String = _descText
    val tags: List<String> = _tags
    val images: List<Uri> = _images

    fun addImage(uri: Uri) {
        _images.add(uri)
    }

    fun onSubmit() {
        service.submitForm(
            _titleText,
            _descText,
            _tags,
            _images,
        )
    }

    fun onClickImage() {

    }


}