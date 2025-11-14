package com.sesac.domain.usecase.mypage

import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddScheduleUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(schedule: MypageSchedule): Flow<Boolean> = repository.addSchedule(schedule)
}