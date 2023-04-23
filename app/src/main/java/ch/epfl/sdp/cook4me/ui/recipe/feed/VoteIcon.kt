package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * Component that shows an upvote/downvote icon (similar to reddit f.ex)
 * @param counterValue the current value of the counter
 * @param onChange the function that will be called when the counter changes, the int parameter is the relative change
 */
@Preview(showBackground = true)
@Composable
fun VoteIcon(counterValue: Int = 0, onChange: (Int) -> Unit = {}) {
    val upvote = remember { mutableStateOf(false) }
    val downvote = remember { mutableStateOf(false) }
    val notPressedColor = Color.Black
    val pressedColor = Color.Red
    val localCounterValue = remember { mutableStateOf(counterValue) }

    /**
     * Function that is called when the user presses the upvote/downvote button
     * it will update the counter and the buttons color accordingly
     * it also calls the onChange function
     * @param isUpVote true if the upvote button was pressed, false if the downvote button was pressed1
     */
    fun onVote(isUpVote: Boolean) {
        val buttonPressed = if (isUpVote) upvote else downvote
        val otherButton = if (isUpVote) downvote else upvote
        val buttonPressedValue = if (isUpVote) 1 else -1
        // second time pressing the button -> revert the vote
        if (buttonPressed.value) {
            onChange(buttonPressedValue * -1)
            buttonPressed.value = false
            localCounterValue.value -= buttonPressedValue
        } else {
            // other button was pressed -> remove the vote from the other button
            if (otherButton.value) {
                onChange(buttonPressedValue * 2)
                otherButton.value = false
                localCounterValue.value += 2 * buttonPressedValue
            } else {
                onChange(buttonPressedValue * 1)
                localCounterValue.value += buttonPressedValue
            }
            buttonPressed.value = true
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onVote(true) }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Upvote",
                tint = if (upvote.value) pressedColor else notPressedColor
            )
        }
        Text(text = localCounterValue.value.toString())
        IconButton(onClick = { onVote(false) }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Downvote",
                tint = if (downvote.value) pressedColor else notPressedColor
            )
        }
    }
}
