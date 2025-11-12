package com.sesac.data.source.local.datasource

import com.sesac.domain.local.model.LatLngPoint

object MockMonitor {
    val latLngPointList = mutableListOf<LatLngPoint?>()

    init {
        with(latLngPointList) {
            add(
                LatLngPoint(37.527297, 126.929143)
            )

            add(
                LatLngPoint(38.298815, 128.535902)
            )

            add(
                LatLngPoint(33.499936, 126.489461)
            )
        }
    }
}