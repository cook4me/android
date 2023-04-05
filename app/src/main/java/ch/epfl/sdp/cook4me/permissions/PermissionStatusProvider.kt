package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable

interface PermissionStatusProvider {

    /**
     * Returns whether all permissions required by the app have been granted by the user.
     *
     * @return `true` if all permissions are granted, `false` otherwise.
     */
    @Composable
    fun allPermissionsGranted(): Boolean

    /**
     * Returns whether the user has previously denied permission requests and should be shown an explanation
     * for why the app needs those permissions.
     *
     * @return `true` if the user should be shown an explanation, `false` otherwise.
     */
    @Composable
    fun shouldShowRationale(): Boolean

    /**
     * Returns a list of permissions that have been previously denied by the user and should be explained to the user.
     *
     * @return A list of permissions that have been revoked by the user.
     */
    @Composable
    fun getRevokedPermissions(): List<String>

    /**
     * Requests all required permissions from the user.
     */
    @Composable
    fun requestAllPermissions()
}
