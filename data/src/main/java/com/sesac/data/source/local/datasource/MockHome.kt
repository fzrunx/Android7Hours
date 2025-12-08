package com.sesac.data.source.local.datasource

import com.sesac.domain.model.BannerData

object MockHome {
    val bannerDataList = mutableListOf<BannerData>()

    init {
        with(bannerDataList) {
            add(
                BannerData(
                    1,
                    "https://image.msbg.io/?p=mocoblob.blob.core.windows.net%2Ftalkarticle%2F1681624290917Evum6Ha6Yq975QC",
                    "새로운 산책로를 발견하세요",
                    "7Hours와 함께 산책을 시작하세요",
                )
            )
            add(
                BannerData(
                    2,
                    "https://images.unsplash.com/photo-1629130646965-e86223170abc?...",
                    "반려견과 함께하는 특별한 시간",
                    "매주 업데이트되는 추천 코스",
                )
            )
            add(
                BannerData(
                    1,
                    "https://images.unsplash.com/photo-1648976286170-fcc69115697b?...",
                    "행복한 산책 기록",
                    "소중한 추억을 남겨보세요",
                )
            )
        }
    }
}