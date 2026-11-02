package com.mca.androidseminar2026.di

import com.mca.androidseminar2026.data.remote.RecommendationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
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
            // TODO: 요청 메서드·URL, 응답 코드·소요 시간(ms), 연결 실패 원인을 Logcat에 기록하세요.
            // 실패 예외는 다시 전달하고 아래 헤더 추가 동작은 유지하세요. 본문을 읽을 필요는 없습니다.
            chain.proceed(chain.request().newBuilder().header("X-Client", "Android").build())
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
