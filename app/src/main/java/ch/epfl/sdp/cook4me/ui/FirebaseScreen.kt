package ch.epfl.sdp.cook4me.ui

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.Cook4MeScreen
import ch.epfl.sdp.cook4me.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun FirebaseScreen(){
    // add two buttons to get and set values in the database

    val db = Firebase.database.reference

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
            db.child(phoneTextField).get().addOnSuccessListener {
                emailTextField = it.value.toString()
            }
        }) {
            Text(stringResource(id = R.string.set_db_button_message))
        }

        Button(onClick = {
            // set value in database
            db.child(phoneTextField).setValue(emailTextField)
        }) {
            Text(stringResource(id = R.string.set_db_button_message))
        }

        // phone number text field
        TextField(placeholder = {
            Text(stringResource(id =R.string.email_input_message))
        }, value = emailTextField, onValueChange = { emailTextField = it })

        TextField(placeholder = {
            Text(stringResource(id = R.string.phone_input_message))
        }, value = phoneTextField, onValueChange = { phoneTextField = it })
    }
}