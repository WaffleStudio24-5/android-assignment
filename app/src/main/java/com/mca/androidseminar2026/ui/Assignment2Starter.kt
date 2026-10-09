package com.mca.androidseminar2026.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mca.androidseminar2026.ViewModel.CartViewModel
import com.mca.androidseminar2026.ViewModel.OrderViewModel
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.model.OrderTab

@Composable
fun Assignment2Starter() {
    val orderViewModel: OrderViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val orderState by orderViewModel.uiState.collectAsStateWithLifecycle()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val totalQuantity = cartState.quantities.values.sum()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                selectedTab = orderState.selectedTab,
                totalQuantity = totalQuantity,
                onTabSelected = orderViewModel::selectTab,
            )
        },
    ) { padding ->
        when (orderState.selectedTab) {
            OrderTab.RESTAURANT -> RestaurantScreen(
                isFavorite = orderState.isFavorite,
                onFavoriteClick = orderViewModel::toggleFavorite,
                modifier = Modifier.fillMaxSize().padding(padding),
            )

            OrderTab.MENU -> MenuScreen(
                selectedCategory = orderState.selectedCategory,
                quantities = cartState.quantities,
                onCategorySelected = orderViewModel::selectCategory,
                onIncrease = cartViewModel::increase,
                onDecrease = cartViewModel::decrease,
                modifier = Modifier.fillMaxSize().padding(padding),
            )

            OrderTab.CART -> CartScreen(
                quantities = cartState.quantities,
                onIncrease = cartViewModel::increase,
                onDecrease = cartViewModel::decrease,
                onClear = cartViewModel::clear,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        }
    }
}
