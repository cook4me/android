package ch.epfl.sdp.cook4me.persistence.model

data class Profile(
    var credentials: String = "",
    var name: String = "",
    var bio: String = "",
    var allergies: String = "",
    var favoriteDish: String = "",
    var userImage: String = "",
    var photos: List<String> = listOf(),
)
