package ch.epfl.sdp.cook4me.application

// private const val USER_A = "user.a@epfl.ch"
// private const val PASSWORD_A = "password_a"
//
// private const val USER_B = "user.b@epfl.ch"
// private const val PASSWORD_B = "password_b"

// @ExperimentalCoroutinesApi
// @RunWith(AndroidJUnit4::class)
// class SwipeServiceTest {
//
//    private lateinit var store: FirebaseFirestore
//    private lateinit var storage: FirebaseStorage
//    private lateinit var auth: FirebaseAuth
//    private lateinit var tupperwareRepository: TupperwareRepository
//    private lateinit var swipeRepository: SwipeRepository
//    private lateinit var swipeService: SwipeService

//    @Before
//    fun setUp() {
//        store = setupFirestore()
//        storage = setupFirebaseStorage()
//        auth = setupFirebaseAuth()
//        tupperwareRepository = TupperwareRepository(store, storage, auth)
//        swipeRepository = SwipeRepository(store, auth)
//        swipeService = SwipeService(swipeRepository, auth, tupperwareRepository)
//        runBlocking {
//            auth.createUserWithEmailAndPassword(USER_A, PASSWORD_A).await()
//            auth.createUserWithEmailAndPassword(USER_B, PASSWORD_B).await()
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            tupperwareRepository.deleteAll()
//            swipeRepository.deleteAll()
//            signInWithUserA()
//            auth.currentUser?.delete()
//            signInWithUserB()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun noMatchOnNoOverlap() = runTest {
//        val filesA = withContext(Dispatchers.IO) {
//            generateTempFiles(5)
//        }
//        val filesB = withContext(Dispatchers.IO) {
//            generateTempFiles(5)
//        }
//        signInWithUserA()
//        tupperwareRepository.addMultipleTestTupperware(filesA)
//        signInWithUserB()
//        tupperwareRepository.addMultipleTestTupperware(filesB)
//        signInWithUserA()
//        swipeService.getAllUnswipedTupperware().forEach {
//            swipeService.storeSwipeResult(it.key, true)
//            assertThat(swipeService.isMatch(it.key), `is`(false))
//        }
//    }

//    @Test
//    fun matchOnOverlap() = runTest {
//        val filesA = withContext(Dispatchers.IO) {
//            generateTempFiles(5)
//        }
//        val filesB = withContext(Dispatchers.IO) {
//            generateTempFiles(5)
//        }
//        signInWithUserA()
//        tupperwareRepository.addMultipleTestTupperware(filesA)
//        signInWithUserB()
//        tupperwareRepository.addMultipleTestTupperware(filesB)
//        swipeService.getAllUnswipedTupperware().forEach {
//            swipeService.storeSwipeResult(it.key, true)
//        }
//        signInWithUserA()
//        swipeService.getAllUnswipedTupperware().forEach {
//            swipeService.storeSwipeResult(it.key, true)
//            assertThat(swipeService.isMatch(it.key), `is`(true))
//        }
//    }

//    @Test
//    fun getAllUnswipedTupperwareAfterSwipesTest() = runTest {
//        val filesA = withContext(Dispatchers.IO) {
//            generateTempFiles(5)
//        }
//        signInWithUserA()
//        tupperwareRepository.addMultipleTestTupperware(filesA)
//        signInWithUserB()
//        val tupperwareBefore = swipeService.getAllUnswipedTupperware()
//        assertThat(tupperwareBefore.keys.count(), `is`(5))
//        tupperwareBefore.keys.take(3).forEach {
//            swipeService.storeSwipeResult(it, false)
//        }
//        val tupperwareAfter = swipeService.getAllUnswipedTupperware()
//        assertThat(
//            tupperwareAfter.keys,
//            containsInAnyOrder(*tupperwareBefore.keys.drop(3).toTypedArray())
//        )
//    }
//
//    private suspend fun signInWithUserA() =
//        auth.signInWithEmailAndPassword(USER_A, PASSWORD_A).await()
//
//    private suspend fun signInWithUserB() =
//        auth.signInWithEmailAndPassword(USER_B, PASSWORD_B).await()
// }
