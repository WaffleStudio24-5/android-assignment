package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.model.OrderTab
import com.mca.androidseminar2026.uistate.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(tab: OrderTab) = _uiState.update { it.copy(selectedTab = tab) }

    fun selectCategory(category: MenuCategory?) = _uiState.update { it.copy(selectedCategory = category) }

    fun toggleFavorite() = _uiState.update { it.copy(isFavorite = !it.isFavorite) }
}
