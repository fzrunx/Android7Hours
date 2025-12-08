package com.sesac.trail.utils

import com.naver.maps.geometry.LatLng
import com.sesac.domain.model.Coord
import com.sesac.domain.model.Place

fun Coord.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)

fun Place.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)