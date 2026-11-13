package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeRecommendationRemoteDataSource @Inject constructor() : RecommendationRemoteDataSource {
    private var requestCount = 0

    override suspend fun getRecommendations(): List<RecommendedCombination> {
        delay(1_000)
        return when (++requestCount) {
            1, 3, 6 -> throw java.io.IOException("Fake 연결 실패")
            2 -> initial
            4 -> updated
            5 -> emptyList()
            else -> initial
        }
    }

    private val initial = listOf(
        RecommendedCombination(101, "든든한 한 끼", "버거와 감자튀김을 함께 즐겨 보세요.", mapOf(1 to 1, 3 to 2)),
        RecommendedCombination(102, "가벼운 점심", "샐러드와 레몬에이드로 가볍게 즐겨요.", mapOf(4 to 1, 5 to 1)),
    )
    private val updated = listOf(RecommendedCombination(104, "간단한 한 끼", "핫도그와 레몬에이드를 함께 즐겨요.", mapOf(2 to 1, 5 to 1)))
}
