package com.sesac.data.di

import com.sesac.data.source.api.KakaoUserApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoApiModule {

    private const val KAKAO_BASE_URL = "https://kapi.kakao.com/"

    @Provides
    @Singleton
    @Named("Kakao")
    fun provideKakaoOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("Kakao")
    fun provideKakaoRetrofit(
        @Named("Kakao") okHttpClient: OkHttpClient,
        moshi: Moshi // Can reuse the Moshi instance from NetworkModule
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideKakaoUserApiService(@Named("Kakao") retrofit: Retrofit): KakaoUserApiService =
        retrofit.create(KakaoUserApiService::class.java)
}
