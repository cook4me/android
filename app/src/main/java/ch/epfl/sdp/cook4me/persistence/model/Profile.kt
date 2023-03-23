package ch.epfl.sdp.cook4me.persistence.model

data class Profile(
    var name: String = "",
    var bio: String = "",
    var password: String = "",
    var email: String = "",
    var allergies: String = "",
    var favoriteDish: String = "",
    var UserImage: String = "",
    var photos: List<String> = listOf(),
)
