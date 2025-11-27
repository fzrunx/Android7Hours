package com.sesac.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // [추가] 텍스트 버튼
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // [추가] 색상 지정을 위해
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

// --- 카카오 SDK import ---
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
    onNavigateToFindAccount: () -> Unit // [추가] 아이디/비번 찾기 화면으로 이동하는 콜백
) {
    val email by remember { viewModel.loginEmail }
    val password by remember { viewModel.loginPassword }
    val uiState by viewModel.joinUiState.collectAsState()

    // --- 카카오 로그인 전용 로직 (기존 코드 유지) ---
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (!LocalInspectionMode.current) {
        LaunchedEffect(Unit) {
            // (해시 키 로깅 로직 생략 - 기존과 동일)
        }
    }
    // ---

    DisposableEffect(Unit) {
        viewModel.resetUiState()
        onDispose { viewModel.resetUiState() }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is JoinUiState.Success -> onLoginSuccess()
            is JoinUiState.Error -> isLoading = false
            is JoinUiState.Loading -> isLoading = true
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.auth_login_button), style = MaterialTheme.typography.headlineMedium)
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

        // --- 로딩 및 버튼 영역 ---
        if (uiState is JoinUiState.Loading || isLoading) {
            CircularProgressIndicator()
        } else {
            // 1. 로그인 & 회원가입 버튼 행
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

            // [추가] 2. 아이디/비밀번호 찾기 버튼 (텍스트 버튼 형태)
            TextButton(
                onClick = onNavigateToFindAccount,
                modifier = Modifier.padding(vertical = paddingSmall)
            ) {
                Text(
                    text = "아이디 / 비밀번호 찾기",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray // 너무 튀지 않게 회색 처리
                )
            }
        }

        // 에러 메시지
        if (uiState is JoinUiState.Error) {
            Spacer(modifier = Modifier.height(paddingSmall))
            Text((uiState as JoinUiState.Error).message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(paddingLarge))

        // --- 카카오 로그인 버튼 ---
        Button(
            onClick = {
                // (카카오 로그인 로직 생략 - 기존과 동일)
                isLoading = true
                // ... (생략) ...
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kakao Login")
        }
    }
}