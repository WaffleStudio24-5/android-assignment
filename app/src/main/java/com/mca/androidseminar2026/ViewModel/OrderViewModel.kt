package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.model.OrderTab
import com.mca.androidseminar2026.uistate.OrderUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun increaseQuantity(menuId: Int) {
        _uiState.update { current ->
            val currentQty = current.quantities[menuId] ?: 0
            val updatedMap = current.quantities + (menuId to (currentQty + 1))
            current.copy(quantities = updatedMap)
        }
    }

    fun decreaseQuantity(menuId: Int) {
        _uiState.update { current ->
            val currentQty = current.quantities[menuId] ?: 0
            val updatedMap = if (currentQty <= 1) {
                current.quantities - menuId
            } else {
                current.quantities + (menuId to (currentQty - 1))
            }
            current.copy(quantities = updatedMap)
        }
    }

    fun clearCart() {
        _uiState.update { current ->
            current.copy(quantities = emptyMap())
        }
    }

    fun selectCategory(category: MenuCategory?) {
        _uiState.update { current ->
            current.copy(selectedCategory = category)
        }
    }

    fun refreshMenu() {
        if (_uiState.value.isRefreshing) return

        _uiState.update { current ->
            current.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            try {
                delay(500)
            } finally {
                _uiState.update { current ->
                    current.copy(isRefreshing = false)
                }
            }
        }
    }

    fun selectTab(tab: OrderTab) {
        _uiState.update { current ->
            current.copy(selectedTab = tab)
        }
    }

    fun toggleFavorite() {
        _uiState.update { current ->
            current.copy(isFavorite = !current.isFavorite)
        }
    }
}
