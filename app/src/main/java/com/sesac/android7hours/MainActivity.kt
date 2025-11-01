package com.sesac.android7hours

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.android7hours.nav_graph.EntryPointScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android7HoursTheme {
                EntryPointScreen()
            }
        }
    }
}