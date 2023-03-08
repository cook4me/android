package ch.epfl.sdp.cook4me.application

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.COLLECTION_PATH

class TupperwareService(
) {


    fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<Uri>,
    ) {
      println("It was good")
    }
}