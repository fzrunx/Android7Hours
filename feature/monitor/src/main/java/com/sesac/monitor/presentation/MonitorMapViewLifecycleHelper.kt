package com.sesac.monitor.presentation

import android.os.Bundle
import com.naver.maps.map.MapView

class MonitorMapViewLifecycleHelper(val monitorMapView: MapView) {


    fun onCreate(savedInstanceState: Bundle?) {
        monitorMapView.onCreate(savedInstanceState)
    }

    fun onStart() {
        monitorMapView.onStart()
    }

    fun onResume() {
        monitorMapView.onResume()
    }

    fun onPause() {
        monitorMapView.onPause()
    }

    fun onStop() {
        monitorMapView.onStop()
    }

    fun onDestroy() {
        monitorMapView.onDestroy()
    }

    fun onSaveInstanceState(outState: Bundle) {
        monitorMapView.onSaveInstanceState(outState)
    }
}