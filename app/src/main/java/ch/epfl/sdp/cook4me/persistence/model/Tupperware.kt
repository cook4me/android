package ch.epfl.sdp.cook4me.persistence.model

data class Tupperware(
    val name: String = "",
    val desc: String = "",
    val tags: List<String> = listOf(),
    val photos: List<String> = listOf()
)
