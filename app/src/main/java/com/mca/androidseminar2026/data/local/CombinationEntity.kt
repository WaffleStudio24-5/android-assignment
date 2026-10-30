package com.mca.androidseminar2026.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** 메뉴 수량은 작은 Map이므로 한 행의 문자열로 저장합니다. */
@Entity(tableName = "saved_combinations", indices = [Index(value = ["name"], unique = true)])
data class CombinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantitiesText: String,
)

fun Map<Int, Int>.toQuantitiesText(): String =
    entries.filter { it.value > 0 }.sortedBy { it.key }.joinToString(",") { "${it.key}:${it.value}" }

fun String.toQuantities(): Map<Int, Int> =
    split(',').mapNotNull { part ->
        val (id, quantity) = part.split(':').let { if (it.size == 2) it else return@mapNotNull null }
        id.toIntOrNull()?.let { menuId -> quantity.toIntOrNull()?.takeIf { it > 0 }?.let { menuId to it } }
    }.toMap()
