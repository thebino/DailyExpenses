package pro.stuermer.dailyexpenses.domain.model

import androidx.compose.ui.graphics.Color

class GraphData(
    val category: Category,
    val value: Int,
    val color: Color,
    val label: String? = null,
)
