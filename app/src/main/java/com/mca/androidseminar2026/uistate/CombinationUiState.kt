package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.Combination

/** 저장한 조합의 조회와 변경 상태입니다. */
data class CombinationUiState(
    val combinations: List<Combination> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val deletingId: Long? = null,
    val errorMessage: String? = null,
)
