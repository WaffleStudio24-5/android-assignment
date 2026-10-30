package com.mca.androidseminar2026.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.data.local.CombinationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Room DB와 DAO를 Hilt로 제공합니다. */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideCombinationDatabase(@ApplicationContext context: Context): CombinationDatabase =
        Room.databaseBuilder(context, CombinationDatabase::class.java, "saved-combinations.db")
            .addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE saved_combinations ADD COLUMN quantitiesText TEXT NOT NULL DEFAULT ''")
                }
            })
            .build()

    @Provides
    fun provideCombinationDao(database: CombinationDatabase): CombinationDao =
        database.combinationDao()
}
