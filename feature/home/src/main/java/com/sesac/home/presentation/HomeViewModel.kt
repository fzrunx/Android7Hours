package com.sesac.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.BannerData
import com.sesac.domain.model.Community
import com.sesac.domain.model.DogCafe
import com.sesac.domain.model.TravelDestination
import com.sesac.domain.model.WalkPath
import com.sesac.domain.usecase.GetAllBannersUseCase
import com.sesac.domain.usecase.GetAllCommunityUseCase
import com.sesac.domain.usecase.GetAllDogCafeUseCase
import com.sesac.domain.usecase.GetAllTravelDestinationUseCase
import com.sesac.domain.usecase.GetAllWalkPathUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllBannersUseCase: GetAllBannersUseCase,
    private val getAllDogCafeUseCase: GetAllDogCafeUseCase,
    private val getAllTravelDestinationUseCase: GetAllTravelDestinationUseCase,
    private val getAllWalkPathUseCase: GetAllWalkPathUseCase,
    private val getAllCommunityUseCase: GetAllCommunityUseCase,
): ViewModel() {
    private val _bannerList = MutableStateFlow<List<BannerData?>>(emptyList())
    private val _dogCafelist = MutableStateFlow<List<DogCafe?>>(emptyList())
    private val _travelDestinationList = MutableStateFlow<List<TravelDestination?>>(emptyList())
    private val _walkPathDestinationList = MutableStateFlow<List<WalkPath?>>(emptyList())
    private val _communityList = MutableStateFlow<List<Community?>>(emptyList())

    val bannerList get() = _bannerList.asStateFlow()
    val dogCafeList get() = _dogCafelist.asStateFlow()
    val travelDestinationList get() = _travelDestinationList.asStateFlow()
    val walkPathList get() = _walkPathDestinationList.asStateFlow()
    val communityList get() = _communityList.asStateFlow()

    init {
        viewModelScope.launch {
            getAllBannersUseCase().collect { _bannerList.value = it }
            getAllDogCafeUseCase().collect { _dogCafelist.value = it }
            getAllTravelDestinationUseCase().collect { _travelDestinationList.value = it }
            getAllWalkPathUseCase().collect { _walkPathDestinationList.value = it }
            getAllCommunityUseCase().collect { _communityList.value = it }
        }
    }
}