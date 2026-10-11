package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.MenuItem
import com.mca.androidseminar2026.model.OrderTab

data class OrderUiState(
    // 1. 기본 상태
    val selectedTab: OrderTab = OrderTab.RESTAURANT,
    val isFavorite: Boolean = false,
    val selectedCategory: MenuCategory? = null,
    val isRefreshing: Boolean = false,
    val quantities: Map<Int, Int> = emptyMap() // key: menuId, value: 수량
) {
    // 2. 파생 상태 (편의 프로퍼티)

    // 장바구니 전체 상품 수량 합계 (BottomBar용)
    val totalQuantity: Int
        get() = quantities.values.sum()

    // 장바구니에 담긴 메뉴 목록 (수량이 1 이상인 메뉴만 필터링)
    val cartItems: List<Pair<MenuItem, Int>>
        get() = MenuData.items
            .filter { (quantities[it.id] ?: 0) > 0 }
            .map { menu -> menu to (quantities[menu.id] ?: 0) }

    // 총 금액
    val totalPrice: Long
        get() = cartItems.sumOf { (menu, quantity) -> (menu.price * quantity).toLong() }

    // 장바구니가 비어 있는지 여부
    val isCartEmpty: Boolean
        get() = quantities.isEmpty()
}