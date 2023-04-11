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
package ch.epfl.sdp.cook4me.ui.common.form

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorMsg: String,
    private val default: String = "",
) : FormElementState {
    var text: String by mutableStateOf(default)

    // was the TextField ever focused
    private var displayErrors: Boolean by mutableStateOf(false)
    private var isFocused: Boolean by mutableStateOf(false)
    var isFocusedDirty: Boolean by mutableStateOf(false)

    override val isValid: Boolean
        get() = validator(text)

    override val errorMessage: String
        get() = errorMsg

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
        if (!isFocused && isFocusedDirty) {
            enableShowErrors()
        }
    }

    override fun enableShowErrors() {
        displayErrors = true
    }

    override fun showErrors() = !isValid && displayErrors
}

class RequiredTextFieldState(errorMsg: String, default: String = "",) : TextFieldState(
    { it.isNotBlank() },
    errorMsg,
    default
)

class EmailState(errorMsg: String, default: String = "",) : TextFieldState(
    {
        it.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
    },
    errorMsg,
    default,
)
