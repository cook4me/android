package ch.epfl.sdp.cook4me.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SwipeServiceTest {

//    @Before
//    fun setUp() {
//        store = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setHost("10.0.2.2:8080") // connect to local firestore emulator
//            .setSslEnabled(false)
//            .setPersistenceEnabled(false)
//            .build()
//        store.firestoreSettings = settings
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        storage = FirebaseStorage.getInstance()
//        storage.useEmulator("10.0.2.2", 9199)
//        auth = FirebaseAuth.getInstance()
//        tupperwareRepository = TupperwareRepository(store, storage, auth)
//        runBlocking {
//            auth.createUserWithEmailAndPassword(USER_NAME, "123456").await()
//            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
//        }
//    }

    //TODO: test match
    @Test
    fun shouldMatch() = runTest {
        //userA
        //userB

    }
    
}