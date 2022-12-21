package pro.stuermer.dailyexpenses.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import java.lang.IndexOutOfBoundsException
import pro.stuermer.dailyexpenses.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnrolled: Boolean = false,
    error: String? = null,
    shareCode: String = "",
    onCodeChanged: (String) -> Unit,
    onGenerateClicked: () -> Unit,
    onJoinClicked: () -> Unit,
    onShareLeaveClicked: () -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.share_dialog_description),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        PinInput(
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading,
            value = shareCode,
            onValueChanged = { input: String ->
                onCodeChanged(input)
            },
        )

        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "Unknown error!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraLight,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (isEnrolled.not()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.share_dialog_explanation),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = shareCode.isEmpty(),
                    onClick = {
                        onGenerateClicked()
                        keyboard?.hide()
                    }
                ) {
                    Text(text = stringResource(R.string.share_dialog_button_generate))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onJoinClicked,
                    enabled = shareCode.length == 6
                ) {
                    Text(text = stringResource(R.string.share_dialog_button_join))
                }
            }
        } else {
            Button(
                modifier = Modifier,
                onClick = onShareLeaveClicked
            ) {
                Text(text = stringResource(R.string.share_dialog_button_stop))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PinInput(
    modifier: Modifier = Modifier,
    length: Int = 6,
    numbersOnly: Boolean = false,
    isLoading: Boolean = false,
    value: String = "",
    onValueChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val keyboardState by keyboardAsState()

    TextField(
        readOnly = false,
        value = value,
        onValueChange = {
            if (it.length <= length) {
                // skip non-numerical inputs
                if (numbersOnly) {
                    if (it.all { c -> c in '0'..'9' }) {
                        onValueChanged(it)
                    }
                } else {
                    onValueChanged(it.uppercase())
                }
                if (it.length >= length) {
                    keyboard?.hide()
                }
            }
        },
        modifier = Modifier
            // Hide the text field
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(6) { index ->
            val shadowElevation = if (keyboardState == Keyboard.Opened && index == value.length) {
                6.dp
            } else {
                0.dp
            }
            CharaterInput(
                modifier = Modifier.clickable {
                    if (value.isNotEmpty()) {
                        // when losing focus (keyboard closed) & clicking last item
                        try {
                            onValueChanged(value.removeRange(index, index + 1))
                        } catch (e: IndexOutOfBoundsException) {
                            // no-op
                        }
                    }
                    focusRequester.requestFocus()
                    keyboard?.show()
                }, value = value.getOrNull(index)?.toString()?.uppercase() ?: "",
                isLoading = isLoading,
                shadowElevation = shadowElevation
            )

            // add divider
            if (index == 2) {
                Text(text = "-")
            }
        }
    }
}

enum class Keyboard {
    Opened, Closed
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun keyboardAsState(): State<Keyboard> {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isResumed = lifecycle.currentState == Lifecycle.State.RESUMED
    return rememberUpdatedState(if (WindowInsets.isImeVisible && isResumed) Keyboard.Opened else Keyboard.Closed)
}

@Composable
fun CharaterInput(
    value: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 0.dp,
) {
    Surface(
        modifier = modifier.padding(4.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 30)),
        color = MaterialTheme.colorScheme.onPrimary,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
        shadowElevation = shadowElevation,
        tonalElevation = tonalElevation,
    ) {
        Box(
            modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 1.2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = value, color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShareContentPreview(
    @PreviewParameter(ShareContentPreviewParameterProvider::class) content: ShareContentPreviewData
) {
    MaterialTheme {
        ShareContent(
            isLoading = content.isLoading,
            isEnrolled = content.isEnrolled,
            shareCode = content.code,
            onCodeChanged = {},
            onGenerateClicked = {},
            onJoinClicked = {},
            onShareLeaveClicked = {},
        )
    }
}

private data class ShareContentPreviewData(
    val isLoading: Boolean,
    val isEnrolled: Boolean,
    val code: String
)

private class ShareContentPreviewParameterProvider: PreviewParameterProvider<ShareContentPreviewData> {
    override val values: Sequence<ShareContentPreviewData>
        get() = listOf(
            ShareContentPreviewData(isLoading = true, isEnrolled = false, code = ""),
            ShareContentPreviewData(isLoading = false, isEnrolled = false, code = "D3X9yi"),
            ShareContentPreviewData(isLoading = false, isEnrolled = true, code = "D3X9yi"),
        ).asSequence()
}
