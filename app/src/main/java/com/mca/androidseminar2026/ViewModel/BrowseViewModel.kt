package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.uistate.BrowseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** 제공 코드: 과제 2의 가게 찜과 메뉴 카테고리를 담당합니다. */
@HiltViewModel
class BrowseViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState = _uiState.asStateFlow()

    fun toggleFavorite() {
        _uiState.update { it.copy(isRestaurantFavorite = !it.isRestaurantFavorite) }
    }

    fun selectCategory(category: MenuCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}
