@file:Suppress("MagicNumber")

package pro.stuermer.dailyexpenses.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pro.stuermer.dailyexpenses.domain.model.Category
import pro.stuermer.dailyexpenses.domain.model.GraphData
import pro.stuermer.dailyexpenses.domain.model.nameResource
import pro.stuermer.dailyexpenses.ui.theme.neuropoliticalFamily
import kotlin.math.min

@Composable
fun Legend(
    modifier: Modifier = Modifier,
    chartData: List<GraphData>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sum = chartData.sumOf { it.value }
        chartData.sortedBy {
            it.value * 100 / sum
        }.reversed().subList(0, min(chartData.size, 5)).forEach { row ->
            LegendItem(row, sum)
        }
    }
}

@Composable
fun LegendItem(
    data: GraphData,
    sum: Int
) {
    Column(modifier = Modifier.padding(8.dp)) {
        data.label?.let {
            Text(
                text = stringResource(id = data.category.nameResource),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = neuropoliticalFamily
            )
        }
        Row(
            modifier = Modifier.semantics(mergeDescendants = true) { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(data.color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${data.value * 100 / sum} %",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LegendPreview() {
    MaterialTheme {
        Legend(
            chartData = fakeChartData
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LegendItemPreview() {
    MaterialTheme {
        LegendItem(
            data = fakeChartData[0], sum = fakeChartData[0].value
        )
    }
}

val fakeChartData = listOf(
    GraphData(category = Category.Clothing, label = "2016", value = 20, color = Color(0x579C27B0)),
    GraphData(category = Category.Grocery, label = "2017", value = 10, color = Color(0x86249904)),
    GraphData(category = Category.Restaurant, label = "2018", value = 2, color = Color(0x8679F8DC)),
    GraphData(category = Category.Hobby, label = "2019", value = 42, color = Color(0xFFE6FFC4)),
    GraphData(category = Category.Living, label = "2020", value = 7, color = Color(0xffc4e7ff)),
    GraphData(category = Category.Commute, label = "2021", value = 5, color = Color(0x5BE91E63)),
    GraphData(category = Category.Other, label = "2022", value = 2, color = Color(0x63FF9800)),
)
