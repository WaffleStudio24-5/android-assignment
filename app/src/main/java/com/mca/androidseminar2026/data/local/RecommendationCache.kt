package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "recommendation_cache")
data class RecommendationCacheEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val quantitiesText: String,
    val position: Int,
)

@Entity(tableName = "recommendation_metadata")
data class RecommendationMetadataEntity(@PrimaryKey val id: Int = 0, val updatedAt: Long)

@Dao
interface RecommendationCacheDao {
    @Query("SELECT * FROM recommendation_cache ORDER BY position")
    fun observeRecommendations(): Flow<List<RecommendationCacheEntity>>

    @Query("SELECT * FROM recommendation_metadata WHERE id = 0")
    fun observeMetadata(): Flow<RecommendationMetadataEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecommendationCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: RecommendationMetadataEntity)

    @Query("DELETE FROM recommendation_cache")
    suspend fun clearRecommendations()

    @Transaction
    suspend fun replaceAll(items: List<RecommendationCacheEntity>, updatedAt: Long) {
        clearRecommendations()
        insertAll(items)
        insertMetadata(RecommendationMetadataEntity(updatedAt = updatedAt))
    }
}

@Database(entities = [RecommendationCacheEntity::class, RecommendationMetadataEntity::class], version = 1, exportSchema = false)
abstract class RecommendationCacheDatabase : RoomDatabase() {
    abstract fun recommendationCacheDao(): RecommendationCacheDao
}

fun Map<Int, Int>.toRecommendationText(): String = entries.sortedBy { it.key }.joinToString(",") { "${it.key}:${it.value}" }
fun String.toRecommendationQuantities(): Map<Int, Int> = split(',').mapNotNull {
    val parts = it.split(':'); if (parts.size != 2) null else parts[0].toIntOrNull()?.let { id -> parts[1].toIntOrNull()?.let { quantity -> id to quantity } }
}.toMap()
