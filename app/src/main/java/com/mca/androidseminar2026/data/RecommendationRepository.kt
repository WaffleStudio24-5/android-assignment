package com.mca.androidseminar2026.data

import com.mca.androidseminar2026.data.remote.RecommendationRemoteDataSource
import com.mca.androidseminar2026.data.local.RecommendationCacheDao
import com.mca.androidseminar2026.data.local.RecommendationCacheEntity
import com.mca.androidseminar2026.data.local.toRecommendationQuantities
import com.mca.androidseminar2026.data.local.toRecommendationText
import com.mca.androidseminar2026.model.RecommendedCombination
import com.mca.androidseminar2026.model.RecommendationSnapshot
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/** TODO: 로컬 조회와 원격 갱신 규칙을 구현하고 로컬 DataSource를 주입받으세요. */
class RecommendationRepository @Inject constructor(
    private val remote: RecommendationRemoteDataSource,
    private val cache: RecommendationCacheDao,
) {
    fun observeCache(): Flow<RecommendationSnapshot?> = combine(cache.observeRecommendations(), cache.observeMetadata()) { entries, metadata ->
        metadata?.let { RecommendationSnapshot(entries.map { RecommendedCombination(it.id, it.name, it.description, it.quantitiesText.toRecommendationQuantities()) }, it.updatedAt) }
    }

    /** TODO: 원격 목록을 받아 검증이 끝난 결과와 성공 시각을 함께 저장하세요. */
    suspend fun refresh() {
        val recommendations = remote.getRecommendations()
        cache.replaceAll(recommendations.mapIndexed { index, item ->
            RecommendationCacheEntity(item.id, item.name, item.description, item.quantities.toRecommendationText(), index)
        }, System.currentTimeMillis())
    }
}
