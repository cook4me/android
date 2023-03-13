package ch.epfl.sdp.cook4me.persistence.model

import android.net.Uri

data class Tupperware(
    val name: String = "",
    val desc: String = "",
    val tags: List<String> = listOf(),
    val photos: List<Uri> = listOf()
)
