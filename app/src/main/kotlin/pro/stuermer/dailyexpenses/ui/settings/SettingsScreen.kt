package pro.stuermer.dailyexpenses.ui.settings

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.ui.composables.ShareContent
import pro.stuermer.dailyexpenses.ui.home.BottomSheetDialog
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit
) {
    val viewModel: SettingsViewModel = getViewModel()

    SettingsList(
        uiState = viewModel.uiState.collectAsState().value,
        handleEvent = viewModel::handleEvent,
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    handleEvent: (event: SettingsEvent) -> Unit,
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.title_settings),
                    fontFamily = FontFamily.Default
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
        )
    }, content = { contentPadding: PaddingValues ->
        Box {
            Column(modifier = Modifier.padding(contentPadding)) {
                MaterialYouSettingItem(
                    title = stringResource(R.string.settings_material_label),
                    checked = uiState.isMaterialYouEnabled,
                    onCheckedChanged = { newState: Boolean ->
                        if (newState) {
                            handleEvent(SettingsEvent.EnableMaterialYou)
                        } else {
                            handleEvent(SettingsEvent.DisableMaterialYou)
                        }
                    }
                )

                SharingSettingItem(
                    title = stringResource(R.string.settings_share_label),
                    onSettingClicked = {
                        handleEvent(SettingsEvent.ShowShareDialog)
                    }
                )

                SectionSpacer(modifier = Modifier.fillMaxWidth())

                LicenseSettingItem(
                    text = "This app is using Neuropolitical typeface from Typodermic Fonts Inc. A free for commercial use font.",
                    link = "Neuropolitical",
                    destination = "https://typodermicfonts.com/neuropolitical-science"
                )

                LicenseSettingItem(
                    text = "The money purse illustration is created by flatart from vecteezy.com",
                    link = "flatart from vecteezy.com",
                    destination = "https://www.vecteezy.com/members/flatart"
                )

                val context = LocalContext.current
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0L)
                    )
                } else {
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }
                val version: String = packageInfo.versionName

                AppVersionItem(appVersion = version)
            }

            AnimatedVisibility(
                visible = uiState.showShareDialog,
                enter = slideIn(
                    initialOffset = { IntOffset(0, it.height) }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = slideOut(
                    targetOffset = { IntOffset(0, it.height) },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ),
            ) {
                BottomSheetDialog(contentPadding = contentPadding,
                    visible = uiState.showShareDialog,
                    padding = 4.dp,
                    verticalArrangement = Arrangement.Center,
                    onDismissDialog = {
                        handleEvent(SettingsEvent.HideShareDialog)
                    }) {
                    ShareContent(isLoading = uiState.isSharingLoading,
                        isEnrolled = uiState.isShareingEnrolled,
                        shareCode = uiState.sharingCode,
                        error = uiState.sharingError,
                        onCodeChanged = { code: String ->
                            handleEvent(SettingsEvent.CodeChanged(code))
                        },
                        onGenerateClicked = {
                            handleEvent(SettingsEvent.GenerateShare)
                        },
                        onJoinClicked = {
                            handleEvent(SettingsEvent.JoinShare(uiState.sharingCode))
                        },
                        onShareLeaveClicked = {
                            handleEvent(SettingsEvent.LeaveSharing)
                        })
                }
            }
        }
    })
}

@Preview
@Composable
private fun MaterialYouSettingItemPreview() {
    DailyExpensesTheme {
        SettingsList(
            uiState = SettingsUiState(),
            handleEvent = {},
            onNavigateUp = {},
        )
    }
}
