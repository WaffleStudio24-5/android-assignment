package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CombinationDao {
    @Transaction
    @Query("SELECT * FROM saved_combinations ORDER BY id DESC")
    abstract fun observeAll(): Flow<List<CombinationWithItems>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCombination(combination: CombinationEntity): Long

    @Insert
    abstract suspend fun insertItems(items: List<CombinationItemEntity>)

    @Transaction
    open suspend fun save(name: String, quantities: Map<Int, Int>): Boolean {
        val id = insertCombination(CombinationEntity(name = name))
        if (id == -1L) return false
        insertItems(quantities.map { (menuId, quantity) -> CombinationItemEntity(id, menuId, quantity) })
        return true
    }

    @Query("DELETE FROM saved_combinations WHERE id = :id")
    abstract suspend fun delete(id: Long)
}
