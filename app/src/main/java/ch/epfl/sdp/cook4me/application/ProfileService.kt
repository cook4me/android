package ch.epfl.sdp.cook4me.application

interface ProfileService {
    suspend fun submitForm(
        id: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
    )
}
