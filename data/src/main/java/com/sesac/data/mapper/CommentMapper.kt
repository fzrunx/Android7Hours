package com.sesac.data.mapper

import com.sesac.common.utils.parseDate
import com.sesac.data.dto.comment.request.CommentCreateRequestDTO
import com.sesac.data.dto.comment.response.CommentAuthorDTO
import com.sesac.data.dto.comment.response.CommentItemDTO
import com.sesac.domain.model.AuthorSummary
import com.sesac.domain.model.CommentItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.ranges.contains

object TimeUtils {
    // 서버 timeZone 정의
    private val serverTimeZone = TimeZone.getTimeZone("Asia/Seoul")
    /**
     * 서버에서 받은 문자열을 Date로 변환
     * 실패 시 Epoch(1970-01-01) 반환
     */
    fun parseToDate(dateString: String?): Date {
        if (dateString.isNullOrEmpty()) return Date(0L)
        return try {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormatter.timeZone = serverTimeZone
            dateFormatter.parse(dateString) ?: Date(0L)
        } catch (e: Exception) {
            Date(0L)
        }
    }

    /**
     * Date 기반으로 상대 시간을 계산
     */
    fun getTimeAgo(date: Date): String {
        val now = Date()
        val diffInMillis = now.time - date.time

        if (diffInMillis < 0) return "방금 전"

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "방금 전"
            minutes in 1..59 -> "${minutes}분 전"
            hours in 1..23 -> "${hours}시간 전"
            days in 1..6 -> "${days}일 전"
            weeks in 1..3 -> "${weeks}주 전"
            months in 1..11 -> "${months}개월 전"
            else -> "${years}년 전"
        }
    }

    /**
     * 편리하게 String → 시간 Ago 변환
     */
    fun getTimeAgo(dateString: String?): String {
        val date = parseToDate(dateString)
        return getTimeAgo(date)
    }
}


fun CommentAuthorDTO.toDomain() = AuthorSummary(
    id = this.authId,
    nickname = this.authUserNickname ?: "",
    profileImageUrl = null
)

fun CommentItemDTO.toDomain() = CommentItem(
    id = this.id,
    author = this.author.toDomain(),
    content = this.content,
    createdAt = TimeUtils.parseToDate(this.createdAt)
)

fun List<CommentItemDTO>.toDomain(): List<CommentItem> = this.map { it.toDomain() }
fun createCommentRequest(content: String) = CommentCreateRequestDTO(content)