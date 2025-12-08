package com.sesac.domain.result

sealed class LocationFlowResult<out T> {
    data class Success<T>(val coord: T) : LocationFlowResult<T>()
    data class Error(val exception: LocationException) : LocationFlowResult<Nothing>()
}

sealed class LocationException(message: String) : Exception(message) {
    object PermissionDenied : LocationException("위치 권한이 거부되었습니다")
    object LocationDisabled : LocationException("위치 서비스가 비활성화되어 있습니다")
    object Timeout : LocationException("위치를 찾을 수 없습니다")
    object NoLocation : LocationException("사용 가능한 위치 정보가 없습니다")
    data class Unknown(val throwable: Throwable?) : LocationException("알 수 없는 오류: ${throwable?.message}")
}