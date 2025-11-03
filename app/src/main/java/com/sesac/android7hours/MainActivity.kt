package com.sesac.android7hours

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.naver.maps.map.MapView
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.monitor.presentation.MonitorCamScreen
import com.sesac.monitor.presentation.MapViewLifecycleHelper


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

                    MonitorCamScreen(
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