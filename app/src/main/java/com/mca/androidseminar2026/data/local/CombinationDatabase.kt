package com.mca.androidseminar2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/** TODO: Entity를 추가하거나 바꾸면 entities와 버전을 함께 확인하세요. */
@Database(
    entities = [
        CombinationEntity::class,
        CombinationItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class CombinationDatabase : RoomDatabase() {
    abstract fun combinationDao(): CombinationDao
}
