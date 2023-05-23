package ch.epfl.sdp.cook4me.application

import android.util.Log
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
        val otherUser = getUserByTupperwareId(tupperwareId)
        checkNotNull(otherUser)
        val swipesFromOther = swipeRepository.getAllPositiveIdsByUser(otherUser)
        val ownTupperwareIds = tupperwareRepository.getAllTupperwareIdsAddedByUser(email)
        return swipesFromOther.intersect(ownTupperwareIds).isNotEmpty()
    }

    suspend fun getUserByTupperwareId(tupperwareId: String): String? {
        return tupperwareRepository.getById(tupperwareId)?.user
    }

    suspend fun getAllUnswipedTupperware(): Map<String, TupperwareWithImage> =
        try {
            val email = auth.currentUser?.email
            checkNotNull(email)
            val allSwipes = swipeRepository.getAllIdsByUser(email)
            val allTupperwareIdsFromOtherUsers = tupperwareRepository.getAllTupperwareIdsNotAddedByUser(email)
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
