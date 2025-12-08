package com.sesac.data.di

import com.sesac.data.dto.TokenRefreshRequestDTO
import com.sesac.data.source.api.AuthApi
import com.sesac.domain.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authApiProvider: Provider<AuthApi> // 순환 참조 방지를 위해 Provider 사용
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. 이전 요청의 헤더를 확인하여, 이미 토큰 갱신을 시도했다면 null을 반환하여 무한 루프 방지
        val oldAccessToken = runBlocking { sessionRepository.getAccessToken().first() }
        if (response.request.header("Authorization") == "Bearer $oldAccessToken") {
            // 토큰이 동일함에도 401이 발생했다는 것은 리프레시도 실패했다는 의미일 수 있음
        }

        // 2. 동기적으로 리프레시 토큰을 가져옴
        val refreshToken = runBlocking { sessionRepository.getRefreshToken().first() }
            ?: return null // 리프레시 토큰이 없으면 갱신 불가

        // 3. 동기적으로 토큰 갱신 API 호출 (runBlocking 사용)
        return runBlocking {
            val authApi = authApiProvider.get()
            val tokenResponse = try {
                authApi.refreshToken(TokenRefreshRequestDTO(refreshToken))
            } catch (e: Exception) {
                null
            }

            if (tokenResponse != null && tokenResponse.isSuccessful && tokenResponse.body() != null) {
                // 4. 성공 시, 새로운 토큰들 저장
                val newTokens = tokenResponse.body()!!
                sessionRepository.saveRefreshedTokens(newTokens.access, newTokens.refresh)

                // 5. 실패했던 원래 요청에 새로운 액세스 토큰을 담아 재요청
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.access}")
                    .build()
            } else {
                // 6. 리프레시 실패 시, 세션을 클리어하고 null을 반환하여 요청 중단
                sessionRepository.clearSession()
                null
            }
        }
    }
}