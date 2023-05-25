package ch.epfl.sdp.cook4me.permissions

import android.content.Context
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
            val permissionText = getPermissionText(
                context,
                permissionStatusProvider.getRevokedPermissions(),
                permissionStatusProvider.shouldShowRationale()
            )
            PermissionRequesterScreen(
                permissionText = permissionText,
                onClick = { permissionsRequested = true }
            )
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

        val formattedPermissions = permissions.map { formatPermission(it) }

        val permissionText = if (permissionCount == 1) {
            String.format(permissionMessageSingular, formattedPermissions[0])
        } else {
            String.format(permissionMessagePlural, formattedPermissions.joinToString())
        }

        val permissionRecommendationText = if (permissionCount == 1) {
            String.format(permissionMessageSingularRecommendation, formattedPermissions[0])
        } else {
            String.format(permissionMessagePluralRecommendation, formattedPermissions.joinToString())
        }

        return if (shouldShowRationale) {
            permissionText
        } else {
            permissionRecommendationText
        }
    }

    private fun formatPermission(permission: String): String {
        val reformatted = permission.split(".").last().replace("_", " ").lowercase()
        return reformatted.replaceFirstChar { char -> if (char.isLowerCase()) char.uppercase() else char.toString() }
    }
}
