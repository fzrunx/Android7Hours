package com.sesac.domain.type

enum class PostType {
    REVIEW,
    INFO,
    UNKNOWN
}

// UI용 확장함수
fun PostType.toKoreanString(): String {
    return when (this) {
        PostType.REVIEW -> "산책후기"
        PostType.INFO -> "정보공유"
        PostType.UNKNOWN -> "알 수 없음"
    }
}
