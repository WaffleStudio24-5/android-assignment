package com.mca.androidseminar2026.model

/** 원격 추천 조합의 앱 모델입니다. 메뉴 상세 정보는 MenuData에서 찾습니다. */
data class RecommendedCombination(
    val id: Long,
    val name: String,
    val description: String,
    val quantities: Map<Int, Int>,
)

/** 한 번 성공한 목록입니다. items가 비어 있어도 성공 기록은 남습니다. */
data class RecommendationSnapshot(
    val items: List<RecommendedCombination>,
    val updatedAtEpochMillis: Long,
)
