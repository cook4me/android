package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

object GenericSeparators {

    val ConstantSeparator = { separator: String ->
        generateSequence(separator) { it }
    }

    /**
     * Default formatting:
     *
     * • item1
     *
     * • item2
     *
     * • etc...
     */
    val BulletSeparator = ConstantSeparator("• ")

    /**
     * Enumerated list formatting, as in:
     * 1. item1
     * 2. item2
     * 3. etc...
     */
    val EnumeratedList = generateSequence(1) { it + 1 }.map { "$it. " }
}

class ListVisualTransformation(
    private val separator: Sequence<String>
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText =
            if (text.isBlank()) {
                ""
            } else {
                text.lines()
                    .zip(separator.asIterable())
                    .map { (line, separator) -> "$separator${line.trimStart()}" }
                    .reduce { x, y -> x + '\n' + y }
            }

        return TransformedText(
            AnnotatedString(formattedText),
            object : OffsetMapping {
                private fun offsetDueToSeparator(nbOfLinesBeforeCursor: Int): Int {
                    return separator.take(nbOfLinesBeforeCursor + 1)
                        .map { separator -> separator.length }
                        .reduce { x, y -> x + y }
                }

                override fun originalToTransformed(offset: Int): Int {
                    if (text.isBlank()) {
                        return 0
                    }
                    val nbOfLinesBeforeCursor = text
                        .dropLast(text.length - offset)
                        .count { it == '\n' }

                    return offset + offsetDueToSeparator(nbOfLinesBeforeCursor)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (text.isBlank()) {
                        return 0
                    }
                    val textBeforeCursor = formattedText.dropLast(formattedText.length - offset)
                    val nbOfLinesBeforeCursor = textBeforeCursor.count { it == '\n' }
                    val newOffset = offset - offsetDueToSeparator(nbOfLinesBeforeCursor)

                    val lastLineLength = textBeforeCursor.lines().last().length
                    val lastSeparatorLength = separator.take(nbOfLinesBeforeCursor + 1).last().length

                    return if (lastLineLength < lastSeparatorLength) {
                        newOffset + lastSeparatorLength - lastLineLength
                    } else {
                        newOffset
                    }
                }
            }
        )
    }
}

private val defaultShape = RoundedCornerShape(8.dp)

/**
 * Text field where input is automatically formatted as "bullet points" at each newline.
 * The "bullet point" style can be changed through the separators parameter.
 * By default it is [GenericSeparators.BulletSeparator]
 */
@Composable
fun BulletPointTextField(
    modifier: Modifier = Modifier,
    placeholder: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit,
    separators: Sequence<String> = GenericSeparators.BulletSeparator,
    contentDescription: String,
    shape: Shape = defaultShape
) {
    fun textFormatter(text: String): String {
        return text.trimStart()
            .lines()
            .map { line -> line.trimStart() }
            .reduce { x, y -> x + '\n' + y }
    }

    val text = remember { mutableStateOf("") }
    CustomTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = {
            text.value = textFormatter(it)
            onValueChange(it)
        },
        placeholder = placeholder,
        visualTransformation = ListVisualTransformation(separators),
        contentDescription = contentDescription,
        shape = shape
    )
}

@Preview("default", showBackground = true)
@Composable
fun IngredientEntry() {
    Cook4meTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BulletPointTextField(contentDescription = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
