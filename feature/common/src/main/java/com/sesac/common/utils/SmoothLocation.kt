package com.sesac.common.utils

import android.location.Location

fun smoothLocation(old: Location?, new: Location): Location {
    if (old == null) return new
    val alpha = 0.2f
    val smoothed = Location(new).apply {
        latitude = old.latitude + alpha * (new.latitude - old.latitude)
        longitude = old.longitude + alpha * (new.longitude - old.longitude)
        accuracy = new.accuracy
        bearing = new.bearing
        speed = new.speed
        time = new.time
    }
    return smoothed
}