package com.sesac.data.source.local.datasource

import com.sesac.common.ui.theme.AccentGreen
import com.sesac.common.ui.theme.ColorBlue
import com.sesac.common.ui.theme.Purple500
import com.sesac.domain.model.LatLngPoint
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.RecommendedPath
import com.sesac.domain.model.UserPath
import java.util.Date

object MockTrail {
    val recommendedPaths = mutableListOf<UserPath>()
    val myRecord = mutableListOf<MyRecord>()


    init {
        with(recommendedPaths) {
            add(UserPath(1, "강남역 주변 산책로", "산책왕123", 1.5f, 15, 45, 0.3f, LatLngPoint(0.2, 0.3)))
            add(UserPath(2, "압구정 한강 야경 코스", "멍멍이사랑", 2.8f, 35, 89, 0.8f,  LatLngPoint(0.45, 0.6)))
            add(UserPath(3, "청담동 카페거리 산책", "댕댕이집사", 2.0f, 25, 67, 1.2f, LatLngPoint(0.3, 0.7)))
            add(UserPath(4, "선릉역 공원 코스", "산책매니아", 1.2f, 15, 34, 1.5f, LatLngPoint(0.55, 0.4)))
            add(UserPath(5, "서울숲 산책로", "자연러버", 2.5f, 30, 152, 2.1f,  LatLngPoint(0.25, 0.5)))
        }

        with(myRecord) {
            add(MyRecord(101, "아침 산책", Date(2025,11,2), 2.3f, 32, 3420, 145, Purple500,))
            add( MyRecord(102, "한강공원 조깅", Date(2025,11,1), 4.5f, 58, 6780, 298, ColorBlue))
            add( MyRecord(103, "저녁 산책", Date(2025,10,31), 1.8f, 25, 2560, 112, AccentGreen))
        }
    }
}