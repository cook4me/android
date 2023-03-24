package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.Event
import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository

class EventFormService(private val objectRepository: ObjectRepository = ObjectRepository()) {

    suspend fun submitForm(event: Event): String? {
        return if (event.isValidEvent){
            objectRepository.add(event)
            null
        } else {
            event.eventProblem
        }
    }
}