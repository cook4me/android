package ch.epfl.sdp.cook4me.ui.simpleComponent

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun Ingredient(
    modifier: Modifier = Modifier,
    text: String,
    verticalPadding: Dp = 3.dp,
    onClickIngredient: () -> Unit = {}
) {

    val shownText = "– $text"
    Text(
        modifier = modifier
            .clickable { onClickIngredient }
            .padding(vertical = verticalPadding),
        text = shownText,
        fontWeight = FontWeight.Thin,
    )
}
class BulletPointsVisualTransformation(private val separator: String = "\u2022 ") : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText =
            if (text.isNotBlank()) {
                text.lines()
                    .map { line -> "$separator${line.trimStart()}" }
                    .reduce { x, y -> x + '\n' + y }
            } else {""}

        return TransformedText(
            AnnotatedString(formattedText),
            object: OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    Log.d("Debug", "ogText: ${text.toString().replace(' ', '#')}")
                    if (text.isBlank()) {
                        return 0
                    }

                    val nbOfBulletPointsBeforeCursor = text
                        .dropLast(text.length - offset)
                        .count { it == '\n' }
                    return offset + (nbOfBulletPointsBeforeCursor + 1) * separator.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (text.isBlank()) {
                        return 0
                    }

                    val nbOfBulletPointsBeforeCursor = formattedText
                        .dropLast(formattedText.length - offset)
                        .count { it == '\n' }
                    return offset - (nbOfBulletPointsBeforeCursor + 1) * separator.length
                }
            }
        )

    }
}

@Composable
fun IngredientsEntry(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) ->  Unit,
) {
    fun onValueChange(text: String): String {
        val formattedText = text.trimStart()
            .lines()
            .map { line ->
                "${line.trimStart()}"
            }.reduce {x, y -> x + '\n' + y}
        return formattedText
    }

    val text = remember { mutableStateOf("") }
    TextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { text.value = onValueChange(it) },
        placeholder = {Text(text="Tap to add ingredients")},
        visualTransformation = BulletPointsVisualTransformation()
    )
}

@Composable
fun IngredientsList(
    modifier: Modifier = Modifier,
    ingredients: List<String>,
) {
    Column(
        modifier = modifier,
    ) {
        ingredients.forEach { ingredient ->
            Ingredient(text = ingredient)
        }
    }

}

@Preview("default", showBackground = true)
@Composable
fun IngredientPreview() {
    val ingredients = listOf<String>("3 onions", "1 tbs of olive oil", "1 lbs of chicken breast")
    Cook4meTheme {
        IngredientsList(ingredients = ingredients)
    }
}

@Preview("default", showBackground = true)
@Composable
fun IngredientEntry() {
    Cook4meTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            IngredientsEntry(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}