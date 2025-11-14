package com.sesac.domain.usecase.mypage

import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteScheduleUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(scheduleId: Long): Flow<Boolean> = repository.deleteSchedule(scheduleId)
}