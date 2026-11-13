package com.mca.androidseminar2026.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.data.local.CombinationDatabase
import com.mca.androidseminar2026.data.local.RecommendationCacheDao
import com.mca.androidseminar2026.data.local.RecommendationCacheDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.budgetDataStore by preferencesDataStore(name = "meal-budget")

/** Room DB와 DAO를 Hilt로 제공 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    /** 기존 조합을 보존하면서 DB를 엽니다. */
    @Provides
    @Singleton
    fun provideCombinationDatabase(@ApplicationContext context: Context): CombinationDatabase =
        Room.databaseBuilder(context, CombinationDatabase::class.java, "saved-combinations.db")
            .addMigrations(CombinationDatabase.MIGRATION_1_2)
            .build()

    /** 저장한 조합 DAO를 주입합니다. */
    @Provides
    fun provideCombinationDao(database: CombinationDatabase): CombinationDao =
        database.combinationDao()

    @Provides @Singleton
    fun provideRecommendationDatabase(@ApplicationContext context: Context): RecommendationCacheDatabase =
        Room.databaseBuilder(context, RecommendationCacheDatabase::class.java, "recommendation-cache.db").build()

    @Provides
    fun provideRecommendationDao(database: RecommendationCacheDatabase): RecommendationCacheDao = database.recommendationCacheDao()

    /** 앱에서 한 인스턴스의 예산 DataStore를 공유합니다. */
    @Provides
    @Singleton
    fun provideBudgetDataStore(@ApplicationContext context: Context) = context.budgetDataStore
}
