package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.RecommendedCombination

data class RecommendationUiState(
    val recommendations: List<RecommendedCombination> = emptyList(),

    val isCacheLoaded: Boolean = false,

    val hasSuccessfulCache: Boolean = false,

    val isRefreshing: Boolean = false,

    val lastUpdatedAtEpochMillis: Long? = null,

    val errorMessage: String? = null,
)
