package pro.stuermer.dailyexpenses.ui.composables

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "phone | light", showBackground = true, device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "phone | dark", showBackground = true, device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "foldable | default", device = Devices.FOLDABLE, fontScale = 1f)
@Preview(name = "foldable | large", device = Devices.FOLDABLE, fontScale = 1.5f)
@Preview(name = "tablet | en", device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(name = "tablet | ar", device = "spec:width=1280dp,height=800dp,dpi=480", locale = "ar")
@Preview(name = "desktop", device = "spec:width=1920dp,height=1080dp,dpi=480")
annotation class ReferenceDevices
