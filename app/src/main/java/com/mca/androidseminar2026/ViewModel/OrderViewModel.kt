package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.uistate.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** 제공 코드: 과제 2의 장바구니 동작을 담당합니다. */
@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    fun increase(menuId: Int) = changeQuantity(menuId, 1)
    fun decrease(menuId: Int) = changeQuantity(menuId, -1)

    private fun changeQuantity(menuId: Int, delta: Int) {
        if (MenuData.items.none { it.id == menuId }) return
        _uiState.update { current ->
            val next = (current.quantityOf(menuId) + delta).coerceAtLeast(0)
            current.copy(quantities = current.quantities.toMutableMap().apply {
                if (next == 0) remove(menuId) else put(menuId, next)
            })
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(quantities = emptyMap()) }
    }

    fun showSaveDialog() {
        if (_uiState.value.totalQuantity == 0) return
        _uiState.update { it.copy(isSaveDialogOpen = true, saveErrorMessage = null) }
    }

    fun hideSaveDialog() {
        _uiState.update { it.copy(isSaveDialogOpen = false, saveErrorMessage = null) }
    }


    fun addCombination(quantities: Map<Int, Int>) {
        val validMenuIds = MenuData.items.map { it.id }.toSet()

        _uiState.update { current ->
            val updated = current.quantities.toMutableMap()

            quantities.forEach { (menuId, quantity) ->
                if (menuId in validMenuIds && quantity > 0) {
                    updated[menuId] =
                        (updated[menuId] ?: 0) + quantity
                }
            }

            current.copy(quantities = updated)
        }
    }

    fun setSaveError(message: String) {
        _uiState.update {
            it.copy(saveErrorMessage = message)
        }
    }



}
