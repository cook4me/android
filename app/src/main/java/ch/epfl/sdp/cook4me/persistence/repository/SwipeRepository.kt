package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "swipes"

class SwipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val tupperwareRepository: TupperwareRepository = TupperwareRepository(),
//    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    ObjectRepository(store, COLLECTION_PATH) {

    suspend fun addSwipe(tupperwareId: String, liked: Boolean) {
        store.collection(COLLECTION_PATH).document(tupperwareId).set(liked).await()
    }

    // need to check if it contains any of my matches
    suspend fun checkIsMatch(tupperwareId: String) {

    }

    suspend fun getAllUnswipedTupperwares() {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val swipes = super.getAll<Boolean>()
        val tupperwareFromOtherPeople = tupperwareRepository.getAllExceptFrom(email)
        val toBeSwiped = tupperwareFromOtherPeople.filterNot { swipes.containsKey(it.key) }
//        val image =
    }
}