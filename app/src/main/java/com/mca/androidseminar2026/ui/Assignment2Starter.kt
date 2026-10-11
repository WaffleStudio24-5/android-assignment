package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mca.androidseminar2026.ViewModel.OrderViewModel
import com.mca.androidseminar2026.model.OrderTab

@Composable
fun Assignment2Starter(
    viewModel: OrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedTab = uiState.selectedTab,
                totalQuantity = uiState.totalQuantity,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState.selectedTab) {
                OrderTab.RESTAURANT -> RestaurantScreen(
                    isFavorite = uiState.isFavorite,
                    onFavoriteClick = { viewModel.toggleFavorite() }
                )
                OrderTab.MENU -> MenuScreen(viewModel = viewModel)
                OrderTab.CART -> CartScreen(viewModel = viewModel)
            }
        }
    }
}
