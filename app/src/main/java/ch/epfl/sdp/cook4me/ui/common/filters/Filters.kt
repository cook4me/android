package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "Filter"
        )
        Text(text = stringResource(R.string.filter_button_text))
    }
}


class Filter<A: Any>(
    private val conditions: List<(A) -> Boolean> = listOf()
) {
    fun <A> addCondition(condition: (A) -> Boolean) {
        val list = listOf(condition)
    }

    /**
     *  returns a list containing only elements where one condition of the Filter class was true
     */
    fun filter(list: List<A>) =
        list.filter {elem ->
            conditions.map {it(elem)}.any()
        }
}