package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.domain.Post
import ch.epfl.sdp.cook4me.ui.theme.Purple500
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/***
 * Todo navgation return a string to the navcontroller indicating the location
 */
@Preview(showBackground = true)
@Composable
fun PostGrid() {
    val context = LocalContext.current

    val dataFileString = getJsonDataFromAsset(context, "PostList.json")
    val gson = Gson()
    val gridSampleType = object : TypeToken<List<Post>>() {}.type
    val postData: List<Post> = gson.fromJson(dataFileString, gridSampleType)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Purple500)
                .padding(6.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Receipts",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            modifier = Modifier.padding(10.dp)
        ) {
           items(postData) { post ->
                PostDataItem(post)
            }
        }
    }
}

@Composable
fun PostDataItem(data: Post) {
    Card(modifier = Modifier.clickable(){
        val itemVal = Gson().toJson(data)
    }
        .fillMaxSize(), elevation = 10.dp, shape = RoundedCornerShape(5.dp)) {
        Column(modifier = Modifier) {
            Image(
                painter = painterResource(
                    id = when (data.id) {
                        1L -> R.drawable.carbonara
                        2L -> R.drawable.tiramisu
                        3L -> R.drawable.guacamole
                        4L -> R.drawable.tiramisu
                        5L -> R.drawable.carbonara
                        6L -> R.drawable.tiramisu
                        else -> R.drawable.guacamole
                    }
                ),
                contentDescription = "Grid Image",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(10.dp)),
                alignment = Alignment.Center
            )
            /*                Text(
                          text = data.name,
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          fontSize = 15.sp,
                          fontWeight = FontWeight.Bold,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                      )
                      Spacer(modifier = Modifier.padding(1.dp))
                      Text(
                          text = data.desc,
                          modifier = Modifier.padding(7.dp, 0.dp, 0.dp, 20.dp),
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Normal,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                     )*/
        }
    }
}


fun getJsonDataFromAsset(context: Context, data: String): String {
    return context.assets.open(data).bufferedReader().use { it.readText() }
}