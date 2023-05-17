package ch.epfl.sdp.cook4me.application

import android.util.Log
import ch.epfl.sdp.cook4me.persistence.model.FirestoreTupperware
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import com.google.firebase.auth.FirebaseAuth

class SwipeService(
    private val swipeRepository: SwipeRepository = SwipeRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val tupperwareRepository: TupperwareRepository = TupperwareRepository()
) {

    suspend fun storeSwipeResult(id: String, right: Boolean) {
        swipeRepository.add(id, right)
    }

    suspend fun isMatch(tupperwareId: String): Boolean {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val otherUser = tupperwareRepository.getById<FirestoreTupperware>(tupperwareId)?.user
        checkNotNull(otherUser)
        val swipesFromOther = swipeRepository.getAllPositiveIdsByUser(otherUser)
        val ownTupperwareIds = tupperwareRepository.getAllIdsByUser(email)
        return swipesFromOther.intersect(ownTupperwareIds).isNotEmpty()
    }

    suspend fun getAllUnswipedTupperware(): Map<String, TupperwareWithImage> =
        try {
            val email = auth.currentUser?.email
            checkNotNull(email)
            val allSwipes = swipeRepository.getAllIdsByUser(email)
            val allTupperwareIdsFromOtherUsers = tupperwareRepository.getAllIdsNotByUser(email)
            val toBeSwiped = allTupperwareIdsFromOtherUsers.minus(allSwipes)
            toBeSwiped.mapNotNull { id ->
                val tupperware = tupperwareRepository.getWithImageById(id)
                tupperware?.let {
                    id to it
                }
            }.toMap()
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            Log.e("SwipeService", "exception was thrown", e)
            mapOf()
        }
}
