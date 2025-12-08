package com.sesac.domain.usecase.mypage

import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import javax.inject.Inject

class GetSchedulesUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(date: LocalDate): Flow<List<MypageSchedule>> = repository.getSchedules(date)
}