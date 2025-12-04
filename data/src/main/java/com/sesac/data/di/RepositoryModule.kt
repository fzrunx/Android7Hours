package com.sesac.data.di

import com.sesac.common.repository.WebRTCRepository
import com.sesac.data.repository.AuthRepositoryImpl
import com.sesac.data.repository.BookmarkRepositoryImpl
import com.sesac.data.repository.CommentRepositoryImpl
import com.sesac.data.repository.HomeRepositoryImpl
import com.sesac.data.repository.LikeRepositoryImpl
import com.sesac.data.repository.LocationRepositoryImpl
import com.sesac.data.repository.MypageRepositoryImpl
import com.sesac.data.repository.PathRepositoryImpl
import com.sesac.data.repository.PetRepositoryImpl
import com.sesac.data.repository.PlaceRepositoryImpl
import com.sesac.data.repository.PostRepositoryImpl
import com.sesac.data.repository.SessionRepositoryImpl
import com.sesac.data.repository.UserRepositoryImpl
import com.sesac.data.repository.WebRTCRepositoryImpl
import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.repository.BookmarkRepository
import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.repository.HomeRepository
import com.sesac.domain.repository.LikeRepository
import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.repository.MypageRepository
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.repository.PetRepository
import com.sesac.domain.repository.PlaceRepository
import com.sesac.domain.repository.PostRepository
import com.sesac.domain.repository.SessionRepository
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

    @Binds // New binding for WebRTCRepository
    @ActivityRetainedScoped
    abstract fun bindWebRTCRepository(
        webRTCRepositoryImpl: WebRTCRepositoryImpl
    ): WebRTCRepository


    @Binds
    @ActivityRetainedScoped
    abstract fun bindLikeRepository(
        likeRepositoryImpl: LikeRepositoryImpl
    ): LikeRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SingleToneRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMapRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository
}