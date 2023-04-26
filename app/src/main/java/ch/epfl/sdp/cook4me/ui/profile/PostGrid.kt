package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Preview(showBackground = true)
@Composable
fun PostGrid(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val dataFileString = getJsonDataFromAsset(context, "PostList.json")
    val gson = Gson()
    val gridSampleType = object : TypeToken<List<Post>>() {}.type
    val postData: List<Post> = gson.fromJson(dataFileString, gridSampleType)

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = Modifier.padding(10.dp)
    ) {
        items(postData) { post ->
            PostDataItem(
                post,
                modifier,
            )
        }
    }
}

@Composable
fun PostDataItem(data: Post, modifier: Modifier) {
    Card(
        modifier = modifier
            .clickable() {
            }
            .fillMaxSize(),
        elevation = 10.dp, shape = RoundedCornerShape(5.dp)
    ) {
        Column(modifier = modifier) {
            MockImageSelection(data = data, modifier = modifier)
        }
    }
}

@Composable
fun MockImageSelection(data: Post, modifier: Modifier) {
    Image(
        painter = painterResource(
            id = when (data.id) {
                1L -> R.drawable.placeholder_carbonara
                2L -> R.drawable.tiramisu
                else -> R.drawable.placeholder_guacamole
            }
        ),
        contentDescription = "Grid Image",
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp)),
        alignment = Alignment.Center
    )
}

fun getJsonDataFromAsset(context: Context, data: String) =
    context.assets.open(data).bufferedReader().use { it.readText() }
