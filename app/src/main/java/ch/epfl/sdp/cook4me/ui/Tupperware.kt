package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickImage: () -> Unit,
    images: List<FoodImage>,

) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        items(images) { image ->
            ImageElement(image, onClickImage)
        }
        item {
            AddPictureElement(onClickAddImage)
        }
    }

}

@Composable
fun AddPictureElement(
    onClickAddImage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClickAddImage },
        border = BorderStroke(1.dp, MaterialTheme.colors.secondary, ) ,

    ) {
        Text(
            text = stringResource(R.string.image_selector_add_image),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageElement(
    image: FoodImage,
    onClickPhoto: () -> Unit
) {

}

class Image {

}

class FoodImage {

}

@Preview("default")
@Composable
fun AddImagePreview() {
    Cook4meTheme {
        AddPictureElement {
        }
    }
}