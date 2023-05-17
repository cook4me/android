package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class ListItemState(
    var isSelected: Boolean = false
    ) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen() {
    val filterCategories = listOf("Category 1", "Category 2", "Category 3")
    val sortOptions = listOf("Sort Option 1", "Sort Option 2", "Sort Option 3")
    val (expandedCategory, setExpandedCategory) = remember { mutableStateOf<String?>(null) }
    val (selectedSortOption, setSelectedSortOption) = remember { mutableStateOf<String?>(null) }

    Column(modifier =
        Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Filter") },
            navigationIcon = {
                IconButton(onClick = { /* Handle navigation */ }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            },
            actions = {
                TextButton(onClick = { /* Handle reset */ }) {
                    Text(text = "Reset")
                }
            }
        )

        Text(text = "Filter by", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold))
        filterCategories.forEach { category ->
            ListItem(
                text = { Text(text = category, style = MaterialTheme.typography.h6)},
                modifier = Modifier.clickable { setExpandedCategory(category) }
            )

            if (expandedCategory == category) {
                // Display a list of filters for this category
                val filters = listOf("Filter 1", "Filter 2", "Filter 3")
                filters.forEach { filter ->
                    ListItem(
                        text = { Text(text = filter) },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        Divider(thickness = 2.dp)
        Text(text = "Sort by", style = MaterialTheme.typography.h6)
        sortOptions.forEach { sortOption ->
            ListItem(
                text = { Text(text = sortOption) },
                trailing = {
                    if (selectedSortOption == sortOption) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colors.secondary)
                    }
                },
                modifier = Modifier.clickable { setSelectedSortOption(sortOption) }
            )
        }
    }
}

