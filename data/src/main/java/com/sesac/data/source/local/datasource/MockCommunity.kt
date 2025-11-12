package com.sesac.data.source.local.datasource

import com.sesac.domain.local.model.Community
import com.sesac.common.R

object MockCommunity {
    val postList = mutableListOf<Community>()

    init {
        with(postList) {
            add(
                Community(
                    postId = 0,
                    title = "Weekend Hike",
                    userName = "간장",
                    content = "부산 근처 산에 다녀왔어요. 자연과 함께한 힐링 타임 🌿",
                    imageResList = listOf(
                        R.drawable.nature,
                        R.drawable.hiking
                    ),
                    comments = listOf("부산", "갈매기"),
                    status = true
                )
            )

            add(
                Community(
                    postId = 1,
                    title = "Lunch with Teammates",
                    userName = "공장",
                    content = "팀원들과 함께한 점심시간 🍜 새로운 아이디어도 많이 나왔어요!",
                    imageResList = listOf(
                        R.drawable.food,
                        R.drawable.team
                    ),
                    comments = listOf("맞없음", "아님 맞있음", "?"),
                    status = false
                )
            )

            add(
                Community(
                    postId = 2,
                    title = "Evening Sunset",
                    userName = "공장장",
                    content = "오늘 본 석양은 정말 아름다웠어요 🌇 하루를 마무리하며 힐링했습니다.",
                    imageResList = listOf(
                        R.drawable.sunset
                    ),
                    status = true
                )
            )

            add(
                Community(
                    postId = 3,
                    title = "테스트용 게시글",
                    userName = "로봇",
                    content = "Mock 데이터 테스트용 포스트입니다.",
                    imageResList = listOf(
                        R.drawable.icons8_dog_50
                    ),
                    status = false
                )
            )

            add(
                Community(
                    postId = 4,
                    title = "기타",
                    userName = "등등",
                    content = "겨울에 모기가 왜있어"
                )
            )
        }
    }
}