package com.mca.androidseminar2026.model

import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.MenuItem

/** 앱에서 사용하는 저장된 메뉴 조합. */
data class Combination(
    val id: Long,
    val name: String,
    val quantities: Map<Int, Int>, // 메뉴 ID -> 수량
) {
    val totalQuantity: Int
        get() = quantities.values.sum()

    val totalPrice: Long
        get() = quantities.entries.sumOf { (menuId, qty) ->
            val menu = MenuData.items.find { it.id == menuId }
            (menu?.price ?: 0).toLong() * qty
        }

    val itemsWithQuantity: List<Pair<MenuItem, Int>>
        get() = MenuData.items.mapNotNull { menu ->
            val qty = quantities[menu.id] ?: 0
            if (qty > 0) menu to qty else null
        }
}
