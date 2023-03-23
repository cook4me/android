package ch.epfl.sdp.cook4me.application

interface ProfileService {
    suspend fun submitForm(
        username: String,
        password: String,
        email: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
        photos: List<String>
    )
}
