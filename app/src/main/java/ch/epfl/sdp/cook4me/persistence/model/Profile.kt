package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
data class Profile(
    var email: String = "",
    var name: String = "",
    var allergies: String = "",
    var bio: String = "",
    var favoriteDish: String = "",
)
