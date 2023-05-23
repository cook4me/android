package ch.epfl.sdp.cook4me.ui.challenge.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterScreen(
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
    viewModel: SearchViewModel = remember { SearchViewModel() }
) {
    val selectedSortOption = viewModel.chosenSortOption
    val sortOptions = viewModel.sortOptions
    val selectedFilters = viewModel.selectedFilters
    val filterCategories = viewModel.filters

    FilterUI(
        onCancelClick = {
            onCancelClick()
            viewModel.cancelChoice()
        },
        onDoneClick = {
            viewModel.confirmChoice()
            onDoneClick()
        },
        onResetClick = { viewModel.resetFilters() },
        onFilterOptionSelect = { viewModel.selectFilterOption(it) },
        onSortSelect = { selectedSortOption.value = it },
        filterCategories = filterCategories,
        sortOptions = sortOptions,
        selectedFilters = selectedFilters,
        selectedSortOption = selectedSortOption.value
    )
}

@Composable
fun FilterUI(
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
    onResetClick: () -> Unit,
    onFilterOptionSelect: (String) -> Unit,
    onSortSelect: (String) -> Unit,
    sortOptions: List<String>,
    filterCategories: Map<String, List<String>>,
    selectedSortOption: String,
    selectedFilters: List<String>,
) {
    val (expandedCategory, setExpandedCategory) = remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    fun expandFilterCategory(filterOption: String) {
        if (expandedCategory == filterOption) {
            setExpandedCategory(null)
        } else {
            setExpandedCategory(filterOption)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TopBar(
            onCancelClick = onCancelClick,
            onDoneClick = onDoneClick,
            onResetClick = onResetClick,
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Filter by", style = MaterialTheme.typography.h5)
            filterCategories.forEach { category ->
                FilterCategory(
                    category = category.key,
                    isExpanded = category.key == expandedCategory,
                    selectedFilters = selectedFilters,
                    filterOptions = category.value,
                    onExpand = { expandFilterCategory(category.key) },
                    onFilterOptionClick = onFilterOptionSelect
                )
            }
            // Divider(thickness = 0.5.dp, color = Color.Black)
            Text(text = "Sort by", style = MaterialTheme.typography.h5)
            sortOptions.forEach { sortOption ->
                SortCategory(
                    sortOption = sortOption,
                    isSelected = selectedSortOption == sortOption,
                    onSelect = onSortSelect
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterCategory(
    category: String,
    isExpanded: Boolean,
    filterOptions: List<String>,
    selectedFilters: List<String>,
    onExpand: () -> Unit,
    onFilterOptionClick: (String) -> Unit
) {
    ListItem(
        text = { Text(category) },
        modifier = Modifier.clickable { onExpand() },
        icon = {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = if (isExpanded) "$category Expanded" else "$category Not Expanded"
            )
        }
    )

    if (isExpanded) {
        // Display a list of filters for this category
        filterOptions.forEach { filter ->
            FilterValue(
                filterOption = filter,
                onClick = { onFilterOptionClick(it) },
                isSelected = selectedFilters.contains(filter)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterValue(filterOption: String, isSelected: Boolean, onClick: (String) -> Unit) {
    ListItem(
        text = { Text(filterOption, Modifier.padding(start = 16.dp)) },
        modifier = Modifier.clickable { onClick(filterOption) },
        trailing = {
            if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "$filterOption selected")
            }
        }
    )
}

@Composable
fun TopBar(
    onCancelClick: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = "Filter") },
        navigationIcon = {
            IconButton(onClick = onCancelClick) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        },
        actions = {
            TextButton(onClick = onResetClick, colors = ButtonDefaults.buttonColors()) {
                Text(text = "Reset")
            }
            TextButton(
                onClick = onDoneClick,
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = "Done")
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SortCategory(sortOption: String, isSelected: Boolean, onSelect: (String) -> Unit) {
    ListItem(
        text = { Text(sortOption) },
        modifier = Modifier.clickable { onSelect(sortOption) },
        trailing = {
            if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "$sortOption selected")
            }
        }
    )
}
