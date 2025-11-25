package com.sesac.data.di

import com.sesac.data.source.remote.PathRemoteDataSource
import com.sesac.data.source.remote.PathRemoteDataSourceImpl
import com.sesac.data.source.remote.api.PathApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PathDataSourceModule {

    @Provides
    @Singleton
    fun providePathRemoteDataSource(api: PathApi): PathRemoteDataSource {
        return PathRemoteDataSourceImpl(api)
    }

}