package com.sesac.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth // [추가]
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // [추가]
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // [추가]
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.sesac.auth.nav_graph.AuthNavigationRoute
import com.sesac.auth.presentation.AuthViewModel
import com.sesac.domain.result.JoinUiState
import com.sesac.common.R
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall

// --- [추가] 카카오 SDK 및 해시 키 로깅을 위한 import ---
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.auth.model.OAuthToken
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import com.kakao.sdk.user.UserApiClient
import java.security.MessageDigest
// ---

@Composable
fun AuthLoginScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    val email by remember { viewModel.loginEmail }
    val password by remember { viewModel.loginPassword }
    val uiState by viewModel.joinUiState.collectAsState()

    // --- [추가] 카카오 로그인 전용 로직 ---
    var isLoading by remember { mutableStateOf(false) } // 카카오 SDK 로딩 상태
    val context = LocalContext.current

    // 1. 카카오 해시 키 로깅 (프리뷰 모드 제외)
    if (!LocalInspectionMode.current) {
        LaunchedEffect(Unit) {
            try {
                val packageName = context.packageName
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES
                    )
                }
                val signatures: Array<Signature> =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        packageInfo.signingInfo?.apkContentsSigners ?: emptyArray()
                    } else {
                        @Suppress("DEPRECATION")
                        packageInfo.signatures ?: emptyArray()
                    }
                if (signatures.isEmpty()) {
                    Log.e("KakaoHashKey", "No signatures found.")
                    return@LaunchedEffect
                }
                for (signature in signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    Log.d("KakaoHashKey", "해시 키: $hashKey") // [중요] 이 값을 복사!
                }
            } catch (e: Exception) {
                Log.e("KakaoHashKey", "해시 키를 가져오지 못했습니다.", e)
            }
        }
    }

    // 2. 카카오 로그인 콜백 정의
    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        isLoading = false
        if (error != null) {
            Log.e("KakaoLoginTest", "카카오계정으로 로그인 실패", error)
            Toast.makeText(context, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.i("KakaoLoginTest", "카카오계정으로 로그인 성공! Access Token: ${token.accessToken}")
            Toast.makeText(context, "카카오 연동 성공!", Toast.LENGTH_SHORT).show()

            // [중요] 로그인 성공 시, ViewModel에 Access Token 전달
            // (AuthViewModel에 onKakaoLoginSuccess 함수를 만들어야 합니다)
            viewModel.onKakaoLoginSuccess(token.accessToken)
        }
    }
    // ---

    // (기존 DisposableEffect, LaunchedEffect(uiState)는 그대로)
    DisposableEffect(Unit) {
        viewModel.resetUiState()
        onDispose {
            viewModel.resetUiState()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is JoinUiState.Success) {
            onLoginSuccess()
        }
    }

    // [추가] 카카오 로그인 시 요청할 스코프 정의
    val kakaoLoginScopes = listOf("profile_nickname", "account_email")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(paddingLarge))
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.loginEmail.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            maxLines = 1,
            label = { Text(stringResource(R.string.auth_join_email_label)) }
        )
        Spacer(modifier = Modifier.height(paddingSmall))
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.loginPassword.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            maxLines = 1,
            label = { Text(stringResource(R.string.auth_join_password_label)) }
        )
        Spacer(modifier = Modifier.height(paddingLarge))

        // --- [수정] 로딩 및 오류 UI ---
        // (이메일 로딩 || 카카오 로딩)
        if (uiState is JoinUiState.Loading || isLoading) {
            CircularProgressIndicator()
        } else {
            // 로딩 중이 아닐 때만 버튼 표시
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { viewModel.onLoginClick() }) {
                    Text(stringResource(R.string.auth_login_button))
                }
                Button(
                    modifier = Modifier.padding(horizontal = paddingMedium),
                    onClick = { navController.navigate(AuthNavigationRoute.JoinTab) },
                ) {
                    Text(stringResource(R.string.auth_signup_button))
                }
            }
        }

        // 이메일 로그인 오류 메시지
        if (uiState is JoinUiState.Error) {
            Spacer(modifier = Modifier.height(paddingSmall))
            Text((uiState as JoinUiState.Error).message, color = MaterialTheme.colorScheme.error)
        }
        // ---

        Spacer(modifier = Modifier.height(paddingLarge))

        // --- [추가] 카카오 로그인 버튼 ---
        Button(
            onClick = {
                isLoading = true // 카카오 SDK 로딩 시작
                // 1. 카카오톡이 설치되어 있는지 확인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    // 2. 카카오톡으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            Log.e("KakaoLoginTest", "카카오톡 로그인 실패", error)
                            // 3. 카톡 실패 (예: 사용자 취소) 시, 카카오계정(웹뷰)으로 재시도
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                            } else {
                                kakaoLoginCallback(null, error)
                            }
                        } else if (token != null) {
                            kakaoLoginCallback(token, null)
                        }
                    }
                } else {
                    // 4. 카카오톡이 없으면, 카카오계정(웹뷰)으로 바로 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kakao Login")
        }
        // ---
    }
}