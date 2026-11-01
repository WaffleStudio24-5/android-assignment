package com.mca.androidseminar2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [CombinationEntity::class, CombinationItemEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class CombinationDatabase : RoomDatabase() {
    abstract fun combinationDao(): CombinationDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE UNIQUE INDEX index_saved_combinations_name ON saved_combinations(name)")
                db.execSQL("CREATE TABLE IF NOT EXISTS combination_items (combinationId INTEGER NOT NULL, menuId INTEGER NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY(combinationId, menuId), FOREIGN KEY(combinationId) REFERENCES saved_combinations(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
            }
        }
    }
}
