package com.sesac.domain.usecase.path

data class PathUseCase(
    val getAllMyRecordUseCase: GetAllMyRecordUseCase,
    val createPathUseCase: CreatePathUseCase,
    val updatePathUseCase: UpdatePathUseCase,
    val deletePathUseCase: DeletePathUseCase,
    val getAllRecommendedPathsUseCase: GetAllRecommendedPathsUseCase,
    val getMyPaths: GetMyPaths,
)