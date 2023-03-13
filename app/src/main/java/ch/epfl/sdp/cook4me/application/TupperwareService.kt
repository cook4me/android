package ch.epfl.sdp.cook4me.application

import android.net.Uri
import android.util.Log

open class TupperwareService {

    open fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<Uri>,
    ) {

    }
}
