package ch.epfl.sdp.cook4me.ui.recipe

import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.permissions.PermissionManager
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository

@Composable
fun CreateRecipePermissionWrapper(
    permissionStatusProvider: PermissionStatusProvider,
    repository: RecipeRepository = RecipeRepository(),
    onCancelClick: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = {}
) {
    val permissionManager = PermissionManager(permissionStatusProvider)
    permissionManager.WithPermission {
        CreateRecipeScreen(
            repository = repository,
            onCancelClick = onCancelClick,
            onSuccessfulSubmit = onSuccessfulSubmit
        )
    }
}
