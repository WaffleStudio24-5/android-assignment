package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.model.OrderTab
import com.mca.androidseminar2026.uistate.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrderUiState(
            selectedTab = OrderTab.entries.find {
                it.name == savedStateHandle.get<String>("tab")
            } ?: OrderTab.RESTAURANT,

            selectedCategory = MenuCategory.entries.find {
                it.name == savedStateHandle.get<String>("category")
            },

            isFavorite = savedStateHandle.get<Boolean>("favorite") ?: false
        )
    )

    val uiState = _uiState.asStateFlow()

    fun selectTab(tab: OrderTab) {
        savedStateHandle["tab"] = tab.name
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun selectCategory(category: MenuCategory?) {
        savedStateHandle["category"] = category?.name
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun toggleFavorite() {
        val favorite = !_uiState.value.isFavorite
        savedStateHandle["favorite"] = favorite
        _uiState.value = _uiState.value.copy(isFavorite = favorite)
    }
}