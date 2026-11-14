package com.mca.androidseminar2026.di

import android.util.Log
import com.mca.androidseminar2026.data.remote.RecommendationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL =
        "https://raw.githubusercontent.com/WaffleStudio24-5/android-assignment/assignment4-data/data/assignment4/"

    private const val TAG = "RecommendationHTTP"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .header("X-Client", "Android")
                .build()

            val startTime = System.nanoTime()

            Log.d(
                TAG,
                "Request: ${request.method} ${request.url}"
            )

            try {
                val response = chain.proceed(request)

                val elapsedMs = TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - startTime
                )

                Log.d(
                    TAG,
                    "Response: HTTP ${response.code}, ${elapsedMs}ms"
                )

                response

            } catch (error: Exception) {

                val elapsedMs = TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - startTime
                )

                Log.e(
                    TAG,
                    "Request failed: ${error.message}, ${elapsedMs}ms",
                    error
                )

                throw error
            }
        }
        .callTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRecommendationApi(
        retrofit: Retrofit
    ): RecommendationApi =
        retrofit.create(RecommendationApi::class.java)
}
