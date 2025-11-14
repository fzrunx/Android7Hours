package com.sesac.domain.model

import org.threeten.bp.LocalDate

/**
 * 마이페이지 메인 화면의 통계 정보를 나타내는 데이터 클래스
 * @param iconName 아이콘을 식별하기 위한 문자열 (e.g., "LocationOn")
 * @param label 통계 항목의 이름 (e.g., "산책 거리")
 * @param value 통계 값 (e.g., "42.5km")
 * @param colorName 아이콘 배경색을 식별하기 위한 문자열 (e.g., "Purple")
 */
data class MypageStat(
    val iconName: String,
    val label: String,
    val value: String,
    val colorName: String
)

/**
 * 마이페이지 메인 화면의 메뉴 아이템을 나타내는 데이터 클래스
 * @param key 메뉴를 식별하기 위한 고유 키 (e.g., "MANAGE", "FAVORITE")
 * @param iconName 아이콘을 식별하기 위한 문자열 (e.g., "CalendarToday")
 * @param label 메뉴 이름 (e.g., "일정관리")
 * @param badgeCount 배지에 표시될 숫자 (없으면 null)
 */
data class MypageMenuItem(
    val key: String,
    val iconName: String,
    val label: String,
    val badgeCount: Int? = null
)

/**
 * 즐겨찾기 화면의 산책로 정보를 나타내는 데이터 클래스
 */
data class FavoriteWalkPath(
    val id: Int,
    val name: String,
    val location: String,
    val distance: String,
    val image: String,
    val rating: Double,
    val uploader: String?,
    val time: String?,
    val likes: Int?,
    val distanceFromMe: String?
)

/**
 * 즐겨찾기 화면의 커뮤니티 게시글 정보를 나타내는 데이터 클래스
 */
data class FavoriteCommunityPost(
    val id: Int,
    val author: String,
    val authorImage: String,
    val timeAgo: String,
    val content: String,
    val image: String?,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val isFavorite: Boolean,
    val category: String
)

/**
 * 일정관리 화면의 스케줄 아이템을 나타내는 데이터 클래스
 */
data class MypageSchedule(
    val id: Long,
    val date: LocalDate,
    val title: String,
    val memo: String
)

/**
 * 설정 화면의 권한 항목을 나타내는 데이터 클래스
 * @param key 권한을 식별하기 위한 고유 키 (e.g., "CAMERA", "GPS")
 * @param iconName 아이콘을 식별하기 위한 문자열
 * @param label 권한 이름 (e.g., "카메라")
 * @param description 권한에 대한 설명
 * @param colorName 아이콘 배경색을 식별하기 위한 문자열
 */
data class MypagePermission(
    val key: String,
    val iconName: String,
    val label: String,
    val description: String,
    val colorName: String
)
