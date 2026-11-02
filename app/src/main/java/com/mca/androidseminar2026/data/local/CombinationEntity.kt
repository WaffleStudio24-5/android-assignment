package com.mca.androidseminar2026.data.local

import androidx.room.Entity
import androidx.room.Embedded
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mca.androidseminar2026.model.Combination

/** 이름은 저장 전에 공백을 제거하며 DB에서도 중복을 방지합니다. */
@Entity(tableName = "saved_combinations", indices = [Index(value = ["name"], unique = true)])
data class CombinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

/** 조합을 삭제하면 구성 항목도 함께 삭제합니다. */
@Entity(
    tableName = "combination_items",
    primaryKeys = ["combinationId", "menuId"],
    foreignKeys = [ForeignKey(
        entity = CombinationEntity::class,
        parentColumns = ["id"], childColumns = ["combinationId"],
        onDelete = ForeignKey.CASCADE,
    )],
)
data class CombinationItemEntity(val combinationId: Long, val menuId: Int, val quantity: Int)

/** 조합과 그 구성 항목을 한 번에 읽는 조회 결과입니다. */
data class CombinationWithItems(
    @Embedded val combination: CombinationEntity,
    @Relation(parentColumn = "id", entityColumn = "combinationId")
    val items: List<CombinationItemEntity>,
) {
    /** DB 조회 결과를 화면에서 사용하는 조합으로 바꿉니다. */
    fun toModel() = Combination(combination.id, combination.name, items.associate { it.menuId to it.quantity })
}
