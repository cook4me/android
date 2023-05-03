package ch.epfl.sdp.cook4me.ui.navigation


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.BottomNavScreen
import ch.epfl.sdp.cook4me.R

val mainScreens = listOf(
    BottomNavScreen.Tupperwares,
    BottomNavScreen.Recipes,
    BottomNavScreen.Events,
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(navigateTo: (String)->Unit = {}, currentRoute: String) {
    val mainDestinations = listOf(
        BottomNavScreen.Tupperwares,
        BottomNavScreen.Recipes,
        BottomNavScreen.Events,
    )

    val dropDownMenuDestinations = listOf(
        BottomNavScreen.MyTupperwares,
        BottomNavScreen.MyRecipes,
        BottomNavScreen.MyEvents,
        BottomNavScreen.Profile,
    )

    var expanded by remember { mutableStateOf(false)}

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {

        mainDestinations.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.weight(1f),
                icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navigateTo(screen.route)
                }
            )
        }
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            onExpandedChange = {expanded = !expanded},
        ) {
            val moreIcon = painterResource(id = R.drawable.baseline_more_horiz_24)
            BottomNavigationItem(
                modifier = Modifier.fillMaxWidth(),
                icon = { Icon(moreIcon, contentDescription = null) },
                label = { Text("More") },
                selected = false,
                onClick = {  },
            )
            ExposedDropdownMenu(
                modifier = Modifier.exposedDropdownSize(false).width(120.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dropDownMenuDestinations.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            navigateTo(item.route)
                        },
                        content = { Text(text = item.title) },
                    )
                }
            }
        }
    }
}

