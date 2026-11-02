package com.mca.androidseminar2026.uistate

/** null은 예산 미설정이고 0원은 설정된 예산입니다. */
data class BudgetUiState(
    val amount: Long? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)
