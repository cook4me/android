package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.BoredApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

@get:Rule
val composeTestRule = createAndroidComposeRule<ComponentActivity>()

@RunWith(AndroidJUnit4::class)
class BoredActivityScreenTest {
    private val mockWebServer = MockWebServer()
    private lateinit var boredApi: BoredApi


    @Before
    fun setup() {
        mockWebServer.start()
        boredApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BoredApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    /*@Test
    fun testSuccessfulResponse() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{\"activity\":\"Learn the Chinese erhu\",\"type\":\"music\",\"participants\":1,\"price\":0.6,\"link\":\"\",\"key\":\"2742452\",\"accessibility\":0.4}")

        mockWebServer.enqueue(response)
        val activity = boredApi.getActivity().enqueue(object : Callback<BoredActivity> {
            override fun onResponse(call: Call<BoredActivity>, response: Response<BoredActivity>) {
                val act = response.body()?.activity
                assert(act=="Learn the Chinese erhu")
            }

            override fun onFailure(call: Call<BoredActivity>, t: Throwable) {
                // TODO: Handle the error
            }
        }

    }*/
}
