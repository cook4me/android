package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository

class ProfileServiceWithRepository(private val repository: ProfileRepository = ProfileRepository()) :
    ProfileService {
    override suspend fun submitForm(
        credentials: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
    ) {
        repository.update("1234",
            Profile(
                credentials,
                username,
                allergies,
                bio,
                favoriteDish,
                userImage,
            )
        )
    }
}
