package com.mca.androidseminar2026.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mca.androidseminar2026.model.Combination

/** Room 저장 모델의 출발점. 메뉴 ID와 수량을 저장할 구조는 직접 완성하세요. */
@Entity(tableName = "saved_combinations")
data class CombinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

/** 조합에 포함된 각 메뉴와 수량을 저장하는 엔티티 (combination_items 테이블) */
@Entity(
    tableName = "combination_items",
    foreignKeys = [
        ForeignKey(
            entity = CombinationEntity::class,
            parentColumns = ["id"],
            childColumns = ["combinationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("combinationId")]
)
data class CombinationItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val combinationId: Long,
    val menuId: Int,
    val quantity: Int,
)

/** 조합(1)과 메뉴 항목(N) 관계 조회를 위한 클래스 */
data class CombinationWithItems(
    @Embedded val combination: CombinationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "combinationId"
    )
    val items: List<CombinationItemEntity>,
) {
    fun toDomainModel(): Combination {
        return Combination(
            id = combination.id,
            name = combination.name,
            quantities = items.associate { it.menuId to it.quantity }
        )
    }
}
