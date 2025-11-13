package com.sesac.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.local.model.BannerData
import com.sesac.domain.local.model.Community
import com.sesac.domain.local.model.DogCafe
import com.sesac.domain.local.model.TravelDestination
import com.sesac.domain.local.model.WalkPath
import com.sesac.domain.local.usecase.community.CommunityUseCase
import com.sesac.domain.local.usecase.home.HomeUseCase
import com.sesac.domain.remote.model.User
import com.sesac.domain.remote.result.AuthResult
import com.sesac.domain.remote.usecase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val communityUseCase: CommunityUseCase,
    private val authUseCase: AuthUseCase,
): ViewModel() {
    private val _bannerList = MutableStateFlow<List<BannerData?>>(emptyList())
    private val _dogCafelist = MutableStateFlow<List<DogCafe?>>(emptyList())
    private val _travelDestinationList = MutableStateFlow<List<TravelDestination?>>(emptyList())
    private val _walkPathDestinationList = MutableStateFlow<List<WalkPath?>>(emptyList())
    private val _communityList = MutableStateFlow<List<Community?>>(emptyList())
    private val _userAPIList = MutableStateFlow<AuthResult<List<User>>>(AuthResult.NoConstructor)

    val bannerList get() = _bannerList.asStateFlow()
    val dogCafeList get() = _dogCafelist.asStateFlow()
    val travelDestinationList get() = _travelDestinationList.asStateFlow()
    val walkPathList get() = _walkPathDestinationList.asStateFlow()
    val communityList get() = _communityList.asStateFlow()
    val userList get() = _userAPIList.asStateFlow()

    init {
        viewModelScope.launch {
//            authUseCase.getAllUsers().collectLatest { _userAPIList.value = it }
            homeUseCase.getAllBannersUseCase().collectLatest { _bannerList.value = it }
            homeUseCase.getAllDogCafeUseCase().collectLatest { _dogCafelist.value = it }
            homeUseCase.getAllTravelDestinationUseCase().collectLatest { _travelDestinationList.value = it }
            homeUseCase.getAllWalkPathUseCase().collectLatest { _walkPathDestinationList.value = it }
            communityUseCase.getAllCommunityUseCase().collectLatest { _communityList.value = it }
        }
    }

    fun getAllUser() {
        viewModelScope.launch {
            authUseCase.getAllUsers().collectLatest { _userAPIList.value = it }
        }
    }
}