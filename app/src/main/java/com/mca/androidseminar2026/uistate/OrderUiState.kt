package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.MenuItem

/** 과제 2에서 완성한 장바구니 상태. 과제 3에서도 같은 인스턴스를 화면 간 공유하며 예산 상태를 관리합니다. */
data class OrderUiState(
    val quantities: Map<Int, Int> = emptyMap(),
    val isSaveDialogOpen: Boolean = false,
    val saveErrorMessage: String? = null,
    val budget: Int = 50000,
    val isBudgetDialogOpen: Boolean = false,
) {
    val totalQuantity: Int get() = quantities.values.sum()
    val totalPrice: Long get() = MenuData.items.sumOf { menu ->
        menu.price.toLong() * (quantities[menu.id] ?: 0)
    }
    val cartItems: List<MenuItem> get() = MenuData.items.filter { (quantities[it.id] ?: 0) > 0 }

    val availableBudget: Long get() = budget.toLong() - totalPrice
    val isCartExceeded: Boolean get() = totalPrice > budget

    fun quantityOf(menuId: Int): Int = quantities[menuId] ?: 0
}
