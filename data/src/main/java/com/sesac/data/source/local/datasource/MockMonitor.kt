package com.sesac.data.source.local.datasource

import com.sesac.domain.model.Coord

object MockMonitor {
    val coordList = mutableListOf<Coord?>()

    init {
        with(coordList) {
            add(
                Coord(37.527297, 126.929143)
            )

            add(
                Coord(38.298815, 128.535902)
            )

            add(
                Coord(33.499936, 126.489461)
            )
        }
    }
}