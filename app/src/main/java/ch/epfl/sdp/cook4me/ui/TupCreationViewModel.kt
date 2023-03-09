package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.TupperwareService


class TupCreationViewModel : ViewModel() {
    private val service = TupperwareService()


    private var _titleText = mutableStateOf("")
    private var _descText = mutableStateOf("")
    private var _tags = mutableStateListOf<String>()
    private val _images = mutableStateListOf<Uri>()

    val titleText: State<String> = _titleText
    val descText: State<String> = _descText
    val tags: List<String> = _tags
    val images: List<Uri> = _images

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
        service.submitForm(
            _titleText.value,
            _descText.value,
            _tags,
            _images,
        )
    }

    fun onClickImage() {

    }


}