package pro.stuermer.dailyexpenses.ui.composables

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "phone | light", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "phone | dark", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "foldable", device = Devices.FOLDABLE)
@Preview(name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(name = "desktop", device = "spec:width=1920dp,height=1080dp,dpi=480")
annotation class ReferenceDevices
