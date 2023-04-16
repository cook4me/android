package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable

class TestPermissionStatusProvider(
    private val initialPermissions: Map<String, Pair<Boolean, Boolean>>
) : PermissionStatusProvider {

    private val permissionsMap: MutableMap<String, Pair<Boolean, Boolean>> = initialPermissions.toMutableMap()

    @Composable
    override fun allPermissionsGranted(): Boolean {
        return permissionsMap.all { it.value.first }
    }

    @Composable
    override fun shouldShowRationale(): Boolean {
        return permissionsMap.any { it.value.second }
    }

    @Composable
    override fun getRevokedPermissions(): List<String> {
        return permissionsMap.filter { !it.value.first }.map { it.key }
    }

    @Composable
    override fun requestAllPermissions() {
        permissionsMap.replaceAll { _, v -> Pair(true, v.second) }
    }

    fun setPermissionValue(permission: String, value: Boolean) {
        permissionsMap[permission]?.let { currentValue ->
            permissionsMap[permission] = Pair(value, currentValue.second)
        }
    }
}
