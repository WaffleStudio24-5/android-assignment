package com.mca.androidseminar2026.data.remote

import retrofit2.http.GET
import retrofit2.Response

/** HTTP로 받는 필드입니다. 누락된 값은 변환 시 검증할 수 있도록 nullable로 둡니다. */
data class RecommendationDto(
    val id: Long?,
    val name: String?,
    val description: String?,
    val items: List<RecommendationItemDto?>?,
)

data class RecommendationItemDto(val menuId: Int?, val quantity: Int?)

/** 공개 JSON 파일을 읽는 API입니다. */
interface RecommendationApi {
    /** isSuccessful로 HTTP 성공 여부를, body()로 JSON 배열을 확인합니다. */
    @GET("recommendations.json")
    suspend fun getRecommendations(): Response<List<RecommendationDto?>>
}
