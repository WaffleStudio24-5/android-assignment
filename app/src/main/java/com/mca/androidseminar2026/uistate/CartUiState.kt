package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.data.MenuData

data class CartUiState(
    val quantities: Map<Int, Int> = emptyMap()
) {
    fun quantityOf(menuId: Int): Int {
        return quantities[menuId] ?: 0
    }

    val totalQuantity: Int
        get() = quantities.values.sum()

    val totalPrice: Long
        get() = MenuData.items.sumOf { menu ->
            menu.price.toLong() * quantityOf(menu.id)
        }
}