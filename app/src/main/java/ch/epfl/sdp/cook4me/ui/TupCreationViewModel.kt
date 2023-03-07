package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel


class TupCreationViewModel : ViewModel() {
    private var _titleText by mutableStateOf("")
    private var _descText by mutableStateOf("")
    private var _tags = mutableStateListOf<String>()
    private val _images = mutableStateListOf<Uri>()

    val titleText: String = _titleText
    val descText: String = _descText
    val tags: List<String> = _tags
    val images: List<Uri> = _images

    fun takePhoto() {
    }

    fun selectImage() {
    }

    fun addImage(uri: Uri?) {
    }

    fun submitForm() {

    }

    fun onClickImage() {

    }


}