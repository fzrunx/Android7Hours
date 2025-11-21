package com.sesac.domain.usecase.trail

data class TrailUseCase(
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
    val clearAllDraftsUseCase: ClearAllDraftsUseCase
)