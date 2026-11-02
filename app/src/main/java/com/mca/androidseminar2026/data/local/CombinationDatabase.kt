package com.mca.androidseminar2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** 과제 3의 저장한 조합 DB입니다. */
@Database(
    entities = [CombinationEntity::class, CombinationItemEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class CombinationDatabase : RoomDatabase() {
    /** 저장한 조합을 읽고 쓰는 DAO를 제공합니다. */
    abstract fun combinationDao(): CombinationDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            /** 과제 3 시작 코드의 DB에 메뉴별 수량 테이블과 이름 제약을 추가합니다. */
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE UNIQUE INDEX index_saved_combinations_name ON saved_combinations(name)")
                db.execSQL("CREATE TABLE IF NOT EXISTS combination_items (combinationId INTEGER NOT NULL, menuId INTEGER NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY(combinationId, menuId), FOREIGN KEY(combinationId) REFERENCES saved_combinations(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
            }
        }
    }
}
