package ch.epfl.sdp.cook4me.domain

data class ProfileData(
    private val username: String,
    private val favoriteDish: String = "Pizza",
    private val allergies: String = "No Allergies",
    private val bio: String = "Hi there I'm a chef",
    private val imageURI: String = ""
) {
    fun getUsername(): String {
        return username
    }
    fun getFavoriteDish(): String {
        return favoriteDish
    }
    fun getAllergies(): String {
        return allergies
    }
    fun getBio(): String {
        return bio
    }
    fun getImageURI(): String {
        return imageURI
    }
}
