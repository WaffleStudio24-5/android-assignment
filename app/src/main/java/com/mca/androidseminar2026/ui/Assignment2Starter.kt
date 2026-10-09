
package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mca.androidseminar2026.ViewModel.CartViewModel
import com.mca.androidseminar2026.ViewModel.OrderViewModel
import com.mca.androidseminar2026.model.OrderTab

@Composable
fun Assignment2Starter() {
    val orderViewModel: OrderViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    val orderUiState by orderViewModel.uiState.collectAsStateWithLifecycle()
    val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                selectedTab = orderUiState.selectedTab,
                totalQuantity = cartUiState.totalQuantity,
                onTabSelected = { tab ->
                    orderViewModel.selectTab(tab)
                }
            )
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (orderUiState.selectedTab) {
                OrderTab.RESTAURANT -> {
                    RestaurantScreen(
                        isFavorite = orderUiState.isFavorite,
                        onFavoriteClick = {
                            orderViewModel.toggleFavorite()
                        }
                    )
                }

                OrderTab.MENU -> {
                    MenuScreen(
                        selectedCategory = orderUiState.selectedCategory,
                        cartUiState = cartUiState,
                        onCategorySelected = { category ->
                            orderViewModel.selectCategory(category)
                        },
                        onIncrease = { menuId ->
                            cartViewModel.increase(menuId)
                        },
                        onDecrease = { menuId ->
                            cartViewModel.decrease(menuId)
                        }
                    )
                }

                OrderTab.CART -> {
                    CartScreen(
                        cartUiState = cartUiState,
                        onIncrease = { menuId ->
                            cartViewModel.increase(menuId)
                        },
                        onDecrease = { menuId ->
                            cartViewModel.decrease(menuId)
                        },
                        onClearCart = {
                            cartViewModel.clearCart()
                        }
                    )
                }
            }
        }
    }
}
