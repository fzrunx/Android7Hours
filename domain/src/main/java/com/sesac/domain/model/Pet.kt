package com.sesac.domain.model

data class Pet(
    val id: Int = -1,
    val owner: String,
    val name: String,
    val gender: String,
    val birthday: String,
    val neutering: Boolean,
    val breed: String,
) {
    companion object {
        val EMPTY = Pet(
            id = -1,
            owner = "",
            name = "",
            gender = "남아",
            birthday = "날짜를 선택해주세요",
            neutering = false,
            breed = "",
        )
    }
}

data class Breed(
    val id: Int = -1,
    val breedName: String,
)