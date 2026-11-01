package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.local.BudgetDataStore
import com.mca.androidseminar2026.uistate.OrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 장바구니 및 예산 관리를 담당하는 ViewModel */
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val budgetDataStore: BudgetDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            budgetDataStore.budgetFlow.collect { budget ->
                _uiState.update { it.copy(budget = budget) }
            }
        }
    }

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

    fun setSaveErrorMessage(message: String?) {
        _uiState.update { it.copy(saveErrorMessage = message) }
    }

    /** 장바구니에 사용자 설정 조합 수량을 더합니다. */
    fun addCombinationQuantities(quantities: Map<Int, Int>) {
        _uiState.update { current ->
            val updated = current.quantities.toMutableMap()
            quantities.forEach { (menuId, qty) ->
                if (qty > 0) {
                    val currentQty = updated[menuId] ?: 0
                    updated[menuId] = currentQty + qty
                }
            }
            current.copy(quantities = updated)
        }
    }

    /* --- 예산 관련 메서드 --- */

    fun showBudgetDialog() {
        _uiState.update { it.copy(isBudgetDialogOpen = true) }
    }

    fun hideBudgetDialog() {
        _uiState.update { it.copy(isBudgetDialogOpen = false) }
    }

    fun updateBudget(amount: Int) {
        viewModelScope.launch {
            budgetDataStore.setBudget(amount)
            hideBudgetDialog()
        }
    }

    /** 주문 완료 시 현재 장바구니 합계를 예산 잔액에서 차감하고 장바구니를 비웁니다. */
    fun checkout(onCheckoutSuccess: (Long) -> Unit) {
        val currentTotal = _uiState.value.totalPrice
        if (_uiState.value.totalQuantity == 0) return

        viewModelScope.launch {
            val newBudget = _uiState.value.budget - currentTotal.toInt()
            budgetDataStore.setBudget(newBudget)
            clearCart()
            onCheckoutSuccess(currentTotal)
        }
    }
}
