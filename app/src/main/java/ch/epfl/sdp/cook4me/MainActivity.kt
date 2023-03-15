package ch.epfl.sdp.cook4me

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ch.epfl.sdp.cook4me.application.TupperwareService
import ch.epfl.sdp.cook4me.ui.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

class MockTupperwareService : TupperwareService {
    override suspend fun submitForm(
        title: String,
        desc: String,
        tags: List<String>,
        photos: List<String>,
    ) {
        Log.d("Debug", "$title\n$desc")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mockService = MockTupperwareService()
            Cook4meTheme {
                TupCreationScreenWithState(
                    TupCreationViewModel(service = mockService)
                )
            }
        }
    }
}
