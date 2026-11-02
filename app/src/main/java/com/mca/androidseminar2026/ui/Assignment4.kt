package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mca.androidseminar2026.ViewModel.BrowseViewModel
import com.mca.androidseminar2026.ViewModel.CombinationViewModel
import com.mca.androidseminar2026.ViewModel.OrderViewModel
import com.mca.androidseminar2026.ViewModel.BudgetViewModel
import com.mca.androidseminar2026.ViewModel.RecommendationViewModel
import com.mca.androidseminar2026.model.OrderTab
import java.text.NumberFormat
import java.util.Locale

private object Destination {
    const val RESTAURANT = "restaurant"
    const val MENU = "menu"
    const val CART = "cart"
    const val COMBINATIONS = "combinations"
    const val BUDGET = "budget"
    const val RECOMMENDATIONS = "recommendations"
}

/** 바텀 바 항목을 Navigation 경로로 변환합니다. */
private fun OrderTab.route(): String = when (this) {
    OrderTab.RESTAURANT -> Destination.RESTAURANT
    OrderTab.MENU -> Destination.MENU
    OrderTab.CART -> Destination.CART
}

/** 공통 화면과 추천 화면을 연결하며 답안에서는 추천 화면만 교체합니다. */
@Composable
fun Assignment4(
    recommendations: @Composable (onAddToCart: (Map<Int, Int>) -> Unit, onBack: () -> Unit) -> Unit = { onAddToCart, onBack ->
        val viewModel: RecommendationViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        RecommendationScreen(state, viewModel::refresh, onAddToCart, onBack)
    },
) {
    val navController = rememberNavController()
    val orderViewModel: OrderViewModel = hiltViewModel()
    val browseViewModel: BrowseViewModel = hiltViewModel()
    val combinationViewModel: CombinationViewModel = hiltViewModel()
    val budgetViewModel: BudgetViewModel = hiltViewModel()
    val order by orderViewModel.uiState.collectAsStateWithLifecycle()
    val browse by browseViewModel.uiState.collectAsStateWithLifecycle()
    val combinations by combinationViewModel.uiState.collectAsStateWithLifecycle()
    val budget by budgetViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentTab = OrderTab.entries.firstOrNull { it.route() == currentRoute }

    Scaffold(
        bottomBar = {
            if (currentTab != null) {
                Column {
                    if (currentTab == OrderTab.MENU || currentTab == OrderTab.CART) {
                        BudgetSummary(budget, order.totalPrice) { navController.navigate(Destination.BUDGET) { launchSingleTop = true } }
                    }
                    BottomBar(currentTab, order.totalQuantity) { tab ->
                        navController.navigate(tab.route()) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.RESTAURANT,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Destination.RESTAURANT) {
                RestaurantScreen(
                    isFavorite = browse.isRestaurantFavorite,
                    onToggleFavorite = browseViewModel::toggleFavorite,
                )
            }
            composable(Destination.MENU) {
                MenuScreen(
                    selectedCategory = browse.selectedCategory,
                    order = order,
                    onCategorySelected = browseViewModel::selectCategory,
                    onIncrease = orderViewModel::increase,
                    onDecrease = orderViewModel::decrease,
                )
            }
            composable(Destination.CART) {
                CartScreen(
                    order = order,
                    onIncrease = orderViewModel::increase,
                    onDecrease = orderViewModel::decrease,
                    onClear = orderViewModel::clearCart,
                    onCheckout = {
                        val price = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
                        Toast.makeText(context, "${price}원 결제 완료!", Toast.LENGTH_SHORT).show()
                    },
                    onNavigateToCombinations = { navController.navigate(Destination.COMBINATIONS) },
                    onShowSaveDialog = orderViewModel::showSaveDialog,
                    onNavigateToRecommendations = {
                        navController.navigate(Destination.RECOMMENDATIONS) { launchSingleTop = true }
                    },
                )
            }
            composable(Destination.COMBINATIONS) {
                CombinationScreen(
                    state = combinations,
                    onBack = { navController.popBackStack() },
                    onAddToCart = { quantities ->
                        orderViewModel.addCombination(quantities)
                        Toast.makeText(context, "장바구니에 담았어요.", Toast.LENGTH_SHORT).show()
                    },
                    onDelete = combinationViewModel::delete,
                    onRetry = combinationViewModel::observeCombinations,
                )
            }
            composable(Destination.BUDGET) {
                BudgetScreen(
                    state = budget,
                    onBack = { navController.popBackStack() },
                    onSave = { budgetViewModel.save(it) { navController.popBackStack() } },
                    onClear = { budgetViewModel.clear { navController.popBackStack() } },
                    onRetry = budgetViewModel::observeBudget,
                )
            }
            composable(Destination.RECOMMENDATIONS) {
                recommendations(
                    { quantities ->
                        orderViewModel.addCombination(quantities)
                        Toast.makeText(context, "장바구니에 담았어요.", Toast.LENGTH_SHORT).show()
                    },
                    { navController.popBackStack() },
                )
            }
        }
        if (order.isSaveDialogOpen) {
            SaveCombinationDialog(
                order = order,
                errorMessage = order.saveErrorMessage,
                isSaving = combinations.isSaving,
                onDismiss = orderViewModel::hideSaveDialog,
                onSave = { name ->
                    combinationViewModel.save(name, order.quantities, orderViewModel::onSaveResult)
                },
            )
        }
    }
}
