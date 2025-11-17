package com.sesac.monitor.presentation.ui

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.component.CommonMapLifecycle   // 🔧 공유 lifecycle 관리
import com.sesac.common.component.CommonMapView
import com.sesac.monitor.presentation.MonitorViewModel
import com.sesac.monitor.presentation.utils.LatLngPoint2LatLng
import kotlinx.coroutines.launch


@Composable
fun MonitorGpsScreen (
    modifier: Modifier = Modifier,
    viewModel: MonitorViewModel = hiltViewModel(),
    commonMapLifecycle: CommonMapLifecycle, // 라이프 사이클 따로 관리하려고 만듬
    onMapReady: ((NaverMap) -> Unit)? = null
) { // Context와 LifecycleOwner를 가져옵니다. (지도의 생명주기 관리에 필수)
    val coroutineScope = rememberCoroutineScope()
    val latLngPointRandom by viewModel.latLngRandom.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle // 🔧 Compose에서 lifecycle 가져오기
    val lifecycleState by lifecycle.currentStateAsState()

    LaunchedEffect(latLngPointRandom, Unit) {
        coroutineScope.launch {
            viewModel.getLatLngRandom()
            Log.d("Tag-MonitorGpsScreen", "${latLngPointRandom}")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        key(lifecycleState) {
            if (lifecycleState.isAtLeast(Lifecycle.State.CREATED)) {
                AndroidView(
                    modifier = modifier.fillMaxSize(),
                    factory = { context ->
                        CommonMapView.getMapView(context).apply {
                            getMapAsync { naverMap ->
                                naverMap.locationSource = locationSource
                                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                                // ✅ Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                                naverMap.uiSettings.isLocationButtonEnabled = true
                                naverMap.uiSettings.isZoomControlEnabled = false

                                //✅ 지도 준비 완료 시 마커 생성
                                val marker = Marker().apply {
                                    Log.d(
                                        "Tag-MonitorGpsScreen",
                                        "변환 -> ${LatLngPoint2LatLng(latLngPointRandom)}"
                                    )
                                    position = LatLngPoint2LatLng(latLngPointRandom)
                                    map = naverMap
                                }

                                onMapReady?.invoke(naverMap)
                                Log.d("Tag-MonitorGpsScreen", "gps 지도 준비 완료")
                            }
                        }
                    },
                    update = {
                        it.requestLayout()
                    }
                )
            }
        }
    }
}
