package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import com.google.firebase.database.DatabaseReference

const val EMAIL_TEXT_FIELD_TAG = "emailTextField"
const val PHONE_TEXT_FIELD_TAG = "phoneTextField"
const val GET_DB_BUTTON_TAG = "getDbButton"
const val SET_DB_BUTTON_TAG = "setDbButton"

@Composable
fun FirebaseScreen(database: DatabaseReference){
    // add two buttons to get and set values in the database

    var emailTextField by remember {
        mutableStateOf("")
    }

    var phoneTextField by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {
            // get value from database
            database.child(phoneTextField).get().addOnSuccessListener {
                emailTextField = it.value.toString()
            }
        }, modifier = Modifier.testTag(GET_DB_BUTTON_TAG)
        ) {
            Text(stringResource(id = R.string.set_db_button_message))
        }

        Button(onClick = {
            // set value in database
            database.child(phoneTextField).setValue(emailTextField)
        }, modifier = Modifier.testTag(SET_DB_BUTTON_TAG)
        ) {
            Text(stringResource(id = R.string.set_db_button_message))
        }

        // phone number text field
        TextField(placeholder = {
            Text(stringResource(id = R.string.email_input_message))
        }, value = emailTextField, onValueChange = { emailTextField = it },
            modifier = Modifier.testTag(EMAIL_TEXT_FIELD_TAG))

        TextField(placeholder = {
            Text(stringResource(id = R.string.phone_input_message))
        }, value = phoneTextField, onValueChange = { phoneTextField = it },
            modifier = Modifier.testTag(PHONE_TEXT_FIELD_TAG))
    }
}