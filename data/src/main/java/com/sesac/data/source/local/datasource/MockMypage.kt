package com.sesac.data.source.local.datasource

import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.MypageStat
import org.threeten.bp.LocalDate

object MockMypage {

    // From MypageMainScreen
    val stats = listOf(
        MypageStat("LocationOn", "산책 거리", "42.5km", "Purple"),
        MypageStat("Schedule", "산책 시간", "12.5시간", "Blue"),
        MypageStat("CalendarToday", "산책 횟수", "28회", "Green")
    )

    // From MypageFavoriteScreen
    val favoriteWalkPaths = mutableListOf(
        FavoriteWalkPath(
            id = 1,
            name = "한강 산책로",
            location = "서울 영등포구",
            distance = "2.5km",
            image = "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?w=500",
            rating = 4.8,
            uploader = "산책왕",
            time = "30분 코스",
            likes = 120,
            distanceFromMe = "1.2km"
        ),
        FavoriteWalkPath(
            id = 2,
            name = "남산 둘레길",
            location = "서울 중구",
            distance = "3.2km",
            image = "https://images.unsplash.com/photo-1753375676074-6a660c5ac264?w=500",
            rating = 4.9,
            uploader = "도시러버",
            time = "40분 코스",
            likes = 98,
            distanceFromMe = "2.0km"
        )
    )

    val favoritePosts = mutableListOf(
        FavoriteCommunityPost(
            id = 1,
            author = "멍멍이집사",
            authorImage = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=500",
            timeAgo = "1시간 전",
            content = "내일 같이 한강 산책하실 분~?",
            image = "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?w=500",
            likes = 24,
            comments = 3,
            isLiked = true,
            isFavorite = true,
            category = "모임"
        ),
        FavoriteCommunityPost(
            id = 2,
            author = "강아지사랑",
            authorImage = "https://images.unsplash.com/photo-1598133894008-61f75d8a3c91?w=500",
            timeAgo = "3시간 전",
            content = "남산 코스 너무 좋았어요!",
            image = null,
            likes = 12,
            comments = 1,
            isLiked = false,
            isFavorite = true,
            category = "후기"
        )
    )

    // From MypageManageScreen
    val schedules = mutableListOf(
        MypageSchedule(
            id = 1L,
            date = LocalDate.now(),
            title = "초코 백신접종",
            memo = "종합 백신 접종 - 오전 10시 동물병원"
        ),
        MypageSchedule(
            id = 2L,
            date = LocalDate.now(),
            title = "복순이 건강검진",
            memo = "정기 건강검진 및 심장사상충 예방약 처방 - 오후 2시"
        )
    )

    // From MypageSettingScreen
//    val permissions = listOf(
//        MypagePermission("CAMERA", "CameraAlt", "카메라", "산책 중 사진 및 영상 촬영", "Purple"),
//        MypagePermission("GPS", "LocationOn", "GPS", "위치 기반 산책로 추천 및 기록", "Blue"),
//        MypagePermission("NOTIFICATION", "Notifications", "알림", "산책 알림 및 커뮤니티 소식", "Green")
//    )
}
