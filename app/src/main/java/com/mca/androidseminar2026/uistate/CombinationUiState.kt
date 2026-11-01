package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.Combination

data class CombinationUiState(
    val combinations: List<Combination> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)