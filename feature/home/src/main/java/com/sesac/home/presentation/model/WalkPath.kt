package com.sesac.home.presentation.model

data class WalkPath(
    override val id: Int,
    override val name: String,
    override val image: String,
    val distance: String,
    val time: String,
    val difficulty: String,
) : HomeCardData(id, name, image)