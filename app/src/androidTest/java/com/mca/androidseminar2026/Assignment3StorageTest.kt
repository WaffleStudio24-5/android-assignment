package com.mca.androidseminar2026

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mca.androidseminar2026.data.local.CombinationDatabase
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Assignment3StorageTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun duplicateSavesAreAtomic() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, CombinationDatabase::class.java).build()
        try {
            val dao = db.combinationDao()
            val results = coroutineScope {
                (1..8).map { async(Dispatchers.IO) { dao.save("버거 세트", mapOf(1 to 1, 3 to 2)) } }.awaitAll()
            }
            assertEquals(1, results.count { it })
            val stored = dao.observeAll().first()
            assertEquals(1, stored.size)
            assertEquals(mapOf(1 to 1, 3 to 2), stored.single().toModel().quantities)
        } finally { db.close() }
    }

    @Test
    fun combinationsSurviveReopenAndDeleteIndependently() = runBlocking {
        val name = "assignment3-test-${UUID.randomUUID()}.db"
        var db = Room.databaseBuilder(context, CombinationDatabase::class.java, name).build()
        try {
            db.combinationDao().save("첫 조합", mapOf(1 to 1))
            db.combinationDao().save("다음 조합", mapOf(3 to 2))
            db.close()
            db = Room.databaseBuilder(context, CombinationDatabase::class.java, name).build()
            val rows = db.combinationDao().observeAll().first()
            assertEquals(listOf("다음 조합", "첫 조합"), rows.map { it.combination.name })
            db.combinationDao().delete(rows.first().combination.id)
            assertEquals("첫 조합", db.combinationDao().observeAll().first().single().combination.name)
            db.openHelper.readableDatabase.query("SELECT COUNT(*) FROM combination_items").use {
                it.moveToFirst()
                assertEquals(1, it.getInt(0))
            }
        } finally {
            db.close()
            context.deleteDatabase(name)
        }
    }

    @Test
    fun migratesStarterDatabase() = runBlocking {
        val name = "assignment3-migration-${UUID.randomUUID()}.db"
        val file = context.getDatabasePath(name)
        file.parentFile?.mkdirs()
        SQLiteDatabase.openOrCreateDatabase(file, null).use { old ->
            old.execSQL("CREATE TABLE saved_combinations (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)")
            old.execSQL("INSERT INTO saved_combinations(name) VALUES ('기존 조합')")
            old.version = 1
        }
        val db = Room.databaseBuilder(context, CombinationDatabase::class.java, name)
            .addMigrations(CombinationDatabase.MIGRATION_1_2).build()
        try {
            assertEquals("기존 조합", db.combinationDao().observeAll().first().single().combination.name)
            assertTrue(db.combinationDao().save("새 조합", mapOf(1 to 1, 3 to 2)))
        } finally {
            db.close()
            context.deleteDatabase(name)
        }
    }
}
