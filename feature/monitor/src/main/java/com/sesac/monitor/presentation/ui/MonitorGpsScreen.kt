package com.sesac.monitor.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.sesac.monitor.utils.MapViewLifecycleHelper


@Composable
fun MonitorGpsScreen (modifier: Modifier = Modifier,
                      lifecycleHelper: MapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
                      onMapReady: ((NaverMap) -> Unit)? = null) { // Context와 LifecycleOwner를 가져옵니다. (지도의 생명주기 관리에 필수)
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 48.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
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
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MonitorGpsPreview() {
//    MonitorGpsScreen ()
//}
