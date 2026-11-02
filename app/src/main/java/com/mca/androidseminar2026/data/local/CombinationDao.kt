package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/** 조합 이름과 구성 항목을 원자적으로 저장하고 변경을 관찰하는 DAO입니다. */
@Dao
abstract class CombinationDao {
    /** 나중에 저장한 조합 순서로 관찰합니다. */
    @Transaction
    @Query("SELECT * FROM saved_combinations ORDER BY id DESC")
    abstract fun observeAll(): Flow<List<CombinationWithItems>>

    /** 같은 이름이 이미 있으면 -1을 반환합니다. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCombination(combination: CombinationEntity): Long

    /** 조합에 속한 메뉴별 수량을 저장합니다. */
    @Insert
    abstract suspend fun insertItems(items: List<CombinationItemEntity>)

    /** 이름 중복 확인과 전체 구성 저장을 하나의 트랜잭션으로 처리합니다. */
    @Transaction
    open suspend fun save(name: String, quantities: Map<Int, Int>): Boolean {
        val id = insertCombination(CombinationEntity(name = name))
        if (id == -1L) return false
        insertItems(quantities.map { (menuId, quantity) -> CombinationItemEntity(id, menuId, quantity) })
        return true
    }

    /** 선택한 조합과 연결된 구성 항목을 삭제합니다. */
    @Query("DELETE FROM saved_combinations WHERE id = :id")
    abstract suspend fun delete(id: Long)
}
