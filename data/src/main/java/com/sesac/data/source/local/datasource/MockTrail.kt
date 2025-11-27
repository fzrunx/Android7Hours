package com.sesac.data.source.local.datasource

import androidx.compose.ui.graphics.Color
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.Path
import java.util.Date

object MockTrail {
    val recommendedPaths = mutableListOf<Path>()
    val myRecord = mutableListOf<MyRecord>()


    init {
//        with(recommendedPaths) {
//            add(Path(1, "강남역 주변 산책로", "산책왕123", 1.5f, 15, 45, 0.3f, listOf(Coord(0.2, 0.3))))
//            add(Path(2, "압구정 한강 야경 코스", "멍멍이사랑", 2.8f, 35, 89, 0.8f,  listOf(Coord(0.45, 0.6))))
//            add(Path(3, "청담동 카페거리 산책", "댕댕이집사", 2.0f, 25, 67, 1.2f, listOf(Coord(0.3, 0.7))))
//            add(Path(4, "선릉역 공원 코스", "산책매니아", 1.2f, 15, 34, 1.5f, listOf(Coord(0.55, 0.4))))
//            add(Path(5, "서울숲 산책로", "자연러버", 2.5f, 30, 152, 2.1f,  listOf(Coord(0.25, 0.5))))
//        }

        with(myRecord) {
            add(MyRecord(101, "아침 산책", Date(2025,11,2), 2.3f, 32, 3420, 145, Color(0xFF6200EE),))
            add( MyRecord(102, "한강공원 조깅", Date(2025,11,1), 4.5f, 58, 6780, 298, Color(0xFF3B82F6)))
            add( MyRecord(103, "저녁 산책", Date(2025,10,31), 1.8f, 25, 2560, 112, Color(0xFF22C55E)))
        }
    }
}