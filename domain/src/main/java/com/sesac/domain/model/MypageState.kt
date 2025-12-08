package com.sesac.domain.model

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
    val colorName: String,
)