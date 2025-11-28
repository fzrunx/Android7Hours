package com.sesac.data.di

import com.sesac.data.repository.AuthRepositoryImpl
import com.sesac.data.repository.BookmarkRepositoryImpl
import com.sesac.data.repository.CommentRepositoryImpl
import com.sesac.data.repository.CommunityRepositoryImpl
import com.sesac.data.repository.HomeRepositoryImpl
import com.sesac.data.repository.MonitorRepositoryImpl
import com.sesac.data.repository.MypageRepositoryImpl
import com.sesac.data.repository.PetRepositoryImpl
import com.sesac.data.repository.SessionRepositoryImpl
import com.sesac.data.repository.PathRepositoryImpl
import com.sesac.data.repository.PlaceRepositoryImpl
import com.sesac.data.repository.UserRepositoryImpl
import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.repository.BookmarkRepository
import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.repository.CommunityRepository
import com.sesac.domain.repository.HomeRepository
import com.sesac.domain.repository.MonitorRepository
import com.sesac.domain.repository.MypageRepository
import com.sesac.domain.repository.PetRepository
import com.sesac.domain.repository.SessionRepository
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.repository.PlaceRepository
import com.sesac.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCommunityRepository(
        communityRepositoryImpl: CommunityRepositoryImpl
    ): CommunityRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindTrailRepository(
        pathRepositoryImpl: PathRepositoryImpl
    ): PathRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindMonitorRepository(
        monitorRepositoryImpl: MonitorRepositoryImpl
    ): MonitorRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindMypageRepository(
        mypageRepositoryImpl: MypageRepositoryImpl
    ): MypageRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindPlaceRepository(
        placeRepositoryImpl: PlaceRepositoryImpl
    ): PlaceRepository

}
