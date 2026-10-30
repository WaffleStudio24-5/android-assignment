package com.mca.androidseminar2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CombinationEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class CombinationDatabase : RoomDatabase() {
    abstract fun combinationDao(): CombinationDao
}
