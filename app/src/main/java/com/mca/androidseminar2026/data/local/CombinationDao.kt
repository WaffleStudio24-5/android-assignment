package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CombinationDao {

    @Query("SELECT * FROM saved_combinations ORDER BY id DESC")
    fun getAllCombinations(): Flow<List<CombinationEntity>>

    @Query("SELECT * FROM saved_combinations WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): CombinationEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCombination(
        combination: CombinationEntity
    ): Long

    @Query("DELETE FROM saved_combinations WHERE id = :id")
    suspend fun deleteCombination(id: Long): Int
}
