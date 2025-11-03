package com.sesac.home.presentation.model

sealed class HomeCardData(
    open val id: Int,
    open val name: String,
    open val image: String,
)