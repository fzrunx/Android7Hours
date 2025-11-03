package com.sesac.monitor.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.sesac.common.R



@Composable
fun MonitorGpsScreen (modifier: Modifier = Modifier,
                      lifecycleHelper: MapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
                      onMapReady: ((NaverMap) -> Unit)? = null) {
    // Context와 LifecycleOwner를 가져옵니다. (지도의 생명주기 관리에 필수)
        // 1. AndroidView를 사용하여 네이버 지도 View를 통합합니다.
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                lifecycleHelper.mapView.apply {
                    lifecycleHelper.onCreate(null)  // savedInstanceState 필요 시 전달
                    getMapAsync { naverMap ->
                        //✅ 지도 준비 완료 시 마커 생성
                        val marker = Marker().apply {
                            position = LatLng(37.5670135, 126.9783740)
                            map = naverMap
                        }

                        onMapReady?.invoke(naverMap)
                    }
                }
            },
            update = { mapView ->
                lifecycleHelper.onResume()
            }
        )
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MonitorGpsPreview() {
//    MonitorGpsScreen ()
//}
