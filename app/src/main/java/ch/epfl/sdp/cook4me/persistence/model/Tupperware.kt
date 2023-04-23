package ch.epfl.sdp.cook4me.persistence.model

data class Tupperware(
    val title: String = "",
    val description: String = "",
    val images: List<String> = listOf()
)
