package com.sesac.data.repository

import android.util.Log
import com.sesac.data.dao.PathDao
import com.sesac.data.mapper.toMyRecord
import com.sesac.data.mapper.toPathCreateRequestDTO
import com.sesac.data.mapper.toPathEntity
import com.sesac.data.mapper.toPathUpdateRequestDTO
import com.sesac.data.mapper.toUserPath
import com.sesac.data.mapper.toUserPathList
import com.sesac.data.source.api.PathApi
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.UserPath
import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrailRepositoryImpl @Inject constructor(
    private val pathApi: PathApi,
    private val pathDao: PathDao
): TrailRepository {
    override suspend fun getAllRecommendedPaths(coord: Coord?, radius: Float?): Flow<AuthResult<List<UserPath>>> = flow {
        emit(AuthResult.Loading)
        val paths = pathApi.getPaths(
//            token = "Bearer $token",
            lat = coord?.latitude,
            lng = coord?.longitude,
            radius = radius
        ).map { it.toUserPath() }
        emit(AuthResult.Success(paths))
    }.catch {
        Log.d("TAG-TrailRepository", "Recommend Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getMyPaths(token: String): Flow<AuthResult<List<UserPath>>> = flow {
        emit(AuthResult.Loading)
        val result = pathApi.getMyPaths("Bearer $token").toUserPathList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-TrailRepository", "My Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun createPath(token: String, path: UserPath): Flow<AuthResult<UserPath>> = flow {
        emit(AuthResult.Loading)
        val createdPath = pathApi.createPath(
            token = "Bearer $token",
            request = path.toPathCreateRequestDTO()
        )
        emit(AuthResult.Success(createdPath.toUserPath()))
    }.catch {
        Log.d("TAG-TrailRepository", "Create Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updatePath(token: String, id: Int, updatedPath: UserPath): Flow<AuthResult<UserPath>> = flow {
        emit(AuthResult.Loading)
        val result = pathApi.updatePath("Bearer $token", id, updatedPath.toPathUpdateRequestDTO()).toUserPath()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-TrailRepository", "Update Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deletePath(token: String, id: Int): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        pathApi.deletePath("Bearer $token", id)
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-TrailRepository", "Delete Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getAllMyRecord(): Flow<List<MyRecord>> =
        pathDao.getDraftsByStatusFlow(DraftStatus.RECORD).map { list ->
            list.map { it.toMyRecord() }
        }

    override suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean> = flow {
        pathDao.insertDraft(newRecord.toPathEntity())
        emit(true)
    }
    // ⭐ Local(Room)
    override suspend fun saveDraft(draft: UserPath): Flow<Boolean> = flow {
        val entity = draft.toPathEntity()  // Mapper 필요
        pathDao.insertDraft(entity)
        emit(true)
    }

    override suspend fun getAllDrafts(): Flow<List<UserPath>> = flow {
        val entities = pathDao.getDraftsByStatus()
        emit(entities.map { it.toUserPath() }) // Mapper 필요
    }

    override suspend fun deleteDraft(draft: UserPath): Flow<Boolean> = flow {
        val entity = draft.toPathEntity()
        pathDao.deleteDraft(entity)
        emit(true)
    }

    override suspend fun clearAllDrafts(): Flow<Boolean> = flow {
        pathDao.clearAllDrafts()
        emit(true)
    }

}