package pro.stuermer.dailyexpenses

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
