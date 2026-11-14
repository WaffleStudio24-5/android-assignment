package com.mca.androidseminar2026.data

import com.mca.androidseminar2026.data.local.RecommendationDao
import com.mca.androidseminar2026.data.remote.RecommendationRemoteDataSource
import com.mca.androidseminar2026.model.RecommendedCombination
import com.mca.androidseminar2026.model.RecommendationSnapshot
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecommendationRepository @Inject constructor(
    private val remote: RecommendationRemoteDataSource,
    private val local: RecommendationDao,
) {

    fun observeCache(): Flow<RecommendationSnapshot?> {
        return local.observeCache().map { cache ->
            cache?.let {
                RecommendationSnapshot(
                    items = it.recommendations
                        .sortedBy { entry -> entry.recommendation.position }
                        .map { entry ->
                            RecommendedCombination(
                                id = entry.recommendation.id,
                                name = entry.recommendation.name,
                                description = entry.recommendation.description,
                                quantities = entry.quantities
                                    .sortedBy { quantity -> quantity.position }
                                    .associate { quantity ->
                                        quantity.menuId to quantity.quantity
                                    }
                            )
                        },
                    updatedAtEpochMillis = it.info.updatedAtEpochMillis
                )
            }
        }
    }

    suspend fun refresh() {
        val recommendations = remote.getRecommendations()

        local.replaceAll(
            items = recommendations,
            updatedAtEpochMillis = System.currentTimeMillis()
        )
    }
}
