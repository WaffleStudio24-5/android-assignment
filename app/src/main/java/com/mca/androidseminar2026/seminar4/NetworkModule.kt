package com.mca.androidseminar2026.seminar4

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 과제·답안의 Retrofit/OkHttp 바인딩과 구분하기 위해 다음 코드가 들어가 있습니다.
// @SeminarNetwork 어노테이션은 무시하셔도 됩니다.
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SeminarNetwork

class ClientHeaderInterceptor @Inject constructor() : Interceptor {
    /** 모든 발표용 HTTP 요청에 클라이언트 헤더를 추가합니다. 원래는 별도의 파일로 구현하는게 일반적입니다. */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().header("X-Client", "Android").build()
        return chain.proceed(request)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /** Interceptor가 연결된 발표 전용 HTTP 클라이언트를 생성합니다. */
    @Provides @Singleton @SeminarNetwork
    fun provideOkHttpClient(interceptor: ClientHeaderInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    /** JSON을 DTO로 변환하는 Converter와 서버 주소를 등록합니다. */
    @Provides @Singleton @SeminarNetwork
    fun provideRetrofit(@SeminarNetwork client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Retrofit이 구현한 TaskApi를 Hilt에 제공합니다. */
    @Provides @Singleton
    fun provideTaskApi(@SeminarNetwork retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    /** FIXME: Fake 실습 시 파라미터 타입만 FakeTaskRemoteDataSource로 바꿉니다. */
    @Binds
    abstract fun bindTaskRemoteDataSource(impl: RetrofitTaskRemoteDataSource): TaskRemoteDataSource
}
