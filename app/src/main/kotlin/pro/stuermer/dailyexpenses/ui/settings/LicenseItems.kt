package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun LicenseSettingItem(
    modifier: Modifier = Modifier,
    text: String,
    link: String,
    destination: String,
) {
    val uriHandler = LocalUriHandler.current
    SettingItem(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            val annotatedLinkString = buildAnnotatedString {
                val startIndex = text.indexOf(link)
                val endIndex = startIndex + link.length

                val red = MaterialTheme.colorScheme.onSurfaceVariant.red
                val green = MaterialTheme.colorScheme.onSurfaceVariant.green
                val blue = MaterialTheme.colorScheme.onSurfaceVariant.blue
                withStyle(style = SpanStyle(color = Color(red = red, green = green, blue = blue))) {
                    append(text)
                    addStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        ), start = startIndex, end = endIndex
                    )
                }

                addStringAnnotation(
                    tag = "URL", annotation = destination, start = startIndex, end = endIndex
                )
            }

            ClickableText(text = annotatedLinkString,
                style = MaterialTheme.typography.bodyLarge,
                onClick = {
                    annotatedLinkString.getStringAnnotations("URL", it, it).firstOrNull()
                        ?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                })
        }
    }
}

@Preview
@Composable
fun LicenseSettingItemPreview() {
    DailyExpensesTheme {
        LicenseSettingItem(
            modifier = Modifier.fillMaxWidth(),
            text = "This app is using Neuropolitical typeface from Typodermic Fonts Inc. A free for commercial use font.",
            link = "Neuropolitical",
            destination = "https://typodermicfonts.com/neuropolitical-science"
        )
    }
}
