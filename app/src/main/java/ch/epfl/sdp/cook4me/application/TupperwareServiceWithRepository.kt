package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository

class TupperwareServiceWithRepository(private val repository: TupperwareRepository) : TupperwareService {

    override suspend fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<String>,
    ) {
        repository.add(
            Tupperware(
                title,
                desc,
                tags,
                photos,
            )
        )
    }
}
