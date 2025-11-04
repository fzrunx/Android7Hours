package com.sesac.android7hours.common

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class A7HApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        // ✅ ThreeTenABP 초기화 (LocalDate 등 사용 가능하게 함)
        AndroidThreeTen.init(this)

        // ✅ 기존 전역 참조 유지
        a7HApp = this
    }
    companion object{
        private lateinit var a7HApp: A7HApplication
        fun getA7HApplication() = a7HApp
    }
}