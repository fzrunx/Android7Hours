package com.sesac.data.di

import android.content.Context
import com.sesac.data.datastore.AuthDataStore
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context, moshi: Moshi): AuthDataStore {
        return AuthDataStore(context, moshi)
    }
}
