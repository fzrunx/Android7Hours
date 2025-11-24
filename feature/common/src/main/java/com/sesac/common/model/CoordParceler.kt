package com.sesac.common.model

import android.os.Parcel
import com.sesac.domain.model.Coord
import kotlinx.parcelize.Parceler

object CoordParceler : Parceler<Coord> {
    override fun create(parcel: Parcel): Coord {
        return Coord(
            latitude = parcel.readDouble(),
            longitude = parcel.readDouble(),
            altitude = parcel.readValue(Double::class.java.classLoader) as? Double
        )
    }

    override fun Coord.write(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeValue(altitude)
    }
}
