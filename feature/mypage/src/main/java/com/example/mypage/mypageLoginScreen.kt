package com.example.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 하단 내비게이션 아이템을 위한 data class
data class NavItem(
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {

    // 입력 필드 상태
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepLoggedIn by remember { mutableStateOf(false) }

    // 하단 내비게이션 상태
    var selectedNavItem by remember { mutableStateOf(4) } // 마이페이지 선택
    val navItems = listOf(
        NavItem("홈", Icons.Default.Home),
        NavItem("산책로", Icons.Default.Hiking),
        NavItem("모니터링", Icons.Default.Monitor),
        NavItem("커뮤니티", Icons.Default.Comment),
        NavItem("마이페이지", Icons.Default.Person)
    )

    // 화면 전체의 옅은 하늘색 (Top/Bottom Bar)
    val lightBlueBg = Color(0xFFE6F7FF)
    // 메인 콘텐츠의 옅은 회색
    val lightGrayBg = Color(0xFFF8F9FA)

    Scaffold(
        // 1. 상단 앱 바
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        "My Page", // 타이틀 변경
//                        fontWeight = FontWeight.Bold,
//                        color = Color.DarkGray
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { /* TODO: 메뉴 열기 */ }) {
//                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* TODO: 프로필 가기 */ }) {
//                        Icon(Icons.Default.AccountCircle, contentDescription = "프로필")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = lightBlueBg
//                )
//            )
//        },
        // 2. 메인 콘텐츠
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(lightGrayBg), // 배경을 옅은 회색으로
                contentAlignment = Alignment.Center // 자식(로그인 카드)을 중앙에 배치
            ) {
                LoginCard(
                    username = username,
                    onUsernameChange = { username = it },
                    password = password,
                    onPasswordChange = { password = it },
                    keepLoggedIn = keepLoggedIn,
                    onKeepLoggedInChange = { keepLoggedIn = it },
                    onLoginClick = { /* TODO: Handle login */ },
                    onCloseClick = { /* TODO: Handle close */ },
                    onSignUpClick = { /* TODO: Handle sign up */ },
                    onFindCredentialsClick = { /* TODO: Handle find credentials */ },
                    onGoogleLoginClick = { /* TODO: Handle Google login */ },
                    onFacebookLoginClick = { /* TODO: Handle Facebook login */ },
                    onNaverLoginClick = { /* TODO: Handle Naver login */ },
                    onKakaoLoginClick = { /* TODO: Handle Kakao login */ }
                )
            }
        }
    )
}

@Composable
fun LoginCard(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    keepLoggedIn: Boolean,
    onKeepLoggedInChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onFindCredentialsClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onFacebookLoginClick: () -> Unit,
    onNaverLoginClick: () -> Unit,
    onKakaoLoginClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp, // 카드에 그림자 효과
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // 화면 좌우 여백
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // 아이템 간 세로 간격
        ) {
            // 1. 헤더 (제목 + 닫기 버튼)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "로그인 또는 회원가입",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                }
            }

            // 2. 부제목
            Text("위니브에서 여러분의 궁금증을 해결하세요! :)")

            // 3. 아이디/비밀번호 입력
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("아이디") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(), // 비밀번호 가리기
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 4. 로그인 상태 유지
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = keepLoggedIn,
                    onCheckedChange = onKeepLoggedInChange
                )
                Text("로그인 상태 유지")
            }

            // 5. 로그인 버튼
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "로그인",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 6. 회원가입 / 찾기
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onSignUpClick) {
                    Text("회원가입", fontSize = 14.sp)
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(14.dp)
                        .width(1.dp)
                )
                TextButton(onClick = onFindCredentialsClick) {
                    Text("아이디/비밀번호 찾기", fontSize = 14.sp)
                }
            }

            // 7. '또는' 구분선
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("또는", color = Color.Gray, fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f))
            }

            // 8. 소셜 로그인 버튼들
            SocialLoginButton(
                text = "구글 계정으로 로그인",
                iconPlaceholder = "G",
                backgroundColor = Color.White,
                borderColor = Color.LightGray,
                onClick = onGoogleLoginClick
            )
            SocialLoginButton(
                text = "페이스북 계정으로 로그인",
                iconPlaceholder = "f",
                backgroundColor = Color(0xFF1877F2),
                borderColor = Color.Transparent,
                onClick = onFacebookLoginClick
            )
            SocialLoginButton(
                text = "네이버 계정으로 로그인",
                iconPlaceholder = "N",
                backgroundColor = Color(0xFF03C75A),
                borderColor = Color.Transparent,
                onClick = onNaverLoginClick
            )
            SocialLoginButton(
                text = "카카오톡 계정으로 로그인",
                iconPlaceholder = "K",
                backgroundColor = Color(0xFFFFE812),
                borderColor = Color.Transparent,
                onClick = onKakaoLoginClick
            )
        }
    }
}

/**
 * 재사용 가능한 소셜 로그인 버튼
 */
@Composable
fun SocialLoginButton(
    text: String,
    iconPlaceholder: String,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: 이 Box를 실제 로고 Icon이나 Image로 교체하세요.
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    iconPlaceholder,
                    color = if (backgroundColor == Color(0xFFFFE812)) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Android Studio에서 미리보기
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // 앱의 테마로 감싸주면 더 정확한 미리보기가 가능합니다.
    // 예: YourAppTheme { LoginScreen() }
    LoginScreen()
}
