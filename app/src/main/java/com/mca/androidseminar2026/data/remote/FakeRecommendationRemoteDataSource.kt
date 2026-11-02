package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import javax.inject.Inject

/** TODO: 요청 순서에 따라 assignment4.md의 상황을 재현하세요. 실제 실패 시 자동 대체용이 아닙니다. */
class FakeRecommendationRemoteDataSource @Inject constructor() : RecommendationRemoteDataSource {
    private var requestCount = 0

    /** TODO: 1초 대기 후 요청 횟수에 맞는 결과를 반환하세요. */
    override suspend fun getRecommendations(): List<RecommendedCombination> {
        TODO("Fake 시나리오를 구현하세요.")
    }
}
