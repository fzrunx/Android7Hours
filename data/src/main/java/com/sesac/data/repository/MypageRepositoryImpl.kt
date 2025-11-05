package com.sesac.data.repository

import com.sesac.data.source.local.datasource.MockMypage
import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.model.MypageMenuItem
import com.sesac.domain.model.MypagePermission
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.MypageStat
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor() : MypageRepository {
    override fun getMypageStats(): Flow<List<MypageStat>> = flow {
        emit(MockMypage.stats)
    }

//    override fun getMypageMenuItems(): Flow<List<MypageMenuItem>> = flow {
//        emit(MockMypage.menuItems)
//    }

    override fun getFavoriteWalkPaths(): Flow<List<FavoriteWalkPath>> = flow {
        emit(MockMypage.favoriteWalkPaths)
    }

    override fun deleteFavoriteWalkPaths(favoriteWalkPathId: Int): Flow<Boolean> = flow {
        val removed = MockMypage.favoriteWalkPaths.removeAll { it.id == favoriteWalkPathId }
        emit(removed)
    }

    override fun getFavoriteCommunityPosts(): Flow<List<FavoriteCommunityPost>> = flow {
        emit(MockMypage.favoritePosts)
    }

    override fun deleteFavoriteCommunityPosts(favoriteCommunityPostId: Int): Flow<Boolean> = flow {
        val removed = MockMypage.favoritePosts.removeAll { it.id == favoriteCommunityPostId }
        emit(removed)
    }

    override fun getSchedules(date: LocalDate): Flow<List<MypageSchedule>> = flow {
        val filtered = MockMypage.schedules.filter { it.date == date }
        emit(filtered)
    }

    override fun addSchedule(schedule: MypageSchedule): Flow<Boolean> = flow {
        MockMypage.schedules.add(schedule)
        emit(true)
    }

    override fun deleteSchedule(scheduleId: Long): Flow<Boolean> = flow {
        val removed = MockMypage.schedules.removeAll { it.id == scheduleId }
        emit(removed)
    }

//    override fun getMypagePermissions(): Flow<List<MypagePermission>> = flow {
//        emit(MockMypage.permissions)
//    }

    override fun updatePermissionStatus(key: String, isEnabled: Boolean): Flow<Boolean> = flow {
        // This is a mock implementation, so we don't actually persist the state.
        // In a real app, you would update a database or shared preferences here.
        emit(true)
    }
}
