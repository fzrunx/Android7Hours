package com.sesac.data.source.remote

import com.sesac.data.dto.PathUploadDto
import com.sesac.data.source.remote.api.PathApi

class PathRemoteDataSourceImpl(
    private val api: PathApi
) : PathRemoteDataSource {

    override suspend fun uploadPath(dto: PathUploadDto): PathUploadDto {
        return api.uploadPath(dto) // 서버가 반환하는 PathUploadDto 그대로 반환
    }
}