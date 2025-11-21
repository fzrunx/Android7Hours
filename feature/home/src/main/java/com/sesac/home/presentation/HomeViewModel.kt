package com.sesac.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.BannerData
import com.sesac.domain.model.Community
import com.sesac.domain.model.DogCafe
import com.sesac.domain.model.TravelDestination
import com.sesac.domain.usecase.community.CommunityUseCase
import com.sesac.domain.usecase.home.HomeUseCase
import com.sesac.domain.model.User
import com.sesac.domain.model.UserPath
import com.sesac.domain.result.AuthResult
import com.sesac.domain.usecase.trail.TrailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trailUseCase: TrailUseCase,
    private val homeUseCase: HomeUseCase,
    private val communityUseCase: CommunityUseCase,
): ViewModel() {
    private val _recommendPathList = MutableStateFlow<AuthResult<List<UserPath?>>>(AuthResult.NoConstructor)
    val recommendPathList get() = _recommendPathList.asStateFlow()
    private val _bannerList = MutableStateFlow<List<BannerData?>>(emptyList())
    val bannerList get() = _bannerList.asStateFlow()
    private val _communityList = MutableStateFlow<List<Community?>>(emptyList())
    val communityList get() = _communityList.asStateFlow()

    init {
        viewModelScope.launch {
            homeUseCase.getAllBannersUseCase().collectLatest { _bannerList.value = it }
            getRecommendedPaths()
            communityUseCase.getAllCommunityUseCase().collectLatest { _communityList.value = it }
        }
    }

    fun getRecommendedPaths() {
        viewModelScope.launch {
            trailUseCase.getAllRecommendedPathsUseCase(null, null).collectLatest { _recommendPathList.value = it }
        }
    }

}