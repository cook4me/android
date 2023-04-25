package ch.epfl.sdp.cook4me.ui.profile

import ch.epfl.sdp.cook4me.application.ProfileService

class MockProfileService(
    private val expectedId: String,
    private val expectedUsername: String,
    private val expectedAllergies: String,
    private val expectedBio: String,
    private val expectedFavoriteDish: String,
    private val expectedImage: String,
) : ProfileService {
    override suspend fun submitForm(
        id: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
    ) {
        assert(expectedId == id)
        assert(expectedUsername == username)
        assert(expectedAllergies == allergies)
        assert(expectedBio == bio)
        assert(expectedFavoriteDish == favoriteDish)
        assert(expectedImage == userImage)
    }
}
