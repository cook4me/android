package ch.epfl.sdp.cook4me.ui.common

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    onClickImage: () -> Unit,
    isError: Boolean,
    image: Uri?,
    imageSize: Dp = 200.dp,
) {
    Box(modifier = Modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.Center) {
        if (image == null)
            AddPictureButtons(
                onClickAddImage = onClickAddImage,
                onClickTakePhoto = onClickTakePhoto,
                imageHeight= imageSize,
                isError = isError
            )
        else
            ImageCard(image = image, imageHeight = imageSize, onClick = onClickImage)
    }
}


@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    imageHeight: Dp,
    image: Uri,
    onClick: () -> Unit
) {
    var clicked by remember { mutableStateOf(false) }
    val scaleFactor = 0.4f
    val filter = if (clicked)
        ColorFilter.colorMatrix(ColorMatrix().apply {
            setToScale(scaleFactor, scaleFactor, scaleFactor, 1f)
        })
    else null

    Card(
        modifier = modifier
            .size(imageHeight)
            .testTag("image"),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .build(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { clicked = !clicked },
                contentScale = ContentScale.Crop,
                colorFilter = filter
            )
            if (clicked) {
                DeleteButton(Modifier.align(Alignment.Center), onClick)
            }
        }
    }
}

@Composable
private fun DeleteButton(modifier: Modifier, onClick: () -> Unit) {
    Box(modifier.size(60.dp)){
        IconButton(
            onClick = onClick,
            modifier = Modifier,
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

@Composable
private fun AddPictureButtons(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    imageHeight: Dp = 200.dp,
    isError: Boolean,
) {
    val color = if (isError) MaterialTheme.colors.error else Color.LightGray

    Box(modifier = modifier.height(imageHeight), contentAlignment = Alignment.Center) {
        DottedRectangle(modifier, color = color)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            //
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                AddPictureFromGalleryButton(onClick = onClickAddImage)
                TakePictureButton(onClick = onClickTakePhoto)
            }
            Text(text = "Add a picture", color = Color.LightGray, style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
private fun DottedRectangle(modifier: Modifier = Modifier, color: Color) {
    val stroke = Stroke(
        width = 6f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
    )
    Canvas(
        modifier = Modifier.fillMaxSize()
    ){
        drawRoundRect(color = color,style = stroke)
    }
}

@Composable
private fun AddPhotoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.padding(16.dp),
        backgroundColor = Color.LightGray
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White//MaterialTheme.colors.onSurface
        )
    }
}


@Composable
private fun AddPictureFromGalleryButton(
    onClick: () -> Unit,
) {
    AddPhotoButton(
        modifier = Modifier.testTag("Add From Gallery Button"),
        onClick = onClick,
        icon = Icons.Default.AddPhotoAlternate
    )
}

@Composable
private fun TakePictureButton(
    onClick: () -> Unit,
) {
    AddPhotoButton(modifier = Modifier, onClick = onClick, icon = Icons.Default.AddAPhoto)
}