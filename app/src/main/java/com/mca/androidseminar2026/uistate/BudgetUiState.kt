package com.mca.androidseminar2026.uistate

data class BudgetUiState(
    val amount: Long? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
