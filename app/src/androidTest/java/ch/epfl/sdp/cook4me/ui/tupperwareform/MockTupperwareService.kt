package ch.epfl.sdp.cook4me.ui.tupperwareform

import ch.epfl.sdp.cook4me.application.TupperwareService

class MockTupperwareService(
    private val expectedTitle: String,
    private val expectedDesc: String,
    private val expectedTags: List<String>,
    private val expectedImages: List<String>,
) : TupperwareService {
    override suspend fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<String>,
    ) {
        assert(expectedTitle == title)
        assert(expectedDesc == desc)
        assert(expectedTags.zip(tags).all { (x, y) -> x == y })
        assert(expectedImages.zip(photos).all { (x, y) -> x == y })
    }
}
