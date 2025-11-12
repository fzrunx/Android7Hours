package com.sesac.domain.local.model

sealed class HomeCardData(
    open val id: Int,
    open val name: String,
    open val image: String,
)

data class BannerData(
    val id: Int,
    val image: String,
    val title: String,
    val subtitle: String
)

data class DogCafe(
    override val id: Int,
    override val name: String,
    override val image: String,
    val location: String,
    val description: String,
) : HomeCardData(id, name, image)

data class TravelDestination(
    override val id: Int,
    override val name: String,
    override val image: String,
    val location: String,
    val description: String,
) : HomeCardData(id, name, image)

data class WalkPath(
    override val id: Int,
    override val name: String,
    override val image: String,
    val distance: Float,
    val timeTaken: Int,
    val difficulty: String,
) : HomeCardData(id, name, image)