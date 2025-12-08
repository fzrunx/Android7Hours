package com.sesac.monitor.presentation.utils

import com.naver.maps.geometry.LatLng
import com.sesac.domain.model.Coord

fun LatLngPoint2LatLng(coord: Coord?) =
    LatLng(coord?.latitude ?: 37.5670135, coord?.longitude ?: 126.9783740)