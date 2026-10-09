package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.model.OrderTab

data class OrderUiState(
    val selectedTab: OrderTab = OrderTab.RESTAURANT,
    val selectedCategory: MenuCategory? = null,
    val isFavorite: Boolean = false,
)

data class CartUiState(
    val quantities: Map<Int, Int> = emptyMap(),
)
