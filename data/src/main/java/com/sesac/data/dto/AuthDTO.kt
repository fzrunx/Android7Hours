package com.sesac.data.dto

import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.model.UserInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
fun UserInfo.toUserAPI() = mapOf(
    "username" to username,
    "nickname" to nickname,
    "full_name" to fullName,
    "email" to email,
)

fun UserAPI.toUserInfo() = UserInfo(
    username = username,
    nickname = nickname,
    fullName = fullName,
    email = email,
)

fun List<UserAPI>.toUserInfoList() = this.map {
    it.toUserInfo()
}.toList()