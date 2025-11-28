package com.sesac.data.source.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sesac.data.dao.PathDao
import com.sesac.data.entity.PathEntity

@Database(entities = [PathEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pathDao(): PathDao
}