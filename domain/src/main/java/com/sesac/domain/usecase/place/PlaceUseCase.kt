package com.sesac.domain.usecase.place

import jakarta.inject.Inject


data class PlaceUseCase @Inject constructor (
    val getPlaceUseCase: GetPlaceUseCase
)
