package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    val notPressedColor = MaterialTheme.colors.onSurface
    val pressedColor = MaterialTheme.colors.primary

    fun onVote(isUpVote: Boolean) {
        val buttonPressed = if (isUpVote) upvote else downvote
        val otherButton = if (isUpVote) downvote else upvote
        val buttonPressedValue = if (isUpVote) 1 else -1
        if (buttonPressed.value) {
            onChange(buttonPressedValue * -1)
            buttonPressed.value = false
        } else {
            if (otherButton.value) {
                onChange(buttonPressedValue*2)
                otherButton.value = false
            } else {
                onChange(buttonPressedValue*1)
            }
            buttonPressed.value = true
        }
    }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = {onVote(true)}) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Upvote",
                tint = if (upvote.value) pressedColor else notPressedColor
            )
        }
        Text(text = counterValue.toString())
        IconButton(onClick = {onVote(false)}) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Downvote",
                tint = if (downvote.value) pressedColor else notPressedColor
            )
        }
    }
}