package com.sesac.android7hours.common

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache

class A7HApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        a7HApp = this
    }
    companion object{
        private lateinit var a7HApp: A7HApplication
        fun getA7HApplication() = a7HApp
    }
}

//Default Memory Size = 0.15 ~ 0.2
const val COIL_MEMORY_CACHE_SIZE_PERCENT = 0.3

//Coil Disk Cache Size Setting
//30~100
const val COIL_DISK_CACHE_DIR_NAME = "coil_file_cache"
const val COIL_DISK_CACHE_MAX_SIZE = 1024 * 1024 * 100

class CoilUsingApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }
    companion object{
        private lateinit var myApplication: CoilUsingApplication
        fun getCoilUsingApplication() = myApplication
    }
    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, COIL_MEMORY_CACHE_SIZE_PERCENT)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(filesDir.resolve(COIL_DISK_CACHE_DIR_NAME))
                .maximumMaxSizeBytes(COIL_DISK_CACHE_MAX_SIZE.toLong())
                .build()
        }.build()
}