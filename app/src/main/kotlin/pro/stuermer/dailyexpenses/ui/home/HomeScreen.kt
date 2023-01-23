package pro.stuermer.dailyexpenses.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.math.roundToInt
import org.koin.androidx.compose.getViewModel
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.domain.model.Category
import pro.stuermer.dailyexpenses.domain.model.Expense
import pro.stuermer.dailyexpenses.domain.model.GraphData
import pro.stuermer.dailyexpenses.domain.model.color
import pro.stuermer.dailyexpenses.ui.composables.CurrencyInputDialog
import pro.stuermer.dailyexpenses.ui.composables.Graph
import pro.stuermer.dailyexpenses.ui.composables.MonthPicker
import pro.stuermer.dailyexpenses.ui.composables.ReferenceDevices
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme
import timber.log.Timber

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit = {},
    description: String? = null,
    amount: Float? = 0.0f,
) {
    val viewModel: HomeViewModel = getViewModel()
    Timber.e("description=$description")
    Timber.e("amount=$amount")
    if (description != null) {
        viewModel.handleEvent(HomeScreenEvent.NewItemEvent)
    }

    HomeContent(
        state = viewModel.uiState.collectAsState().value,
        handleEvent = viewModel::handleEvent,
        onNavigateToSettings = onNavigateToSettings
    )
}

/**
 * stateless
 */
@Composable
fun HomeContent(
    state: HomeScreenState,
    handleEvent: (event: HomeScreenEvent) -> Unit,
    onNavigateToSettings: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val activeExpense = remember {
        mutableStateOf(Expense(description = "", amount = 0f))
    }

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(message = state.error)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(scrollBehavior = scrollBehavior, title = {
                Text(
                    text = stringResource(id = R.string.app_name_full),
                    style = MaterialTheme.typography.titleLarge
                )
            }, actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Sharp.Settings,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                    )
                }
            })
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            // hide FAB in dialogs
            if (state.showInputDialog.not()) {
                FloatingActionButton(modifier = Modifier, onClick = {
                    activeExpense.value = Expense(description = "", amount = 0f)
                    handleEvent(HomeScreenEvent.NewItemEvent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.action_add_expense)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        val listState = rememberLazyListState()
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isLoading,
            onRefresh = { handleEvent(HomeScreenEvent.RefreshEvent) }
        )

        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Box {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MonthPicker(startDate = LocalDate.now().minusYears(1),
                                endDate = LocalDate.now(),
                                selectedDate = state.selectedDate,
                                onPrevious = { handleEvent(HomeScreenEvent.SelectPreviousMonth) },
                                onNext = { handleEvent(HomeScreenEvent.SelectNextMonth) })

                            val groups = state.items.groupBy { it.category }

                            Graph(modifier = Modifier.padding(horizontal = 16.dp),
                                width = 380.dp,
                                halfcircle = true,
                                chartData = groups.map { map ->
                                    val itemsCategory: Category = map.key
                                    val categorySum: Float = map.value.map { it.amount }.sum()
                                    GraphData(
                                        category = itemsCategory,
                                        value = categorySum.toInt(),
                                        color = map.key.color,
                                        label = categorySum.roundToInt().toString()
                                    )
                                })

                            if (state.isLoading) {
                                Loading()
                            } else {
                                if (state.items.isEmpty()) {
                                    Empty()
                                } else {
                                    Content(
                                        items = state.items,
                                        listState = listState,
                                        onEditClicked = {
                                            handleEvent(HomeScreenEvent.EditExpenseEvent(it))
                                        },
                                        onDeleteClicked = {
                                            handleEvent(HomeScreenEvent.DeleteExpenseEvenr(it))
                                        }
                                    )
                                }
                            }
                        }

                        BottomSheetDialog(modifier = Modifier.padding(top = 48.dp),
                            visible = state.showInputDialog,
                            contentPadding = contentPadding,
                            verticalArrangement = Arrangement.Bottom,
                            onDismissDialog = {
                                handleEvent(HomeScreenEvent.HideInput)
                            }) {

                            if (state.selectedExpense != null) {
                                activeExpense.value = state.selectedExpense
                            }

                            AnimatedVisibility(
                                visible = state.showInputDialog,
                                enter = slideIn(
                                    initialOffset = { IntOffset(0, it.height) },
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                exit = slideOut(
                                    targetOffset = { IntOffset(0, it.height) },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                            ) {
                                CurrencyInputDialog(
                                    category = activeExpense.value.category,
                                    onCategoryChanged = { category: Category ->
                                        activeExpense.value =
                                            activeExpense.value.copy(category = category)
                                    },
                                    description = activeExpense.value.description,
                                    onDescriptionChanged = { description: String ->
                                        activeExpense.value =
                                            activeExpense.value.copy(description = description)
                                    },
                                    amount = activeExpense.value.amount,
                                    onAmountChanged = { amount: Float ->
                                        activeExpense.value =
                                            activeExpense.value.copy(amount = amount)
                                    },
                                    date = activeExpense.value.expenseDate,
                                    onDateChanged = { expenseDate: LocalDate ->
                                        activeExpense.value =
                                            activeExpense.value.copy(expenseDate = expenseDate)
                                    },
                                    onCancelClicked = { handleEvent(HomeScreenEvent.HideInput) },
                                    onSaveClicked = {
                                        if (state.selectedExpense != null) {
                                            handleEvent(HomeScreenEvent.UpdateEvent(activeExpense.value))
                                        } else {
                                            handleEvent(HomeScreenEvent.AddEvent(activeExpense.value))
                                        }
                                    },
                                    isUpdate = state.selectedExpense != null
                                )
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    contentPadding: PaddingValues,
    padding: Dp = 0.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Bottom,
    onDismissDialog: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    var tmpModifier: Modifier = modifier.fillMaxSize()
    if (visible) {
        tmpModifier = tmpModifier
            .background(Color.Gray.copy(alpha = 0.3f))
            .clickable {
                onDismissDialog()
            }
            .padding(padding)
    }
    Column(
        modifier = tmpModifier.padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@ReferenceDevices
@Composable
private fun HomeScreenPreview() {
    DailyExpensesTheme {
        HomeScreen()
    }
}
