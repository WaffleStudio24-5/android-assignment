package com.mca.androidseminar2026.di

import com.mca.androidseminar2026.data.remote.RecommendationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** 제공 코드: HTTP 요청 객체와 JSON 변환기를 연결합니다. */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://raw.githubusercontent.com/WaffleStudio24-5/android-assignment/assignment4-data/data/assignment4/"

    /** 요청마다 앱 식별 헤더를 추가하고 연결 대기 시간을 제한합니다. */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder().header("X-Client", "Android").build()
            val startedAt = System.nanoTime()
            Log.d("RecommendationNetwork", "${request.method} ${request.url}")
            try {
                chain.proceed(request).also { response ->
                    Log.d("RecommendationNetwork", "${response.code} ${((System.nanoTime() - startedAt) / 1_000_000)}ms")
                }
            } catch (e: Exception) {
                Log.d("RecommendationNetwork", "실패 ${((System.nanoTime() - startedAt) / 1_000_000)}ms: ${e.message}")
                throw e
            }
        }
        .callTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    /** JSON 응답을 DTO로 바꾸는 Retrofit 객체를 제공합니다. */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Retrofit이 구현한 API 인터페이스를 제공합니다. */
    @Provides
    @Singleton
    fun provideRecommendationApi(retrofit: Retrofit): RecommendationApi =
        retrofit.create(RecommendationApi::class.java)
}
