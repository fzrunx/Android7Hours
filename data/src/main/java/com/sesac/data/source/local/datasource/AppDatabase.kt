package com.sesac.data.source.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sesac.data.dao.DiaryDao
import com.sesac.data.dao.PathDao
import com.sesac.data.entity.DiaryEntity
import com.sesac.data.entity.PathEntity

@Database(entities = [PathEntity::class, DiaryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pathDao(): PathDao
    abstract fun diaryDao(): DiaryDao
}