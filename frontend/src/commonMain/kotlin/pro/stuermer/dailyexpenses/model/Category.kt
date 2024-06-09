package pro.stuermer.dailyexpenses.model

import androidx.compose.ui.graphics.Color
import dailyexpenses.frontend.generated.resources.Res
import dailyexpenses.frontend.generated.resources.category_clothing
import dailyexpenses.frontend.generated.resources.category_commute
import dailyexpenses.frontend.generated.resources.category_grocery
import dailyexpenses.frontend.generated.resources.category_hobby
import dailyexpenses.frontend.generated.resources.category_living
import dailyexpenses.frontend.generated.resources.category_other
import dailyexpenses.frontend.generated.resources.category_restaurant
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Polymorphic
@Serializable
enum class Category {
    Grocery, Clothing, Restaurant, Hobby, Living, Commute, Other,
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

//val Category.icon: ImageVector
//    get() = when (this) {
//        Category.Clothing -> Icons.Default.Checkroom
//        Category.Grocery -> Icons.Filled.Restaurant
//        Category.Restaurant -> Icons.Filled.FoodBank
//        Category.Hobby -> Icons.Filled.Nightlife
//        Category.Living -> Icons.Filled.Home
//        Category.Commute -> Icons.Filled.Commute
//        Category.Other -> Icons.Filled.Category
//    }

val Category.nameResource: StringResource
    get() = when (this) {
        Category.Clothing -> Res.string.category_clothing
        Category.Grocery -> Res.string.category_grocery
        Category.Restaurant -> Res.string.category_restaurant
        Category.Hobby -> Res.string.category_hobby
        Category.Living -> Res.string.category_living
        Category.Commute -> Res.string.category_commute
        Category.Other -> Res.string.category_other
    }
