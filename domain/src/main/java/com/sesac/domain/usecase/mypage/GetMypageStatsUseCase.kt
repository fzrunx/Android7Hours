package com.sesac.domain.usecase.mypage

import com.sesac.domain.model.MypageStat
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Stats
class GetMypageStatsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<MypageStat>> = repository.getMypageStats()
}