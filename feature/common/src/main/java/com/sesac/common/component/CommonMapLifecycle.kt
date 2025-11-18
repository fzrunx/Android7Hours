package com.sesac.common.component

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.naver.maps.map.MapView

class CommonMapLifecycle(
    lifecycle: Lifecycle
) : DefaultLifecycleObserver {
    private var _mapView: MapView? = null
    val mapView: MapView?
        get() = _mapView

    init {
        lifecycle.addObserver(this)
    }
    fun setMapView(view: MapView) {
        _mapView = view
    }
    override fun onCreate(owner: LifecycleOwner) {
        _mapView?.onCreate(null)
    }

    override fun onStart(owner: LifecycleOwner) {
        _mapView?.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        _mapView?.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        _mapView?.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        _mapView?.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        _mapView?.onDestroy()
        CommonMapView.clear()
    }
}
