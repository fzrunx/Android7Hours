package com.sesac.monitor.presentation.utils

import com.naver.maps.geometry.LatLng
import com.sesac.domain.model.LatLngPoint

fun LatLngPoint2LatLng(latLngPoint: LatLngPoint?) =
    LatLng(latLngPoint?.latitude ?: 37.5670135, latLngPoint?.longitude ?: 126.9783740)