package com.sesac.android7hours.common

import android.app.Application

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