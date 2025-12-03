package com.sesac.data.di

import android.content.Context
import androidx.room.Room
import com.sesac.data.dao.DiaryDao
import com.sesac.data.dao.PathDao
import com.sesac.data.source.local.datasource.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "path_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePathDao(db: AppDatabase): PathDao {
        return db.pathDao()
    }

    @Provides
    fun provideDiaryDao(db: AppDatabase): DiaryDao {
        return db.diaryDao()
    }
}