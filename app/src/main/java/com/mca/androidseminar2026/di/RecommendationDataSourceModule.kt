package com.mca.androidseminar2026.di

import com.mca.androidseminar2026.data.remote.RetrofitRecommendationRemoteDataSource
import com.mca.androidseminar2026.data.remote.RecommendationRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** 제공 코드: 먼저 Fake를 완성한 뒤 실제 통신 구현으로 바꾸어 보세요. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RecommendationDataSourceModule {
    /** 요청하는 인터페이스에 어떤 구현을 제공할지 선택합니다. */
    @Binds
    @Singleton
    abstract fun bindRecommendationRemoteDataSource(
        // 실제 통신 확인 시 RetrofitRecommendationRemoteDataSource로 바꾸세요.
        implementation: RetrofitRecommendationRemoteDataSource,
    ): RecommendationRemoteDataSource
}
