package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.data.MenuCategory

/** 과제 2에서 완성한 가게 찜과 메뉴 필터 상태. */
data class BrowseUiState(
    val isRestaurantFavorite: Boolean = false,
    val selectedCategory: MenuCategory? = null,
)
