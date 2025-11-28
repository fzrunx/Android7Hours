package com.sesac.monitor.presentation.ui

import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.utils.EffectPauseStop
import com.sesac.monitor.presentation.MonitorViewModel
import com.naver.maps.geometry.LatLng
import com.sesac.domain.result.ResponseUiState // NEW IMPORT
import kotlinx.coroutines.launch


@Composable
fun MonitorGpsScreen (
    modifier: Modifier = Modifier,
    viewModel: MonitorViewModel = hiltViewModel(),
    commonMapLifecycle: CommonMapLifecycle,
    petId: Int, // NEW ARGUMENT: petId
) {
    val coroutineScope = rememberCoroutineScope()
    val monitoredPetState by viewModel.monitoredPet.collectAsStateWithLifecycle() // NEW STATE
    val activity = LocalActivity.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateAsState()
    var currentNaverMap by remember { mutableStateOf<NaverMap?>(null) } // To hold NaverMap instance
    var petMarker by remember { mutableStateOf<Marker?>(null) } // NEW: To manage the pet's marker

    // NEW: Start monitoring the pet
    LaunchedEffect(petId) {
        viewModel.startMonitoringPet(petId)
    }

    // NEW: Update map marker when pet location changes
    LaunchedEffect(monitoredPetState, currentNaverMap) {
        val naverMap = currentNaverMap ?: return@LaunchedEffect
        when (val state = monitoredPetState) {
            is ResponseUiState.Success -> {
                val pet = state.result
                pet.lastLocation?.let { petLocation ->
                    val latLng = LatLng(petLocation.latitude, petLocation.longitude)

                    // Clear previous marker if exists
                    petMarker?.map = null

                    // Create and set new marker
                    val newMarker = Marker().apply {
                        position = latLng
                        captionText = pet.name
                        map = naverMap
                    }
                    petMarker = newMarker // Store reference to the new marker

                    // Move camera to pet's location
                    val cameraUpdate = CameraUpdate.scrollTo(latLng)
                    naverMap.moveCamera(cameraUpdate)
                }
            }
            is ResponseUiState.Error -> {
                Log.e("MonitorGpsScreen", "Error monitoring pet: ${state.message}")
                // Optionally show a Toast or error message
            }
            else -> { /* Loading or Idle states */ }
        }
    }


    // 🔴 중요!! 화면이 Pause 또는 Stop 될 때 MapView 반응하도록 설정
    lifecycle.EffectPauseStop {
        commonMapLifecycle.mapView?.onPause()
        commonMapLifecycle.mapView?.onStop()
        Log.d("Tag-MonitorGpsScreen", "📌 Monitor GPS Paused → MapView pause/stop 호출됨")
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
                            // 🔹 이미 부모가 붙어있다면 제거
                            (parent as? ViewGroup)?.removeView(this)

                            // 🔵 MapView 공유 객체에 등록
                            commonMapLifecycle.setMapView(this)

                            // 🔵 Compose에서 MapView 재사용 시 resume/start 호출
                            this.onStart()
                            this.onResume()

                            getMapAsync { naverMap ->
                                currentNaverMap = naverMap // Store NaverMap instance

                                // UI
                                naverMap.uiSettings.isLocationButtonEnabled = true
                                naverMap.uiSettings.isZoomControlEnabled = false

                                Log.d("Tag-MonitorGpsScreen", "gps 지도 준비 완료")
                            }
                        }
                    },
                    update = { view ->
                        view.requestLayout()
                    }
                )
            }
        }
    }
}
