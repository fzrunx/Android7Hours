package com.sesac.android7hours

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.naver.maps.map.MapView
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.monitor.presentation.MapViewLifecycleHelper
import com.sesac.monitor.presentation.ui.MonitorScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android7HoursTheme {
                Scaffold { innerPadding ->
                    val context = LocalContext.current
                    val mapView = MapView(context)
                    val lifecycleHelper = remember { MapViewLifecycleHelper(mapView) }

                    MonitorScreen(
                        onNavigateToHome = { /* 뒤로가기 */ },
                        lifecycleHelper = lifecycleHelper,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Android7HoursTheme {
//        Greeting("Android")
//    }
//}