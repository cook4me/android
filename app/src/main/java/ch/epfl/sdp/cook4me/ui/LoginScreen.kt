package ch.epfl.sdp.cook4me.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.util.PasswordField
import ch.epfl.sdp.cook4me.util.EmailField
import ch.epfl.sdp.cook4me.util.fieldModifier
import ch.epfl.sdp.cook4me.util.BasicToolbar
import ch.epfl.sdp.cook4me.util.basicButton
import ch.epfl.sdp.cook4me.util.BasicButton
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = LoginViewModel(),
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BasicToolbar(stringResource(R.string.sign_in_screen_top_bar_message))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(email, {email = it}, Modifier.fieldModifier())
        PasswordField(password, {password = it}, Modifier.fieldModifier())

        BasicButton(stringResource(R.string.sign_in_screen_sign_in_button), Modifier.basicButton()) {
            scope.launch {
                if (!viewModel.isEmailValid(email)) {
                    Toast.makeText(context, context.getString(R.string.invalid_email_message), Toast.LENGTH_LONG)
                        .show()
                }
                else if (viewModel.isPasswordBlank(password)) {
                    Toast.makeText(context, context.getString(R.string.password_blank), Toast.LENGTH_LONG)
                        .show()
                } else {
                    try {
                        viewModel.onSignInClick(email, password)
                        Toast.makeText(context, context.getString(R.string.sign_in_screen_sign_in_success), Toast.LENGTH_LONG)
                            .show()
                    } catch(e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(context, context.getString(R.string.sign_in_screen_non_exist_user), Toast.LENGTH_LONG).show()
                    } catch(e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context, context.getString(R.string.sign_in_screen_wrong_password), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        /*TODO:*/
//        BasicTextButton("Forgot password? Click to recovery email", Modifier.textButton()) {
//            viewModel.onForgotPasswordClick()
//        }
    }
}