package com.sesac.data.source.remote

import com.sesac.data.dto.PathUploadDto

interface PathRemoteDataSource {
    suspend fun uploadPath(dto: PathUploadDto): PathUploadDto
}