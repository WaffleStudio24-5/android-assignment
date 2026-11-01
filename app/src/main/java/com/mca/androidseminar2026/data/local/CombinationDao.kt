package com.mca.androidseminar2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/** TODO: 직접 설계한 Entity에 맞는 조회·저장·삭제 함수를 선언하세요. */
@Dao
interface CombinationDao {

    /** 전체 조합과 각 조합에 속한 항목들을 1:N 관계형으로 조회 */
    @Transaction
    @Query("SELECT * FROM saved_combinations ORDER BY id DESC")
    fun getAllCombinations(): Flow<List<CombinationWithItems>>

    /** 공백을 제외한 이름이 중복되는지 확인하기 위한 조회 */
    @Query("SELECT * FROM saved_combinations WHERE TRIM(name) = TRIM(:name) LIMIT 1")
    suspend fun getCombinationByName(name: String): CombinationEntity?

    @Insert
    suspend fun insertCombination(entity: CombinationEntity): Long

    @Insert
    suspend fun insertCombinationItems(items: List<CombinationItemEntity>)

    /** 조합 테이블과 항목 테이블에 동시에 안전하게 트랜잭션으로 저장 */
    @Transaction
    suspend fun insertCombinationWithItems(name: String, quantities: Map<Int, Int>): Long {
        val trimmedName = name.trim()
        val combinationId = insertCombination(CombinationEntity(name = trimmedName))
        val itemEntities = quantities.map { (menuId, qty) ->
            CombinationItemEntity(
                combinationId = combinationId,
                menuId = menuId,
                quantity = qty
            )
        }
        insertCombinationItems(itemEntities)
        return combinationId
    }

    /** 조합 삭제 (외래키 CASCADE 옵션으로 관련 항목도 자동 삭제) */
    @Query("DELETE FROM saved_combinations WHERE id = :id")
    suspend fun deleteCombinationById(id: Long)
}
