package com.mca.androidseminar2026.data.local

import androidx.room.Entity
import androidx.room.Embedded
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mca.androidseminar2026.model.Combination

@Entity(tableName = "saved_combinations", indices = [Index(value = ["name"], unique = true)])
data class CombinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

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

data class CombinationWithItems(
    @Embedded val combination: CombinationEntity,
    @Relation(parentColumn = "id", entityColumn = "combinationId")
    val items: List<CombinationItemEntity>,
) {
    fun toModel() = Combination(combination.id, combination.name, items.associate { it.menuId to it.quantity })
}
