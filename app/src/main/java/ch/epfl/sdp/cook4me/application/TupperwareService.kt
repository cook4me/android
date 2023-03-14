package ch.epfl.sdp.cook4me.application

interface TupperwareService {
    suspend fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<String>,
    )
}
