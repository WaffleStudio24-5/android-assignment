package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import javax.inject.Inject

enum class RecommendationScenario {
    SUCCESS, EMPTY, FAILURE, FAILURE_THEN_SUCCESS, SUCCESS_THEN_FAILURE, UPDATED,
}

/** TODO: assignment4.md의 여섯 상황을 재현하세요. 실제 실패 시 자동 대체용이 아닙니다. */
class FakeRecommendationRemoteDataSource @Inject constructor() : RecommendationRemoteDataSource {
    // 테스트에서 인스턴스마다 검증할 상황을 선택할 수 있습니다.
    var scenario = RecommendationScenario.SUCCESS
    private var requestCount = 0

    /** TODO: 1초 대기 후 시나리오와 요청 횟수에 맞는 결과를 반환하세요. */
    override suspend fun getRecommendations(): List<RecommendedCombination> {
        TODO("Fake 시나리오를 구현하세요.")
    }
}
