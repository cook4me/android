import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton
import ch.epfl.sdp.cook4me.ui.common.form.EmailField
import ch.epfl.sdp.cook4me.ui.common.form.EmailState
import ch.epfl.sdp.cook4me.ui.common.form.PasswordField
import ch.epfl.sdp.cook4me.ui.common.form.PasswordState
import ch.epfl.sdp.cook4me.ui.common.form.TextFieldState
import ch.epfl.sdp.cook4me.ui.user.signup.Toolbar
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    accountService: AccountService = AccountService(),
    onSuccessfulAccountCreationAndLogin: () -> Unit
) {
    val context = LocalContext.current
    val emailState = remember { EmailState(context.getString(R.string.invalid_email_message)) }
    val passwordState =
        remember { PasswordState(context.getString(R.string.sign_up_screen_invalid_password)) }
    val passwordAgainState =
        remember {
            PasswordState(
                context.getString(R.string.sign_up_screen_password_not_identical)
            )
        }

    var inProgress by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    suspend fun showErrorMessageAndTerminateProgress(errorMessage: String) {
        inProgress = false
        scaffoldState
            .snackbarHostState
            .showSnackbar(errorMessage)
    }

    suspend fun showErrorMessageAndTerminateProgress(stateWithError: TextFieldState) {
        showErrorMessageAndTerminateProgress(stateWithError.errorMessage)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = modifier
                    .testTag(stringResource(R.string.sign_up_screen_tag))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Toolbar(stringResource(R.string.sign_up_screen_top_bar_message))
                Column(Modifier.padding(vertical = 16.dp)) {
                    EmailField(
                        emailState.text,
                        emailState.showErrors(),
                        { emailState.text = it },
                        Modifier
                            .fieldModifier()
                            .testTag(stringResource(R.string.tag_email))
                            .onFocusChanged {
                                emailState.onFocusChange(it.isFocused)
                            }
                    )
                    PasswordField(
                        passwordState.text,
                        passwordState.showErrors(),
                        {
                            passwordState.text = it
                            passwordAgainState.isValid = true
                        },
                        Modifier
                            .fieldModifier()
                            .testTag(stringResource(R.string.tag_password))
                            .onFocusChanged {
                                passwordState.onFocusChange(it.isFocused)
                            }
                    )
                    PasswordField(
                        passwordAgainState.text,
                        passwordAgainState.showErrors(),
                        {
                            passwordAgainState.text = it
                            passwordState.isValid = true
                        },
                        Modifier
                            .fieldModifier()
                            .testTag(stringResource(R.string.tag_password))
                            .onFocusChanged {
                                passwordAgainState.onFocusChange(it.isFocused)
                            },
                        R.string.sign_up_screen_password_again_field
                    )
                    LoadingButton(
                        R.string.btn_continue,
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp)
                            .testTag(stringResource(id = R.string.btn_continue)),
                        inProgress
                    ) {
                        emailState.enableShowErrors()
                        passwordState.enableShowErrors()
                        passwordAgainState.enableShowErrors()
                        scope.launch {
                            inProgress = true
                            if (!emailState.isValid) {
                                showErrorMessageAndTerminateProgress(emailState)
                            } else if (!passwordState.isValid) {
                                showErrorMessageAndTerminateProgress(passwordState)
                            } else if (passwordState.text != passwordAgainState.text) {
                                // this should actually be part of a validator class, ran out of time
                                passwordState.isValid = false
                                passwordAgainState.isValid = false
                                showErrorMessageAndTerminateProgress(passwordAgainState)
                            } else if (accountService.userAlreadyExists(emailState.text)) {
                                emailState.isValid = false
                                showErrorMessageAndTerminateProgress(
                                    context.getString(R.string.sign_up_screen_user_already_exists)
                                )
                            } else {
                                try {
                                    accountService.register(emailState.text, passwordState.text)
                                    accountService.authenticate(emailState.text, passwordState.text)
                                    onSuccessfulAccountCreationAndLogin()
                                } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                                    Log.e("sign up", e.message, e)
                                    showErrorMessageAndTerminateProgress(
                                        context.getString(R.string.sign_up_screen_firebase_exception)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun Modifier.fieldModifier(): Modifier =
    this
        .fillMaxWidth()
        .padding(16.dp, 4.dp)
