package com.sesac.domain.local.usecase.mypage

import com.sesac.domain.local.model.MypageSchedule
import com.sesac.domain.local.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddScheduleUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(schedule: MypageSchedule): Flow<Boolean> = repository.addSchedule(schedule)
}