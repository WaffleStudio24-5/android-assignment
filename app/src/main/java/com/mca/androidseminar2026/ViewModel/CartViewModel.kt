package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.uistate.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private fun restoreQuantities(): Map<Int, Int> {
        val saved = savedStateHandle.get<IntArray>("quantities")
            ?: intArrayOf()

        return MenuData.items.mapIndexedNotNull { index, menu ->
            val quantity = saved.getOrNull(index) ?: 0

            if (quantity > 0) {
                menu.id to quantity
            } else {
                null
            }
        }.toMap()
    }

    private val _uiState = MutableStateFlow(
        CartUiState(restoreQuantities())
    )

    val uiState = _uiState.asStateFlow()

    private fun setQuantity(menuId: Int, quantity: Int) {
        if (MenuData.items.none { it.id == menuId }) return

        val updated = _uiState.value.quantities.toMutableMap()

        if (quantity <= 0) {
            updated.remove(menuId)
        } else {
            updated[menuId] = quantity
        }

        savedStateHandle["quantities"] = MenuData.items.map {
            updated[it.id] ?: 0
        }.toIntArray()

        _uiState.value = CartUiState(updated)
    }

    fun increase(menuId: Int) {
        setQuantity(
            menuId,
            _uiState.value.quantityOf(menuId) + 1
        )
    }

    fun decrease(menuId: Int) {
        setQuantity(
            menuId,
            _uiState.value.quantityOf(menuId) - 1
        )
    }

    fun clearCart() {
        savedStateHandle["quantities"] = IntArray(MenuData.items.size)
        _uiState.value = CartUiState()
    }
}