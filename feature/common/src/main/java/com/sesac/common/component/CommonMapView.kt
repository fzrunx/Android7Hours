package com.sesac.common.component

import android.content.Context
import android.view.ViewGroup
import com.naver.maps.map.MapView

object CommonMapView {

    private var mapView: MapView? = null

    fun getMapView(context: Context): MapView {
        if (mapView == null) {
            mapView = MapView(context.applicationContext)
        }

        // 이미 다른 부모에 붙어있으면 떼어내기 (중요!!)
        val parent = mapView!!.parent
        if (parent is ViewGroup) {
            parent.removeView(mapView)
        }

        return mapView!!
    }

    fun clear() {
        mapView = null
    }
}