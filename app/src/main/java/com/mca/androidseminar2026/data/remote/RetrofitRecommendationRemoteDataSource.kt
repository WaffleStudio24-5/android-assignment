
package com.mca.androidseminar2026.data.remote

import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.model.RecommendedCombination
import javax.inject.Inject

class RetrofitRecommendationRemoteDataSource @Inject constructor(
    private val api: RecommendationApi,
) : RecommendationRemoteDataSource {

    override suspend fun getRecommendations(): List<RecommendedCombination> {
        val response = api.getRecommendations()

        if (!response.isSuccessful) {
            throw IllegalStateException(
                "HTTP request failed: ${response.code()}"
            )
        }

        val body = response.body()
            ?: throw IllegalStateException("Response body is null")

        val validMenuIds = MenuData.items.map { it.id }.toSet()
        val usedIds = mutableSetOf<Long>()

        return body.map { dto ->
            requireNotNull(dto) { "Null recommendation" }

            val id = requireNotNull(dto.id)
            require(id > 0 && usedIds.add(id)) {
                "Invalid or duplicate recommendation ID"
            }

            val name = requireNotNull(dto.name)
            val description = requireNotNull(dto.description)

            require(name.isNotBlank() && description.isNotBlank()) {
                "Name or description is blank"
            }

            val items = requireNotNull(dto.items)
            require(items.isNotEmpty()) {
                "Recommendation has no items"
            }

            val quantities = mutableMapOf<Int, Int>()

            items.forEach { item ->
                requireNotNull(item) { "Null recommendation item" }

                val menuId = requireNotNull(item.menuId)
                val quantity = requireNotNull(item.quantity)

                require(menuId in validMenuIds) {
                    "Unknown menu ID: $menuId"
                }
                require(quantity in 1..99) {
                    "Invalid quantity: $quantity"
                }
                require(menuId !in quantities) {
                    "Duplicate menu ID: $menuId"
                }

                quantities[menuId] = quantity
            }

            RecommendedCombination(
                id = id,
                name = name,
                description = description,
                quantities = quantities
            )
        }
    }
}
