package com.mca.androidseminar2026.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room 저장 모델의 출발점. 메뉴 ID와 수량을 저장할 구조는 직접 완성하세요. */
@Entity(
    tableName = "saved_combinations",
    indices = [Index(value = ["name"], unique = true)]
)
data class CombinationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val quantitiesJson: String
)
