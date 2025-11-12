package com.sesac.domain.local.usecase.mypage

import com.sesac.domain.local.model.MypageStat
import com.sesac.domain.local.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Stats
class GetMypageStatsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<MypageStat>> = repository.getMypageStats()
}