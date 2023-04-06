package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable

class TestPermissionStatusProvider(private val initialPermissions: Map<String, Boolean>) : PermissionStatusProvider {

    private val permissionsMap: MutableMap<String, Boolean> = initialPermissions.toMutableMap()

    @Composable
    override fun allPermissionsGranted(): Boolean {
        return permissionsMap.all { it.value }
    }

    @Composable
    override fun shouldShowRationale(): Boolean {
        return true
    }

    @Composable
    override fun getRevokedPermissions(): List<String> {
        return permissionsMap.filter { !it.value }.map { it.key }
    }

    @Composable
    override fun requestAllPermissions() {
        permissionsMap.replaceAll { _, _ -> true }
    }

    fun setPermissionValue(permission: String, value: Boolean) {
        permissionsMap[permission] = value
    }
}
