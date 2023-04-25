package ch.epfl.sdp.cook4me.permissions

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import ch.epfl.sdp.cook4me.R

class PermissionManager(
    private val permissionStatusProvider: PermissionStatusProvider
) {
    @Composable
    fun WithPermission(content: @Composable () -> Unit) {
        HandlePermissionStates(content)
    }

    @Composable
    private fun HandlePermissionStates(
        content: @Composable () -> Unit
    ) {
        val context = LocalContext.current
        var permissionsRequested by remember { mutableStateOf(false) }
        if (permissionStatusProvider.allPermissionsGranted()) {
            content()
        } else {
            if (permissionsRequested) {
                permissionStatusProvider.RequestAllPermissions()
            }
            Column {
                Text(
                    getPermissionText(
                        context,
                        permissionStatusProvider.getRevokedPermissions(),
                        permissionStatusProvider.shouldShowRationale()
                    )
                )
                Button(onClick = { permissionsRequested = true }) {
                    Text("Request permissions")
                }
            }
        }
    }

    private fun getPermissionText(context: Context, permissions: List<String>, shouldShowRationale: Boolean): String {
        val permissionMessageSingular =
            context.getString(R.string.permission_message_singular)
        val permissionMessagePlural =
            context.getString(R.string.permission_message_plural)
        val permissionMessageSingularRecommendation =
            context.getString(R.string.permission_message_singular_recommendation)
        val permissionMessagePluralRecommendation =
            context.getString(R.string.permission_message_plural_recommendation)

        val permissionCount = permissions.size
        if (permissionCount == 0) return ""

        val permissionText = if (permissionCount == 1) {
            String.format(permissionMessageSingular, permissions[0])
        } else {
            String.format(permissionMessagePlural, permissions.joinToString())
        }

        val permissionRecommendationText = if (permissionCount == 1) {
            String.format(permissionMessageSingularRecommendation, permissions[0])
        } else {
            String.format(permissionMessagePluralRecommendation, permissions.joinToString())
        }

        return if (shouldShowRationale) {
            permissionText
        } else {
            permissionRecommendationText
        }
    }
}
