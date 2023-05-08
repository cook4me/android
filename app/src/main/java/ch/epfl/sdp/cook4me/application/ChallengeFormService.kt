package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectCollectionRepository
import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import com.google.firebase.firestore.DocumentSnapshot

private const val CHALLENGE_PATH = "challenges"

class ChallengeFormService(
    private val objectRepository: ObjectRepository = ObjectRepository(objectPath = CHALLENGE_PATH),
    private val objectCollectionRepository: ObjectCollectionRepository =
        ObjectCollectionRepository(objectPath = CHALLENGE_PATH)
) {

    suspend fun submitForm(challenge: Challenge): String? = if (challenge.isValidChallenge) {
        objectRepository.add(challenge)
        null
    } else {
        challenge.challengeProblem
    }

    suspend fun getWithGivenField(field: String, query: Any): Map<String, Challenge> {
        val result = objectRepository.getWithGivenField<Challenge>(field, query)
        return result.map { it.id to documentSnapshotToChallenge(it) }.toMap()
    }

    suspend fun getChallengeWithId(id: String): Challenge? {
        val result = objectRepository.getWithId<Challenge>(id)
        return result?.let { documentSnapshotToChallenge(it) }
    }

    suspend fun retrieveAllChallenges(): Map<String, Challenge> {
        val result = objectCollectionRepository.retrieveAllDocuments<Challenge>()
        return result.map { it.id to documentSnapshotToChallenge(it) }.toMap()
    }

    private fun documentSnapshotToChallenge(documentSnapshot: DocumentSnapshot) =
        Challenge(documentSnapshot.data ?: emptyMap())
}
