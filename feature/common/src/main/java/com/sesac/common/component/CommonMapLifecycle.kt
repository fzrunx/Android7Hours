package com.sesac.common.component

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.naver.maps.map.MapView

class CommonMapLifecycle(
    private val mapView: MapView,
    lifecycle: Lifecycle
) : DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        mapView.onCreate(null)
    }

    override fun onStart(owner: LifecycleOwner) {
        mapView.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        mapView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        mapView.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        mapView.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mapView.onDestroy()
        // 화면이 완전히 종료될 때만 MapView 삭제
        CommonMapView.clear()
    }
}