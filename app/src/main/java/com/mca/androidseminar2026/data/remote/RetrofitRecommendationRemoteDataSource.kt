package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import javax.inject.Inject

/** TODO: 실제 요청과 DTO 검증·앱 모델 변환을 담당합니다. */
class RetrofitRecommendationRemoteDataSource @Inject constructor(
    private val api: RecommendationApi,
) : RecommendationRemoteDataSource {
    /** TODO: 응답 전체를 검증하고 메뉴별 수량 맵으로 변환하세요. */
    override suspend fun getRecommendations(): List<RecommendedCombination> {
        TODO("API 호출과 응답 변환을 구현하세요.")
    }
}
