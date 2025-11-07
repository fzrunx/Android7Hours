package com.sesac.trail.presentation

import android.os.Bundle
import com.naver.maps.map.MapView

class TrailMapViewLifecycleHelper(val trailMapView: MapView) {


    fun onCreate(savedInstanceState: Bundle?) {
        trailMapView.onCreate(savedInstanceState)
    }

    fun onStart() {
        trailMapView.onStart()
    }

    fun onResume() {
        trailMapView.onResume()
    }

    fun onPause() {
        trailMapView.onPause()
    }

    fun onStop() {
        trailMapView.onStop()
    }

    fun onDestroy() {
        trailMapView.onDestroy()
    }

    fun onSaveInstanceState(outState: Bundle) {
        trailMapView.onSaveInstanceState(outState)
    }
}