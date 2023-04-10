import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton
import ch.epfl.sdp.cook4me.ui.common.form.EmailField
import ch.epfl.sdp.cook4me.ui.common.form.EmailState
import ch.epfl.sdp.cook4me.ui.common.form.PasswordField
import ch.epfl.sdp.cook4me.ui.common.form.RequiredTextFieldState
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    accountService: AccountService = AccountService(),
    onSuccessfullSignUp: () -> Unit,
    signUpViewModel: SignUpViewModel = SignUpViewModel(),
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

    BasicToolbar(stringResource(R.string.sign_up_screen_top_bar_message))
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
                                if(!accountService.isValidEmail(email = emailState.text)){
                                    throw FirebaseAuthEmailException("email","isMalFormed")
                                }
                                if(!accountService.isValidPassword(password = passwordState.text)){
                                    throw FirebaseAuthException("password","isMalFormed")
                                }
                                signUpViewModel.addPassword(password = passwordState.text)
                                signUpViewModel.addEmail(email = emailState.text)
                                onSuccessfullSignUp()
                            } catch (e: FirebaseAuthEmailException) {
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_up_screen_invalid_email))
                                Log.d(
                                    context.getString(R.string.sign_up_screen_invalid_email),
                                    e.stackTraceToString()
                                )
                            } catch (e: FirebaseAuthException) {
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_up_screen_invalid_password))
                                inProgress = false
                                Log.d(
                                    context.getString(R.string.sign_up_screen_invalid_password),
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

@Composable
fun Password_signUpScreen() {
    var password by rememberSaveable { mutableStateOf("") }

    input_row {
        Text(stringResource(R.string.tag_password), modifier = Modifier.width(100.dp))
        TextField(
            value = password,
            modifier = Modifier.testTag(stringResource(R.string.tag_password)),
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.default_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = colorsTextfield_SignUpScreen()
        )
    }
}

@Composable
fun columnTextBtn_SignUpScreen(
    displayLabel: String,
    defaultText: String
) {
    var textInputVariable by rememberSaveable {
        mutableStateOf("")
    }

    input_row {
        Text(
            text = displayLabel,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            placeholder = {
                Text(
                    defaultText
                )
            },
            value = textInputVariable,
            modifier = Modifier.testTag(displayLabel),
            onValueChange = { textInputVariable = it },
            colors = colorsTextfield_SignUpScreen()
        )
    }
}
@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant


@Composable
fun colorsTextfield_SignUpScreen(): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        textColor = Color.Black
    )

@Composable
private fun continueBack_buttons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text_buttons({}, nameBtn = stringResource(R.string.btn_back))

        text_buttons({}, nameBtn = stringResource(R.string.btn_continue))
    }
}

@Composable
private fun text_buttons(onClick: () -> Unit, nameBtn: String) {
    Text(
        text = nameBtn,
        modifier = Modifier
            .testTag(nameBtn)
            .clickable(onClick = { onClick() })
    )
}

@Composable
private fun input_row(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun CustomTitleText(text: String = "") {
    Text(
        modifier = Modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}


private fun Modifier.fieldModifier(): Modifier =
    this
        .fillMaxWidth()
        .padding(16.dp, 4.dp)