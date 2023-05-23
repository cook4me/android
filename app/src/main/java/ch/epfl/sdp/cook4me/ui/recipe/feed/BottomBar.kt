package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R

/**
 * Displays a bottom bar where user can choose between top recipes or most recent recipes
 * @param onButtonClicked: callback function that is called when a button is clicked,
 * it takes a boolean as parameter, true if top recipes button is clicked, false otherwise
 */
@Preview(showBackground = true)
@Composable
fun BottomBar(onButtonClicked: (Boolean) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(MIDDLE_SPACE_RATIO)
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .border(1.dp, Color.Black)
                .clickable(
                    onClick = {
                        onButtonClicked(true)
                    }
                )
        ) {
            Text(
                text = stringResource(R.string.get_top_recipes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .border(1.dp, Color.Black)
                .clickable(
                    onClick = {
                        onButtonClicked(false)
                    }
                )
        ) {
            Text(
                text = stringResource(R.string.get_recent_recipes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}
