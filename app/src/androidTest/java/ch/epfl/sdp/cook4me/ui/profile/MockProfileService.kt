package ch.epfl.sdp.cook4me.ui.profile

import ch.epfl.sdp.cook4me.application.ProfileService

class MockProfileService(
    private val expectedCredentials: String,
    private val expectedUsername: String,
    private val expectedAllergies: String,
    private val expectedBio: String,
    private val expectedFavoriteDish: String,
    private val expectedImage: String,
) : ProfileService {
    override suspend fun submitForm(
        credentials: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
    ) {
        assert(expectedCredentials == credentials)
        assert(expectedUsername == username)
        assert(expectedAllergies==allergies)
        assert(expectedBio==bio)
        assert(expectedFavoriteDish==favoriteDish)
        assert(expectedImage==userImage)
    }

    override suspend fun loadProfile(
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String
    ) {
        TODO("Not yet implemented")
    }
}
