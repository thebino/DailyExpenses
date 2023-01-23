package pro.stuermer.dailyexpenses.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import pro.stuermer.dailyexpenses.R

@Polymorphic
@Serializable
enum class Category {
    Grocery,
    Clothing,
    Restaurant,
    Hobby,
    Living,
    Commute,
    Other,
}

@Suppress("MagicNumber")
val Category.color: Color
    get() = when (this) {
        Category.Clothing -> Color(0x579C27B0)
        Category.Grocery -> Color(0x86249904)
        Category.Restaurant -> Color(0x8679F8DC)
        Category.Hobby -> Color(0xFFE6FFC4)
        Category.Living -> Color(0xffc4e7ff)
        Category.Commute -> Color(0x5BE91E63)
        Category.Other -> Color(0x63FF9800)
    }

val Category.icon: ImageVector
    get() = when (this) {
        Category.Clothing -> Icons.Default.Checkroom
        Category.Grocery -> Icons.Filled.Restaurant
        Category.Restaurant -> Icons.Filled.FoodBank
        Category.Hobby -> Icons.Filled.Nightlife
        Category.Living -> Icons.Filled.Home
        Category.Commute -> Icons.Filled.Commute
        Category.Other -> Icons.Filled.Category
    }

val Category.nameResource: Int
    get() = when (this) {
        Category.Clothing -> R.string.category_clothing
        Category.Grocery -> R.string.category_grocery
        Category.Restaurant -> R.string.category_restaurant
        Category.Hobby -> R.string.category_hobby
        Category.Living -> R.string.category_living
        Category.Commute -> R.string.category_commute
        Category.Other -> R.string.category_other
    }
