package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    onClickImage: () -> Unit,
    images: List<Uri> = listOf(),
    imageHeight: Dp = 200.dp,
    imageWidth: Dp = 100.dp,
) {

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(images) { img ->
            ImageCard(modifier=Modifier.height(imageHeight), image = img, onClickImage = onClickImage)
        }
        item {
            AddPictureElement(
                modifier = Modifier
                    .height(imageHeight)
                    .width(imageWidth),
                onClickAddImage = onClickAddImage,
            )
        }
        item {
            AddPictureElement(
                modifier = Modifier
                    .height(imageHeight)
                    .width(imageWidth),
                onClickAddImage = onClickTakePhoto,
                color = Color.Red,
            )
        }

    }

}



@Composable
fun AddPictureElement(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    color: Color = MaterialTheme.colors.secondary
) {
    Card (
        modifier = Modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(16.dp)
    ){
        Box(
            modifier = modifier
                .clickable { onClickAddImage() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
                    .background(color, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.image_selector_add_image),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }

    }
}


@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    image: Uri,
    elevation: Dp = 10.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    onClickImage: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape
    ) {
        AsyncImage(
            model = image,
            contentDescription = "",
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun ImageElementPreview() {
    val images: List<Int> = listOf(
        R.drawable.placeholder_tupperware,
        R.drawable.placeholder_tupperware1,
        R.drawable.placeholder_tupperware2,
    )
    Cook4meTheme{
        AddPictureElement(modifier = Modifier.height(200.dp), onClickAddImage = { /*TODO*/ })
    }
}

/*@Preview(showBackground = true)
@Composable
fun AddImagePreview() {
    Cook4meTheme{
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

        ) {
            AddPictureElement(modifier = Modifier.height(200.dp).width(90.dp), onClickAddImage = {}, color = Color.Red)
            Spacer(modifier = Modifier.size(8.dp))
            AddPictureElement(modifier = Modifier.height(200.dp).width(90.dp), onClickAddImage = {})
        }
    }
}*/
/*
@Preview("default", showBackground = true)
@Composable
fun ImageSelectorPreview() {
    val images: List<Int> = listOf(
        R.drawable.placeholder_tupperware,
        R.drawable.placeholder_tupperware1,
        R.drawable.placeholder_tupperware2,
        R.drawable.placeholder_tupperware,
        R.drawable.placeholder_tupperware1,
        R.drawable.placeholder_tupperware2,
    )

    Cook4meTheme {
        ImageSelector(
            modifier = Modifier.height(200.dp),
            images = images,
            onClickAddImage = {},
            onClickTakePhoto = {},
            onClickImage = {},
        )
    }
}

@Preview("default", showBackground = true)
@Composable
fun ImageSelectorEmptyPreview() {
    val images: List<Int> = listOf()

    Cook4meTheme {
        ImageSelector(
            modifier = Modifier.height(200.dp),
            images = images,
            onClickAddImage = {},
            onClickTakePhoto = {},
            onClickImage = {},
        )
    }
}*/