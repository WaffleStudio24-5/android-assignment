package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mca.androidseminar2026.model.OrderTab
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private object Destination {
    const val RESTAURANT = "restaurant"
    const val MENU = "menu"
    const val CART = "cart"
    const val COMBINATIONS = "combinations"
}

private fun OrderTab.route(): String = when (this) {
    OrderTab.RESTAURANT -> Destination.RESTAURANT
    OrderTab.MENU -> Destination.MENU
    OrderTab.CART -> Destination.CART
}

/** Navigation 연결 예시. 화면 UI와 조합 동작은 각 화면에서 완성하세요. */
@Composable
fun Assignment3Root() {
    val navController = rememberNavController()
    val orderViewModel: OrderViewModel = hiltViewModel()
    val browseViewModel: BrowseViewModel = hiltViewModel()
    val combinationViewModel: CombinationViewModel = hiltViewModel()
    val order by orderViewModel.uiState.collectAsStateWithLifecycle()
    val browse by browseViewModel.uiState.collectAsStateWithLifecycle()
    val combinations by combinationViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentTab = OrderTab.entries.firstOrNull { it.route() == currentRoute }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            if (currentTab != null) {
                BottomBar(currentTab, order.totalQuantity) { tab ->
                    navController.navigate(tab.route()) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
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
                )
            }
            composable(Destination.COMBINATIONS) {
                CombinationScreen(
                    state = combinations,
                    onBack = { navController.popBackStack() },
                    onLoadToCart = orderViewModel::addCombinationQuantities,
                    onDeleteCombination = combinationViewModel::deleteCombination,
                )
            }
        }
        if (order.isSaveDialogOpen) {
            SaveCombinationDialog(
                errorMessage = order.saveErrorMessage,
                onDismiss = orderViewModel::hideSaveDialog,
                onSave = { name ->
                    scope.launch {
                        val result = combinationViewModel.saveCombination(name, order.quantities)
                        result.onSuccess {
                            orderViewModel.hideSaveDialog()
                        }.onFailure { error ->
                            orderViewModel.setSaveErrorMessage(error.message ?: "저장에 실패했습니다.")
                        }
                    }
                },
            )
        }
    }
}
