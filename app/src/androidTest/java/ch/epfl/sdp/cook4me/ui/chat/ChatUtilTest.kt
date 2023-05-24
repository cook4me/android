package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatUtilTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val mockLoader: ImageLoader = mockk(relaxed = true)
    private val mockRepo: ProfileImageRepository = mockk(relaxed = true)
    private lateinit var decorator: ImageLoaderDecorator
    @Before
    fun setup() {
        decorator = ImageLoaderDecorator(mockLoader, mockRepo)
    }

    @Test
    fun executeLoadsFromRepositoryWhenPrefixMatches() = runTest {
        val prefixedData = "$PROFILE_IMAGE_PREFIX:123"
        val profileImage = "imageData"
        val mockRequest = ImageRequest.Builder(mockk(relaxed = true), context=context)
            .data(prefixedData)
            .build()
        val mockResult = SuccessResult(
            drawable = mockk(relaxed = true),
            request = mockRequest,
        dataSource = mockk(relaxed = true)
        )

        coEvery { mockRepo.getProfile("123") } returns Uri.EMPTY
        coEvery { mockLoader.execute(any()) } returns mockResult

        val result = decorator.execute(mockRequest)

        Assert.assertEquals(mockResult, result)
    }

    @Test
    fun executeLoadsNormallyWhenPrefixDoesNotMatch() = runTest {
        val notPrefixedData = "123"
        val mockRequest = ImageRequest.Builder(mockk(relaxed = true),context=context)
            .data(notPrefixedData)
            .build()
        val mockResult = SuccessResult(
            drawable = mockk(relaxed = true),
            request = mockRequest,
            dataSource = mockk(relaxed = true)
        )

        coEvery { mockLoader.execute(mockRequest) } returns mockResult

        val result = decorator.execute(mockRequest)

        Assert.assertEquals(mockResult, result)
    }


}