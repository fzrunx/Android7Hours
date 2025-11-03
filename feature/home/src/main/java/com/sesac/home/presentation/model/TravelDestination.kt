package com.sesac.home.presentation.model

data class TravelDestination(
    override val id: Int,
    override val name: String,
    override val image: String,
    val location: String,
    val description: String,
) : HomeCardData(id, name, image)