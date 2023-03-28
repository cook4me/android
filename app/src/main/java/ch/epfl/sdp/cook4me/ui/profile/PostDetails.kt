package ch.epfl.sdp.cook4me.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Post
import ch.epfl.sdp.cook4me.ui.theme.Purple500

@Composable
fun PostDetails(
    painter: Painter,
    data: Post,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Purple500),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.post_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.padding(1.dp))

        Image(
            painter = painter,
            contentDescription = "PostImage",
            modifier = Modifier.testTag(stringResource(R.string.post_tag_image)),
        )

        Spacer(modifier = Modifier.padding(10.dp))
        // data.undertitle,
        Text(
            text = data.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(1.dp))
        // data.desc,
        Text(
            text = data.desc,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(6.dp),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}
