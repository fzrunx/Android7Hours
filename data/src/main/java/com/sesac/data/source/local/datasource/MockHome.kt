package com.sesac.data.source.local.datasource

import com.sesac.domain.model.BannerData
import com.sesac.domain.model.DogCafe
import com.sesac.domain.model.TravelDestination
import com.sesac.domain.model.WalkPath

object MockHome {
    val bannerDataList = mutableListOf<BannerData>()
    val dogCafeList = mutableListOf<DogCafe>()
    val travelDestinationList = mutableListOf<TravelDestination>()
    val walkPathList = mutableListOf<WalkPath>()

    init {
        with(bannerDataList) {
            add(
                BannerData(
                1,
                "https://image.msbg.io/?p=mocoblob.blob.core.windows.net%2Ftalkarticle%2F1681624290917Evum6Ha6Yq975QC",
                "반려견과 함께하는 특별한 시간",
                "7Hours와 함께 산책을 시작하세요"
                )
            )
            add(
                BannerData(
                2,
                "https://images.unsplash.com/photo-1629130646965-e86223170abc?...",
                "새로운 산책로를 발견하세요",
                "매주 업데이트되는 추천 코스"
                )
            )
            add(
                BannerData(
                1,
                "https://images.unsplash.com/photo-1648976286170-fcc69115697b?...",
                "행복한 산책 기록",
                "소중한 추억을 남겨보세요"
                )
            )
        }

        with(dogCafeList) {
            add(
                DogCafe(1,
                "멍멍카페",
                "https://images.unsplash.com/photo-1730402739842-fbfe757d417e?...",
                "강남구",
                "넓은 정원이 있는 애견카페"
                )
            )
            add(
                DogCafe(
                2,
                "포도카페",
                "https://images.unsplash.com/photo-1739723745132-97df9db49db2?...",
                "홍대",
                "강아지 놀이터가 있는 카페"
                )
            )
        }

        with(travelDestinationList) {
            add(
                TravelDestination(
                    1,
                    "제주 애월 해안도로",
                    "https://images.unsplash.com/photo-1649261887227-38725ab9bdac?...",
                    "제주",
                    "바다를 보며 걷는 산책"
                )
            )
            add(
                TravelDestination(
                    2,
                    "강원도 속초",
                    "https://images.unsplash.com/photo-1649934515294-19726be7e02d?...",
                    "강원도",
                    "설악산과 바다를 함께"
                )
            )
        }

        with(walkPathList) {
            add(
                WalkPath(
                    1,
                    "한강공원 산책로",
                    "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...",
                    3.2f,
                    40,
                    "중급"
                )
            )
            add(
                WalkPath(
                    2,
                    "서울숲 산책로",
                    "https://images.unsplash.com/photo-1753375676074-6a660c5ac264?...",
                    2.5f,
                    30,
                    "초급"
                )
            )
            add(
                WalkPath(
                    3,
                    "올림픽공원 둘레길", "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...",
                    4.1f,
                    50,
                    "고급"
                )
            )
        }
    }
}