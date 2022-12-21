package pro.stuermer.dailyexpenses.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun MonthPicker(
    modifier: Modifier = Modifier,
    startDate: LocalDate,
    endDate: LocalDate,
    selectedDate: LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (previousIconRef, selectedDateRef, nextIconRef) = createRefs()

        if (startDate < selectedDate) {
            IconButton(
                modifier = Modifier.constrainAs(previousIconRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                onClick = onPrevious
            ) {
                Icon(Icons.Outlined.ArrowLeft, null)
            }
        }

        // show month only if current year
        val pattern = if (LocalDate.now().year == selectedDate.year) {
            "MMMM"
        } else {
            "MMMM yy"
        }

        Text(
            modifier = Modifier
                .constrainAs(selectedDateRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            textAlign = TextAlign.Center,
            text = selectedDate.format(DateTimeFormatter.ofPattern(pattern)),
            style = MaterialTheme.typography.titleMedium
        )

        if (selectedDate < endDate) {
            IconButton(
                modifier = Modifier.constrainAs(nextIconRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                onClick = onNext
            ) {
                Icon(Icons.Outlined.ArrowRight, null)
            }
        }
    }
}

@Preview("DatePicker")
@Composable
private fun DatePickerPreview() {
    DailyExpensesTheme {
        MonthPicker(
            startDate = LocalDate.now().minusDays(3),
            endDate = LocalDate.now().plusDays(3),
            selectedDate = LocalDate.now(),
            onPrevious = {},
        ) {}
    }
}

@Preview("DatePicker")
@Composable
private fun DatePickerPreview2() {
    DailyExpensesTheme {
        MonthPicker(
            startDate = LocalDate.now().minusDays(3),
            endDate = LocalDate.now().plusDays(3),
            selectedDate = LocalDate.now(),
            onPrevious = {},
        ) {}
    }
}
