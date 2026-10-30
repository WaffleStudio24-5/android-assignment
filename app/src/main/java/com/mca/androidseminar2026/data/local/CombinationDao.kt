package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CombinationDao {
    @Query("SELECT * FROM saved_combinations ORDER BY id DESC")
    fun observeAll(): Flow<List<CombinationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(combination: CombinationEntity): Long

    @Query("DELETE FROM saved_combinations WHERE id = :id")
    suspend fun deleteById(id: Long)
}
