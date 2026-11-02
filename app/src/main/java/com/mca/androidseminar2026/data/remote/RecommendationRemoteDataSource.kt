package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination

/** 실제 통신과 Fake 구현이 지킬 약속입니다. */
interface RecommendationRemoteDataSource {
    /** 검증을 마친 추천 목록을 반환하며 실패는 예외로 전달합니다. */
    suspend fun getRecommendations(): List<RecommendedCombination>
}
