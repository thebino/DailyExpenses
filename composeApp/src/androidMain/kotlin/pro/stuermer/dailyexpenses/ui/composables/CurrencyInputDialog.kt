package pro.stuermer.dailyexpenses.ui.composables

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.domain.model.Category
import pro.stuermer.dailyexpenses.domain.model.icon
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun CurrencyInputDialog(
    modifier: Modifier = Modifier,
    category: Category,
    onCategoryChanged: (category: Category) -> Unit = {},
    description: String,
    onDescriptionChanged: (description: String) -> Unit = {},
    amount: Float,
    onAmountChanged: (amount: Float) -> Unit = {},
    date: LocalDate = LocalDate.now(),
    onDateChanged: (date: LocalDate) -> Unit = {},
    onCancelClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    isUpdate: Boolean = false,
) {
    val text = remember {
        mutableStateOf(
            if (amount > 0.0f) {
                amount.toString()
            } else {
                ""
            }
        )
    }
    val amountFocusRequester = FocusRequester()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(8.dp),
    ) {

        val context = LocalContext.current
        val now = LocalDate.now()
        val datePickerDialog = DatePickerDialog(
            context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
            }, now.year, now.monthValue - 1, now.dayOfMonth
        )

        var expanded by remember { mutableStateOf(false) }
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Category.entries.forEach { category: Category ->
                    CategoryItem(
                        category = category,
                        size = 44.dp,
                        iconSize = 20.dp
                    ) {
                        onCategoryChanged(category)
                        expanded = !expanded
                    }
                }
            }
        }

        Row {
            Column {
                CategorySelection(
                    category = category,
                    onSelectionClicked = {
                        expanded = !expanded
                    }
                )
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(96.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                datePickerDialog.show()
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val dateFormatter: DateTimeFormatter =
                            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

                        Text(
                            text = date.format(dateFormatter),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Column {
                TextField(
                    modifier = Modifier.padding(4.dp),
                    value = description,
                    onValueChange = onDescriptionChanged,
                    placeholder = { Text(stringResource(R.string.currency_input_description_label)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        amountFocusRequester.requestFocus()
                    })
                )
                TextField(
                    modifier = Modifier
                        .padding(4.dp)
                        .focusRequester(amountFocusRequester),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    value = text.value,
                    onValueChange = { newAmount: String ->
                        text.value = newAmount.filter { c: Char -> c in '0'..'9' }
                    },
                    visualTransformation = CurrencyVisualTransformation(),
                )
            }
        }
        ActionButtons(
            isSaveEnabled = text.value.isNotEmpty(),
            onCancelClicked = onCancelClicked,
            onSaveClicked = {
                val amountValue: Float = text.value.toCurrencyFloat()
                onAmountChanged(amountValue)
                onSaveClicked()
            },
            isUpdate = isUpdate
        )
    }
}


@Composable
private fun CategorySelection(
    modifier: Modifier = Modifier,
    category: Category,
    onSelectionClicked: () -> Unit = {},
) {
    CategoryItem(
        modifier = modifier,
        category = category
    ) {
        onSelectionClicked()
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    iconSize: Dp = 48.dp,
    category: Category,
    onItemClicked: () -> Unit = {}
) {
    Card(modifier = modifier
        .clickable {
            onItemClicked()
        }
        .padding(4.dp)
        .size(size)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun ActionButtons(
    modifier: Modifier = Modifier,
    onCancelClicked: () -> Unit = {},
    isSaveEnabled: Boolean = false,
    isUpdate: Boolean = false,
    onSaveClicked: () -> Unit = {},
) {
    Row(modifier = modifier) {
        OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelClicked) {
            Text(text = "Cancel")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            modifier = Modifier.weight(1f), enabled = isSaveEnabled, onClick = onSaveClicked
        ) {
            if (isUpdate) {
                Text(text = "Update expense")
            } else {
                Text(text = "Add expense")
            }
        }
    }
}

@ReferenceDevices
@Composable
private fun InputDialogPreview() {
    DailyExpensesTheme {
        CurrencyInputDialog(
            amount = 13.27f,
            description = "",
            category = Category.Hobby
        )
    }
}
