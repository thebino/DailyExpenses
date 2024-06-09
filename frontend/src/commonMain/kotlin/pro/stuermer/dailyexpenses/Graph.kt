package pro.stuermer.dailyexpenses

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pro.stuermer.dailyexpenses.model.GraphData
import pro.stuermer.dailyexpenses.theme.DailyExpensesTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Graph(
    modifier: Modifier = Modifier,
    width: Dp = 240.dp,
    chartData: List<GraphData>,
    halfcircle: Boolean,
    drawLabels: Boolean = false,
    outerMargin: Float = 0f
) {
    val transitionAnimation = remember(chartData) {
        Animatable(
            initialValue = 0f
        )
    }
    LaunchedEffect(chartData) {
        transitionAnimation.animateTo(
            1f, animationSpec = TweenSpec(durationMillis = 750)
        )
    }

    val radiusBorder = with(LocalDensity.current) { 24.dp.toPx() }
    val labelTextPaint = remember {
        Paint().apply {
//            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//            textAlign = Paint.Align.CENTER
        }
    }.apply {
        alpha = ((255 / 100) * (transitionAnimation.value * 100))
    }

    val sumOfDataSet = remember(chartData) { chartData.sumOf { it.value } }

    if (sumOfDataSet > 0) {
        // Graph + Legend
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val height = if (halfcircle) {
                width / 2
            } else {
                width
            }
            Canvas(
                modifier = modifier
                    .width(width)
                    .height(height),
            ) {
                var currentSegment = 0f
                var currentStartAngle = 0f
                val radius = size.width / 2
                val totalAngle = if (halfcircle) {
                    180
                } else {
                    360
                }

                chartData.sortedBy { it.value }.reversed().forEach { chartData ->
                    val segmentAngle = totalAngle * chartData.value / sumOfDataSet
                    val angleToDraw =
                        totalAngle * (chartData.value * transitionAnimation.value) / sumOfDataSet

                    val strokeWidth = (width.value / 4 * 2) //160f
                    val verticalOffset = if (halfcircle) {
                        radius
                    } else {
                        0f
                    }

                    drawArc(
                        color = chartData.color,
                        startAngle = currentStartAngle,
                        sweepAngle = angleToDraw,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2 - verticalOffset),
                        size = Size(
                            width = radius * 2f - strokeWidth, height = radius * 2f - strokeWidth
                        ),
                        alpha = 0.7f,
                        style = Stroke(strokeWidth, cap = StrokeCap.Butt),
                    )
                    currentSegment += segmentAngle
                    currentStartAngle += angleToDraw

                    if (drawLabels) {
                        drawIntoCanvas {
                            val medianAngle = (currentSegment - (segmentAngle / 2)) * Math.PI / 180f
                            val radiusWithBorder = radius + radiusBorder
                            val drawAtX =
                                ((radiusWithBorder + outerMargin) * cos(medianAngle)).toFloat() + radius
                            val drawAtY =
                                ((radiusWithBorder + outerMargin) * sin(medianAngle)).toFloat() + radius

//                            it.nativeCanvas.drawText(
//                                chartData.value.toString(), drawAtX, drawAtY, labelTextPaint
//                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Legend(chartData = chartData)
        }
    }
}

@Preview
@Composable
fun GraphPreview() {
    DailyExpensesTheme {
        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            chartData = fakeChartData,
            halfcircle = false,
//            useCenter = false,
//            style = Stroke(150f, cap = StrokeCap.Butt),
        )
    }
}
