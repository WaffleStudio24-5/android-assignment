package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.RecommendedCombination

/** TODO: 첫 조회·갱신·캐시·빈 목록·오류를 표현할 상태를 설계하세요. */
data class RecommendationUiState(
    val recommendations: List<RecommendedCombination> = emptyList(),
)
