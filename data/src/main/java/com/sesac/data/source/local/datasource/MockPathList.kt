package com.sesac.data.source.local.datasource

import com.sesac.domain.model.PathInfo

object MockPathList {
    val pathList = mutableListOf<PathInfo>()

    init {
        with(pathList) {
            add(
                PathInfo(
                    1,
                    "한강공원 산책로",
                    "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...",
                    3.2f,
                    40,
                    "중급"
                )
            )

            add(
                PathInfo(
                    2,
                    "서울숲 산책로",
                    "https://images.unsplash.com/photo-1753375676074-6a660c5ac264?...",
                    2.5f,
                    30,
                    "초급"
                )
            )

            add(
                PathInfo(
                    3,
                    "올림픽공원 둘레길", "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...",
                    4.1f,
                    50,
                    "고급"
                ),
            )
        }
    }
}