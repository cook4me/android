package ch.epfl.sdp.cook4me.application

import android.net.Uri

interface ProfileService {
    suspend fun submitForm(
        id: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
    )
}
