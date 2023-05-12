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

    suspend fun isMatch(tupperwareId: String): Boolean {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val other = tupperwareRepository.getById<FirestoreTupperware>(tupperwareId)?.user
        checkNotNull(other)
        val swipesFromOther = swipeRepository.getAllPositiveIdsByUser(other)
        val ownTupperwareIds = tupperwareRepository.getAllIdsByUser(email)
        return swipesFromOther.intersect(ownTupperwareIds).isNotEmpty()
    }

    suspend fun getAllUnswipedTupperware(): Map<String, TupperwareWithImage?> =
        try {
            val email = auth.currentUser?.email
            checkNotNull(email)
            val allSwipes = swipeRepository.getAllIdsByUser(email)
            val allTupperwareIdsFromOtherUsers = tupperwareRepository.getAllIdsNotByUser(email)
            val toBeSwiped = allTupperwareIdsFromOtherUsers.minus(allSwipes)
            toBeSwiped.associateWith {
                tupperwareRepository.getWithImageById(it)
            }
        } catch (e: Exception) {
            Log.e("SwipeService", "exception was thrown", e)
            mapOf()
        }

}