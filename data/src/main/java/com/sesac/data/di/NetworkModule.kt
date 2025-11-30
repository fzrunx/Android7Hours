package com.sesac.data.di

import com.sesac.data.BuildConfig
import com.sesac.data.dto.BookmarkedObject
import com.sesac.data.dto.BookmarkedPathDTO
import com.sesac.data.dto.PostDTO
import com.sesac.data.repository.SessionRepositoryImpl
import com.sesac.data.source.api.AuthApi
import com.sesac.data.source.api.BookmarkApi
import com.sesac.data.source.api.PathApi
import com.sesac.data.source.api.PetsApi
import com.sesac.data.source.api.PlaceApi
import com.sesac.data.source.api.PostApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.CookieManager
import javax.inject.Provider
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    private const val BASE_URL = "http://192.168.0.73:8000/"
    private const val BASE_URL = BuildConfig.SERVER_URL

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(BookmarkedObject::class.java, "content_type_name")
                    .withSubtype(BookmarkedPathDTO::class.java, "path")
                    .withSubtype(PostDTO::class.java, "post")
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideCookieJar(): CookieJar {
        return JavaNetCookieJar(CookieManager())
    }

    @Provides
    @Singleton
    fun provideCsrfTokenInterceptor(cookieJar: CookieJar): CsrfTokenInterceptor {
        return CsrfTokenInterceptor(cookieJar)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        sessionRepositoryImpl: SessionRepositoryImpl,
        authApiProvider: Provider<AuthApi> // Provider로 AuthApi 주입
    ): TokenAuthenticator = TokenAuthenticator(sessionRepositoryImpl, authApiProvider)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        cookieJar: CookieJar,
        csrfTokenInterceptor: CsrfTokenInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(csrfTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun providePetsApi(
        retrofit: Retrofit
    ): PetsApi =
        retrofit.create(PetsApi::class.java)

    @Provides
    @Singleton
    fun provideTrailApi(
        retrofit: Retrofit
    ): PathApi {
        return retrofit.create(PathApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApi(
        retrofit: Retrofit
    ): PostApi =
        retrofit.create(PostApi::class.java)

    @Provides
    @Singleton
    fun provideBookmarkApi(
        retrofit: Retrofit
    ): BookmarkApi =
        retrofit.create(BookmarkApi::class.java)

    @Provides
    @Singleton
    fun providePlaceApi(
        retrofit: Retrofit
    ): PlaceApi =
        retrofit.create(PlaceApi::class.java)

}

//@Module
//@InstallIn(SingletonComponent::class)
//object KakaoLoginModule {
//
//    @Provides
//    @Singleton
//    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
//        return retrofit.create(AuthApiService::class.java)
//    }
//}
