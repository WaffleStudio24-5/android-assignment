package com.mca.androidseminar2026.uistate

import com.mca.androidseminar2026.model.Combination

/** TODO: 화면에 필요한 로딩, 오류, 저장 상태 등을 추가하세요. */
data class CombinationUiState(
    val combinations: List<Combination> = emptyList(),
)
