package ch.epfl.sdp.cook4me.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.epfl.sdp.cook4me.ui.data.Constant.ServerClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

fun getGoogleSignInOptions(): GoogleSignInOptions =
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(ServerClient)
        .build()

@Composable
fun createGoogleSignInLauncher(context: Context, viewModel: SignInViewModel): ActivityResultLauncher<Intent> {
    return rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val result = account.getResult(ApiException::class.java)
            val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
            viewModel.googleSignIn(credentials)
        } catch (it: ApiException) {
            print(it)
        }
    }
}
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = createGoogleSignInLauncher(context = context, viewModel = viewModel)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            launcher.launch(GoogleSignIn.getClient(context, getGoogleSignInOptions()).signInIntent)
        }) { Text(text = "Sign in with Google") }
        LaunchedEffect(key1 = viewModel.googleState.value.success) {
            scope.launch {
                if (viewModel.googleState.value.success != null) {
                    Toast.makeText(context, "sign in success！", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
