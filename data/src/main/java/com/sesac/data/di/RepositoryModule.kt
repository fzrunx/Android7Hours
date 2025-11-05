package com.sesac.data.di

import com.sesac.data.repository.CommunityRepositoryImpl
import com.sesac.data.repository.HomeRepositoryImpl
import com.sesac.data.repository.MonitorRepositoryImpl
import com.sesac.data.repository.MypageRepositoryImpl
import com.sesac.domain.repository.CommunityRepository
import com.sesac.domain.repository.HomeRepository
import com.sesac.domain.repository.MonitorRepository
import com.sesac.domain.repository.MypageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCommunityRepository(
        communityRepositoryImpl: CommunityRepositoryImpl
    ): CommunityRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindMonitorRepository(
        monitorRepositoryImpl: MonitorRepositoryImpl
    ): MonitorRepository

    @Binds
    @Singleton
    abstract fun bindMypageRepository(
        mypageRepositoryImpl: MypageRepositoryImpl
    ): MypageRepository
}
