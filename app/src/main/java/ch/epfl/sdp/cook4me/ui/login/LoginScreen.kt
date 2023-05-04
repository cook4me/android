package ch.epfl.sdp.cook4me.ui.login

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.graphics.Color
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
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    accountService: AccountService = AccountService(),
    onSuccessfulLogin: () -> Unit
) {
    val context = LocalContext.current
    val emailState = remember { EmailState(context.getString(R.string.invalid_email_message)) }
    val passwordState =
        remember { RequiredTextFieldState(context.getString(R.string.password_blank)) }
    var inProgress by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    BasicToolbar(stringResource(R.string.sign_in_screen_top_bar_message))

    Scaffold(
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = modifier
                    .testTag(stringResource(R.string.Login_Screen_Tag))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailField(
                    emailState.text,
                    emailState.showErrors(),
                    { emailState.text = it },
                    Modifier
                        .fieldModifier()
                        .onFocusChanged {
                            emailState.onFocusChange(it.isFocused)
                        }
                )
                PasswordField(
                    passwordState.text,
                    passwordState.showErrors(),
                    { passwordState.text = it },
                    Modifier
                        .fieldModifier()
                        .onFocusChanged {
                            passwordState.onFocusChange(it.isFocused)
                        }
                )
                LoadingButton(
                    R.string.sign_in_screen_sign_in_button,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp),
                    inProgress
                ) {
                    emailState.enableShowErrors()
                    passwordState.enableShowErrors()
                    scope.launch {
                        if (!emailState.isValid) {
                            scaffoldState
                                .snackbarHostState
                                .showSnackbar(emailState.errorMessage)
                        } else if (!passwordState.isValid) {
                            scaffoldState.snackbarHostState.showSnackbar(passwordState.errorMessage)
                        } else {
                            try {
                                inProgress = true
                                accountService.authenticate(emailState.text, passwordState.text)
                                onSuccessfulLogin()
                            } catch (e: FirebaseAuthInvalidUserException) {
                                inProgress = false
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_in_screen_non_exist_user))
                                Log.d(
                                    context.getString(R.string.sign_in_screen_non_exist_user),
                                    e.stackTraceToString()
                                )
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                inProgress = false
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_in_screen_wrong_password))
                                inProgress = false
                                Log.d(
                                    context.getString(R.string.sign_in_screen_wrong_password),
                                    e.stackTraceToString()
                                )
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

@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
