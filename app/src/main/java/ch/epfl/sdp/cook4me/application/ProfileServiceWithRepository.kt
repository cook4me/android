package ch.epfl.sdp.cook4me.application

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository

class ProfileServiceWithRepository(private val repository: ProfileRepository = ProfileRepository()) :
    ProfileService {
    override suspend fun submitForm(
        id: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
    ) {
        repository.update(
            id,
            Profile(
                id,
                username,
                allergies,
                bio,
                favoriteDish,
            )
        )
    }
}
