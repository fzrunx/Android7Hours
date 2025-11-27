package com.sesac.trail.presentation

import com.naver.maps.geometry.LatLng
import com.sesac.domain.model.Coord

fun Coord.toLatLng(): LatLng = LatLng(latitude, longitude)
