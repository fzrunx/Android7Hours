package com.sesac.data.dto

import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.model.UserInfo

fun UserAPI.toUserInfo() = UserInfo(
    username = this.username,
    nickname = nickname,
)

fun List<UserAPI>.toUserInfoList() = this.map {
    it.toUserInfo()
}.toList()