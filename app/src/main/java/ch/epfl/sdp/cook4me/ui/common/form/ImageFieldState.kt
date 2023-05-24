package ch.epfl.sdp.cook4me.ui.common.form

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.lang.reflect.Array.get

open class ImageFieldState(
    private val errorMsg: String,
) : FormElementState {
    var image: Uri? by mutableStateOf(null)
    private var displayErrors: Boolean by mutableStateOf(false)
    override val isValid
        get() = image != null

    override val errorMessage = errorMsg

    override fun enableShowErrors() {
        displayErrors = true
    }

    override fun showErrors(): Boolean {
        println(displayErrors)
        return !isValid && displayErrors
    }
}