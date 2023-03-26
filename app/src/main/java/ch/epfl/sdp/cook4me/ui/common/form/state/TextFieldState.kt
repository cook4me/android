/* copied from https://github.com/android/compose-samples/blob/main/Jetsurvey/app/src/main/java/com/example/compose/jetsurvey/signinsignup/TextFieldState.kt
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.epfl.sdp.cook4me.ui.common.form.state

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ch.epfl.sdp.cook4me.R

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorMessage: (Context) -> String
) {
    var text: String by mutableStateOf("")

    // was the TextField ever focused
    var isFocusedDirty: Boolean by mutableStateOf(false)
    var isFocused: Boolean by mutableStateOf(false)
    private var displayErrors: Boolean by mutableStateOf(false)

    val isValid: Boolean
        get() = validator(text)

    fun getErrorMessage(context: Context) = errorMessage(context)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun enableShowErrors() {
        // only show errors if the text was at least once focused
        if (isFocusedDirty) {
            displayErrors = true
        }
    }

    fun showErrors() = !isValid && displayErrors
}

class EmailState : TextFieldState({
    it.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
},
    { it.getString(R.string.invalid_email_message) }
)

const val MIN_PASSWORD_LENGTH = 6

class PasswordState :
    TextFieldState(
        { it.length > MIN_PASSWORD_LENGTH },
        { it.getString(R.string.invalid_email_message) })
