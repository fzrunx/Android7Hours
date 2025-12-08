package com.sesac.android7hours

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import com.sesac.android7hours.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
    }
    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            Log.e("TAG", "Splash Activity : 필요하다면 여기서 앱에 필요한 초기화 작업 시작!")
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 500L)
    }
}