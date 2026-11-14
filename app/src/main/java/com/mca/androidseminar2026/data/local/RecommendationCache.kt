package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.mca.androidseminar2026.model.RecommendedCombination
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "recommendation_cache_info")
data class RecommendationCacheInfoEntity(
    @PrimaryKey val id: Int = 0,
    val updatedAtEpochMillis: Long,
)

@Entity(
    tableName = "recommended_combinations",
    indices = [Index(value = ["cacheId"])]
)
data class RecommendationEntity(
    @PrimaryKey val id: Long,
    val cacheId: Int = 0,
    val name: String,
    val description: String,
    val position: Int,
)

@Entity(
    tableName = "recommendation_quantities",
    primaryKeys = ["recommendationId", "menuId"],
    foreignKeys = [
        ForeignKey(
            entity = RecommendationEntity::class,
            parentColumns = ["id"],
            childColumns = ["recommendationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recommendationId"])]
)
data class RecommendationQuantityEntity(
    val recommendationId: Long,
    val menuId: Int,
    val quantity: Int,
    val position: Int,
)

data class RecommendationWithQuantities(
    @Embedded val recommendation: RecommendationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recommendationId"
    )
    val quantities: List<RecommendationQuantityEntity>
)

data class RecommendationCacheWithItems(
    @Embedded val info: RecommendationCacheInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cacheId",
        entity = RecommendationEntity::class
    )
    val recommendations: List<RecommendationWithQuantities>
)

@Dao
abstract class RecommendationDao {

    @Transaction
    @Query("SELECT * FROM recommendation_cache_info WHERE id = 0")
    abstract fun observeCache(): Flow<RecommendationCacheWithItems?>

    @Query("DELETE FROM recommendation_quantities")
    protected abstract suspend fun clearQuantities()

    @Query("DELETE FROM recommended_combinations")
    protected abstract suspend fun clearRecommendations()

    @Insert
    protected abstract suspend fun insertRecommendations(
        items: List<RecommendationEntity>
    )

    @Insert
    protected abstract suspend fun insertQuantities(
        items: List<RecommendationQuantityEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun saveCacheInfo(
        info: RecommendationCacheInfoEntity
    )

    // 추천 목록 전체와 성공 시각을 하나의 트랜잭션으로 저장
    @Transaction
    open suspend fun replaceAll(
        items: List<RecommendedCombination>,
        updatedAtEpochMillis: Long
    ) {
        clearQuantities()
        clearRecommendations()

        val entities = items.mapIndexed { index, item ->
            RecommendationEntity(
                id = item.id,
                name = item.name,
                description = item.description,
                position = index
            )
        }

        insertRecommendations(entities)

        val quantities = items.flatMap { item ->
            item.quantities.entries.mapIndexed { index, entry ->
                RecommendationQuantityEntity(
                    recommendationId = item.id,
                    menuId = entry.key,
                    quantity = entry.value,
                    position = index
                )
            }
        }

        insertQuantities(quantities)

        saveCacheInfo(
            RecommendationCacheInfoEntity(
                updatedAtEpochMillis = updatedAtEpochMillis
            )
        )
    }
}

@Database(
    entities = [
        RecommendationCacheInfoEntity::class,
        RecommendationEntity::class,
        RecommendationQuantityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RecommendationDatabase : RoomDatabase() {
    abstract fun recommendationDao(): RecommendationDao
}

