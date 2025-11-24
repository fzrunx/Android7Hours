package com.sesac.data.mapper

import android.content.Context
import com.sesac.common.utils.getTimeAgo
import com.sesac.common.utils.parseDate
import com.sesac.data.dto.AuthorDTO
import com.sesac.data.dto.CommentDTO
import com.sesac.domain.model.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object CommentMapper {

    fun CommentDTO.toDomain(context: Context, postId: Int): Comment {
        val parsedDate = parseDate(createdAt)
        val timeAgoString = parsedDate.getTimeAgo(context)

        return Comment(
            id = id.toLong(),
            postId = postId,
            authorId = author.id,
            author = author.nickname,
            content = content,
            timeAgo = timeAgoString,
            authorImage = author.image ?: "https://img.icons8.com/?size=100&id=Fx70T4fgtNmt&format=png&color=000000"
        )
    }

    fun List<CommentDTO>.toDomain(context: Context, postId: Int): List<Comment> {
        return this.map { it.toDomain(context, postId) }
    }

    fun Comment.toData(): CommentDTO {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val createdAtString = sdf.format(Date())

        return CommentDTO(
            id = id.toInt(),
            author = AuthorDTO(
                id = authorId,
                nickname = author,
                image = authorImage
            ),
            content = content,
            createdAt = createdAtString
        )
    }
}