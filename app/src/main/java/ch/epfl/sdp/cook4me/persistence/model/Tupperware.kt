package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
data class Tupperware(
    var title: String = "",
    var description: String = "",
    var user: String = ""
)
