package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toBookmarkResponse
import com.sesac.data.mapper.toPathCreateRequestDTO
import com.sesac.data.mapper.toPathUpdateRequestDTO
import com.sesac.data.mapper.toPath
import com.sesac.data.mapper.toPathList
import com.sesac.data.source.api.PathApi
import com.sesac.data.source.local.datasource.MockTrail
import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.Path
import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrailRepositoryImpl @Inject constructor(
    private val pathApi: PathApi
): TrailRepository {
    override suspend fun getAllRecommendedPaths(coord: Coord?, radius: Float?): Flow<AuthResult<List<Path>>> = flow {
        emit(AuthResult.Loading)
        val paths = pathApi.getPaths(
//            token = "Bearer $token",
            lat = coord?.latitude,
            lng = coord?.longitude,
            radius = radius
        ).map { it.toPath() }
        emit(AuthResult.Success(paths))
    }.catch {
        Log.d("TAG-TrailRepository", "Recommend Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getMyPaths(token: String): Flow<AuthResult<List<Path>>> = flow {
        emit(AuthResult.Loading)
        val result = pathApi.getMyPaths("Bearer $token").toPathList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-TrailRepository", "My Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun createPath(token: String, path: Path): Flow<AuthResult<Path>> = flow {
        emit(AuthResult.Loading)
        val createdPath = pathApi.createPath(
            token = "Bearer $token",
            request = path.toPathCreateRequestDTO()
        )
        emit(AuthResult.Success(createdPath.toPath()))
    }.catch {
        Log.d("TAG-TrailRepository", "Create Path error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updatePath(token: String, id: Int, updatedPath: Path): Flow<AuthResult<Path>> = flow {
        emit(AuthResult.Loading)
        val result = pathApi.updatePath("Bearer $token", id, updatedPath.toPathUpdateRequestDTO()).toPath()
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

    override suspend fun toggleBookmark(
        token: String,
        id: Int
    ): Flow<AuthResult<BookmarkResponse>> = flow {
        emit(AuthResult.Loading)
        val result = pathApi.bookmarkToggle("Bearer $token", id).toBookmarkResponse()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-TrailRepository", "Toggle Bookmark error : $it")
        emit(AuthResult.NetworkError(it))
    }


    override suspend fun getAllMyRecord(): Flow<List<MyRecord?>> = flow {
        emit(MockTrail.myRecord)
    }

    override suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean> = flow {
        MockTrail.myRecord.add(newRecord)
        emit(true)
    }

}