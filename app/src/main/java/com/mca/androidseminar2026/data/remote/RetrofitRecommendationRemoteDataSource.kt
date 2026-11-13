package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.model.RecommendedCombination
import com.mca.androidseminar2026.data.MenuData
import javax.inject.Inject

class RetrofitRecommendationRemoteDataSource @Inject constructor(
    private val api: RecommendationApi,
) : RecommendationRemoteDataSource {
    override suspend fun getRecommendations(): List<RecommendedCombination> {
        val response = api.getRecommendations()
        if (!response.isSuccessful) throw java.io.IOException("HTTP ${response.code()}")
        val body = response.body() ?: throw java.io.IOException("응답 본문이 없어요.")
        val validMenuIds = MenuData.items.map { it.id }.toSet()
        val result = body.map { dto ->
            val value = dto ?: throw IllegalArgumentException("빈 항목")
            val items = value.items ?: throw IllegalArgumentException("구성 메뉴 없음")
            val id = value.id?.takeIf { it > 0 } ?: throw IllegalArgumentException("잘못된 ID")
            val name = value.name?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("이름 없음")
            val description = value.description?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("설명 없음")
            if (items.isEmpty()) throw IllegalArgumentException("빈 구성")
            val quantities = items.associate { item ->
                val menuId = item?.menuId?.takeIf { it in validMenuIds } ?: throw IllegalArgumentException("잘못된 메뉴")
                val quantity = item.quantity?.takeIf { it in 1..99 } ?: throw IllegalArgumentException("잘못된 수량")
                menuId to quantity
            }
            if (quantities.size != items.size) throw IllegalArgumentException("중복 메뉴")
            RecommendedCombination(id, name, description, quantities)
        }
        if (result.map { it.id }.toSet().size != result.size) throw IllegalArgumentException("중복 ID")
        return result
    }
}
