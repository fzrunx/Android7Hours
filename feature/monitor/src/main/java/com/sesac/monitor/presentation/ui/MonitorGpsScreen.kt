package com.sesac.monitor.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.sesac.common.ui.theme.paddingExtraLarge
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingNone
import com.sesac.common.utils.MapViewLifecycleHelper
import com.sesac.monitor.presentation.MonitorViewModel
import com.sesac.monitor.presentation.utils.LatLngPoint2LatLng
import kotlinx.coroutines.launch


@Composable
fun MonitorGpsScreen (
    modifier: Modifier = Modifier,
    viewModel: MonitorViewModel = hiltViewModel(),
    lifecycleHelper: MapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
    onMapReady: ((NaverMap) -> Unit)? = null
) { // Context와 LifecycleOwner를 가져옵니다. (지도의 생명주기 관리에 필수)
    val coroutineScope = rememberCoroutineScope()
    val latLngPointRandom by viewModel.latLngRandom.collectAsStateWithLifecycle()
    val marker = remember { Marker() }

    LaunchedEffect(Unit) {
        viewModel.getLatLngRandom()
    }

    Box(
        modifier = Modifier
            .padding(start = paddingLarge, top = paddingNone, end = paddingLarge, bottom = paddingExtraLarge)
            .clip(MaterialTheme.shapes.extraLarge)
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                lifecycleHelper.mapView.apply {
                    lifecycleHelper.onCreate(null)  // savedInstanceState 필요 시 전달
                    // In factory, just prepare the map and pass it out if needed
                    getMapAsync { naverMap ->
                        onMapReady?.invoke(naverMap)
                    }
                }
            },
            update = { mapView ->
                coroutineScope.launch {
                    mapView.getMapAsync { naverMap ->
                        // Only interact with the marker when we have a valid coordinate.
                        latLngPointRandom?.let { point ->
                            val newPosition = LatLngPoint2LatLng(point)

                            // Defensively check for NaN to prevent crashes
                            if (newPosition.latitude.isNaN() || newPosition.longitude.isNaN()) {
                                Log.e("Tag-MonitorGpsScreen", "Generated invalid coordinate, skipping marker update.")
                            } else {
                                Log.d("Tag-MonitorGpsScreen", "마커 위치 업데이트 -> $newPosition")
                                marker.position = newPosition
                                // Set the map property here, only when we have a valid position.
                                marker.map = naverMap
                            }
                        }
                    }

                }
                // Get the map instance here. It's safe to call multiple times.
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
