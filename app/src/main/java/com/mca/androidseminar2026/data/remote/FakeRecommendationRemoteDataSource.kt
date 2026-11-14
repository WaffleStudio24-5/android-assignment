
package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRecommendationRemoteDataSource @Inject constructor() :
    RecommendationRemoteDataSource {

    private var requestCount = 0

    private val initialRecommendations = listOf(
        RecommendedCombination(
            id = 101,
            name = "든든한 한 끼",
            description = "버거와 감자튀김을 함께 즐겨 보세요.",
            quantities = mapOf(1 to 1, 3 to 2)
        ),
        RecommendedCombination(
            id = 102,
            name = "가벼운 점심",
            description = "샐러드와 레몬에이드로 가볍게 즐겨요.",
            quantities = mapOf(4 to 1, 5 to 1)
        ),
        RecommendedCombination(
            id = 103,
            name = "둘이 함께",
            description = "버거와 핫도그를 나누어 먹어요.",
            quantities = mapOf(1 to 1, 2 to 1, 3 to 1, 6 to 2)
        )
    )

    private val updatedRecommendations = listOf(
        RecommendedCombination(
            id = 201,
            name = "새로운 추천 조합",
            description = "핫도그와 샐러드를 함께 즐겨 보세요.",
            quantities = mapOf(2 to 1, 4 to 1)
        )
    )

    override suspend fun getRecommendations(): List<RecommendedCombination> {
        requestCount++
        val currentRequest = requestCount

        delay(1000)

        return when (currentRequest) {
            1, 3, 6 -> throw IOException(
                "Fake network failure: request $currentRequest"
            )
            2 -> initialRecommendations
            4 -> updatedRecommendations
            5 -> emptyList()
            else -> initialRecommendations
        }
    }
}
