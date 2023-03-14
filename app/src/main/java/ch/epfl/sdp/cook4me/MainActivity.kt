package ch.epfl.sdp.cook4me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.application.TupperwareService
import ch.epfl.sdp.cook4me.ui.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel
import ch.epfl.sdp.cook4me.ui.TupCreationViewModelFactory
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

class MockTupperwareService(
    private val expectedTitle: String,
    private val expectedDesc: String,
    private val expectedTags: List<String>,
    private val expectedImages: List<String>,
) : TupperwareService {
    override suspend fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<String>,
    ) {
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mockService = MockTupperwareService("", "", listOf(), listOf())
            Cook4meTheme {
                TupCreationScreenWithState(
                    TupCreationViewModel(service = mockService)
                )
            }
        }
    }
}
