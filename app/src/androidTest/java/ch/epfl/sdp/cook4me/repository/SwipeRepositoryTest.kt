package ch.epfl.sdp.cook4me.repository

// private const val USER_NAME = "john.snow@epfl.ch"
// private const val PASSWORD = "ygritte"
// @ExperimentalCoroutinesApi
// class SwipeRepositoryTest {
//    private lateinit var swipeRepository: SwipeRepository
//    private lateinit var store: FirebaseFirestore
//    private lateinit var auth: FirebaseAuth
//
//    @Before
//    fun setUp() {
//        store = setupFirestore()
//        auth = setupFirebaseAuth()
//        swipeRepository = SwipeRepository(store, auth)
//        runBlocking {
//            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
//            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            swipeRepository.deleteAll()
//            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
//            auth.currentUser?.delete()
//        }
//    }

// TODO: check flaky test
//    @Test
//    fun addIdsAndGetThemTest() = runTest {
//        swipeRepository.add("tup1", false)
//        swipeRepository.add("tup2", true)
//        swipeRepository.add("tup3", true)
//        val ids = swipeRepository.getAllIdsByUser(auth.currentUser?.email ?: error("shouldn't happen"))
//        assertThat(ids, containsInAnyOrder("tup1", "tup2", "tup3"))
//    }

//    @Test
//    fun getAllPositiveIdsTest() = runTest {
//        swipeRepository.add("id1", false)
//        swipeRepository.add("id2", true)
//        swipeRepository.add("id3", true)
//        swipeRepository.add("id4", false)
//        val ids = swipeRepository.getAllPositiveIdsByUser(auth.currentUser?.email ?: error("shouldn't happen"))
//        assertThat(ids, containsInAnyOrder("id2", "id3"))
//    }
// }
