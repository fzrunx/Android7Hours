package com.sesac.domain.model

data class Pet(
    val id: Int = -1,
    val owner: String,
    val name: String,
    val gender: String,
    val birthday: String,
    val neutering: Boolean,
    val breed: String,
)

data class Breed(
    val id: Int = -1,
    val breedName: String,
)