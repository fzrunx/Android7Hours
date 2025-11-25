package com.sesac.domain.usecase.path

data class PathUseCase(
    val getAllMyRecordUseCase: GetAllMyRecordUseCase,
    val createPathUseCase: CreatePathUseCase,
    val updatePathUseCase: UpdatePathUseCase,
    val deletePathUseCase: DeletePathUseCase,
    val getAllRecommendedPathsUseCase: GetAllRecommendedPathsUseCase,
    val getMyPaths: GetMyPaths,
    // ⭐ Draft 관련 UseCase 추가
    val saveDraftUseCase: SaveDraftUseCase,
    val getAllDraftsUseCase: GetAllDraftsUseCase,
    val deleteDraftUseCase: DeleteDraftUseCase,
    val clearAllDraftsUseCase: ClearAllDraftsUseCase,
    // server 관련 UseCase 추가
    val uploadPathUseCase: UploadPathUseCase

)