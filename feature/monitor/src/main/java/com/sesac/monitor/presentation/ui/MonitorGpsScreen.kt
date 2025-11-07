package com.sesac.monitor.presentation.ui

import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.ui.theme.paddingExtraLarge
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingNone
import com.sesac.monitor.presentation.MonitorMapViewLifecycleHelper
import com.sesac.monitor.presentation.MonitorViewModel
import com.sesac.monitor.presentation.utils.LatLngPoint2LatLng
import kotlinx.coroutines.launch


@Composable
fun MonitorGpsScreen (
    modifier: Modifier = Modifier,
    viewModel: MonitorViewModel = hiltViewModel(),
    monitorLifecycleHelper: MonitorMapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
    onMapReady: ((NaverMap) -> Unit)? = null
) { // Context와 LifecycleOwner를 가져옵니다. (지도의 생명주기 관리에 필수)
    val coroutineScope = rememberCoroutineScope()
    val latLngPointRandom by viewModel.latLngRandom.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    val context = LocalContext.current
    val monitorMapView = remember { MapView(context) }
    val monitorLifecycleHelper = remember { MonitorMapViewLifecycleHelper(monitorMapView) }

// 화면이 사라질 때 MapView lifecycle 처리
    DisposableEffect(Unit) {
        onDispose {
            val parent = monitorMapView.parent as? ViewGroup
            parent?.removeView(monitorMapView) // 부모에서 제거
            monitorLifecycleHelper.onPause()
            monitorLifecycleHelper.onStop()
            monitorLifecycleHelper.onDestroy()
        }
    }
    LaunchedEffect(latLngPointRandom, Unit) {
        coroutineScope.launch {
            viewModel.getLatLngRandom()
            Log.d("Tag-MonitorGpsScreen", "${latLngPointRandom}")
        }
    }

    Box(
        modifier = Modifier
            .padding(start = paddingLarge, top = paddingNone, end = paddingLarge, bottom = paddingExtraLarge)
            .clip(MaterialTheme.shapes.extraLarge)
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                monitorLifecycleHelper.monitorMapView.apply {
                    monitorLifecycleHelper.onCreate(null)  // savedInstanceState 필요 시 전달
                    getMapAsync { naverMap ->
                        naverMap.locationSource = locationSource
                        naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        // ✅ Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                        naverMap.uiSettings.isLocationButtonEnabled = true
                        naverMap.uiSettings.isZoomControlEnabled = false

                        //✅ 지도 준비 완료 시 마커 생성
                        val marker = Marker().apply {
                            Log.d("Tag-MonitorGpsScreen", "변환 -> ${LatLngPoint2LatLng(latLngPointRandom)}")
                            position = LatLngPoint2LatLng(latLngPointRandom)
                            map = naverMap
                        }

                        onMapReady?.invoke(naverMap)
                    }
                }
            },
            update = { monitorMapView ->
                monitorLifecycleHelper.onResume()
            }
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MonitorGpsPreview() {
//    MonitorGpsScreen ()
//}
