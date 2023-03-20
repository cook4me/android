package ch.epfl.sdp.cook4me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ch.epfl.sdp.cook4me.ui.recipeform.RecipeCreationScreen
import ch.epfl.sdp.cook4me.ui.recipeform.RecipeCreationViewModel
import ch.epfl.sdp.cook4me.ui.simpleComponent.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.GenericSeparators
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import ch.epfl.sdp.cook4me.ui.tupperwareform.TupCreationScreenWithState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val text = remember { mutableStateOf("") }
            Cook4meTheme {
                Column {
                    RecipeCreationScreen(viewModel = RecipeCreationViewModel())
                }
            }
        }
    }
}
