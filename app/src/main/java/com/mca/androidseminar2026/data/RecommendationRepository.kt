package com.mca.androidseminar2026.data

import com.mca.androidseminar2026.data.remote.RecommendationRemoteDataSource
import com.mca.androidseminar2026.model.RecommendationSnapshot
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** TODO: 로컬 조회와 원격 갱신 규칙을 구현하고 로컬 DataSource를 주입받으세요. */
class RecommendationRepository @Inject constructor(
    private val remote: RecommendationRemoteDataSource,
) {
    /** TODO: Room을 관찰하세요. null은 성공한 캐시가 아직 없다는 뜻입니다. */
    fun observeCache(): Flow<RecommendationSnapshot?> {
        TODO("Room 조회를 연결하세요.")
    }

    /** TODO: 원격 목록을 받아 검증이 끝난 결과와 성공 시각을 함께 저장하세요. */
    suspend fun refresh() {
        TODO("원격 갱신과 캐시 저장을 연결하세요.")
    }
}
