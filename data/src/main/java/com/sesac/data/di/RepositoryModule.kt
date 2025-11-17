package com.sesac.data.di

import com.sesac.data.repository.AuthRepositoryImpl
import com.sesac.data.repository.CommunityRepositoryImpl
import com.sesac.data.repository.HomeRepositoryImpl
import com.sesac.data.repository.MonitorRepositoryImpl
import com.sesac.data.repository.MypageRepositoryImpl
import com.sesac.data.repository.PetRepositoryImpl
import com.sesac.data.repository.SessionRepositoryImpl
import com.sesac.data.repository.TrailRepositoryImpl
import com.sesac.domain.repository.CommunityRepository
import com.sesac.domain.repository.HomeRepository
import com.sesac.domain.repository.MonitorRepository
import com.sesac.domain.repository.MypageRepository
import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.repository.PetRepository
import com.sesac.domain.repository.SessionRepository
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
    abstract fun bindTrailRepository(
        trailRepositoryImpl: TrailRepositoryImpl
    ): TrailRepository

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

    @Binds
    @Singleton
    abstract fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository
}
