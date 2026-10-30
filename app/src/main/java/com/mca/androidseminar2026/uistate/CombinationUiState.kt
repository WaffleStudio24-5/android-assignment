package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.Combination

data class CombinationUiState(
    val combinations: List<Combination> = emptyList(),
    val pendingDelete: Combination? = null,
)
