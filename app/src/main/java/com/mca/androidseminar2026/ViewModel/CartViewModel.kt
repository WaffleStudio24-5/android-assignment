package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.uistate.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    fun increase(menuId: Int) = _uiState.update { state ->
        state.copy(quantities = state.quantities + (menuId to (state.quantities[menuId] ?: 0) + 1))
    }

    fun decrease(menuId: Int) = _uiState.update { state ->
        val quantity = (state.quantities[menuId] ?: 0) - 1
        state.copy(
            quantities = if (quantity <= 0) state.quantities - menuId else state.quantities + (menuId to quantity),
        )
    }

    fun clear() = _uiState.update { CartUiState() }
}
