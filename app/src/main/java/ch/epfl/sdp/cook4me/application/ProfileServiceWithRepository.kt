package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository

class ProfileServiceWithRepository(private val repository: ProfileRepository = ProfileRepository()) :
    ProfileService {
    override suspend fun submitForm(
        username: String,
        password: String,
        email: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
        photos: List<String>,
    ) {
        repository.add(
            Profile(
                username,
                password,
                email,
                allergies,
                bio,
                favoriteDish,
                userImage,
                photos
            )
        )
    }
}