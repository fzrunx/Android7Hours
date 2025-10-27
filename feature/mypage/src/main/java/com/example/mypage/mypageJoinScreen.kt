package com.example.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinScreen() {

    // 입력 필드 상태
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // 체크박스 상태
    var agreeAll by remember { mutableStateOf(false) }
    var agreeAge by remember { mutableStateOf(false) }
    var agreeTerms by remember { mutableStateOf(false) }
    var agreePrivacy by remember { mutableStateOf(false) }

    // '모두 동의'가 하위 체크박스들에 영향을 주도록 설정
    LaunchedEffect(agreeAll) {
        if (agreeAll) {
            agreeAge = true
            agreeTerms = true
            agreePrivacy = true
        }
    }
    // 하위 체크박스 중 하나라도 해제되면 '모두 동의'도 해제
    LaunchedEffect(agreeAge, agreeTerms, agreePrivacy) {
        if (!agreeAge || !agreeTerms || !agreePrivacy) {
            agreeAll = false
        }
    }


    val lightBlueBg = Color(0xFFE6F7FF)
    val contentBg = Color(0xFFF8F9FA) // 옅은 회색 배경

    Scaffold(
        // 1. 상단 앱 바 (이미지상 'Log In'으로 되어 있음)
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        "Log In", // 이미지에 "Log In"으로 되어있어 그대로 따름
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
        // 2. 메인 콘텐츠 (스크롤 폼)
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(contentBg)
                    .verticalScroll(rememberScrollState()) // 스크롤 가능하도록
                    .padding(horizontal = 24.dp, vertical = 32.dp), // 폼 내부 여백
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // 아이템 간 세로 간격
            ) {

                Text(
                    "환영합니다!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "통합 인력관리 솔루션 시프티, 지금 바로 시작해보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- 입력 필드 ---
                LabelledTextField(
                    label = "이메일",
                    value = email,
                    onValueChange = { email = it }
                )
                LabelledTextField(
                    label = "비밀번호",
                    value = password,
                    onValueChange = { password = it },
                    helperText = "* 10자 이상이면서 영문, 숫자, 특수문자를 모두 포함하세요",
                    isPassword = true
                )
                LabelledTextField(
                    label = "비밀번호 재확인",
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it },
                    helperText = "* 비밀번호를 다시 입력해주세요",
                    isPassword = true
                )

                // 이름, 전화번호 (가로 배치)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        LabelledTextField(
                            label = "이름",
                            value = name,
                            onValueChange = { name = it }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        LabelledTextField(
                            label = "전화번호 (선택)",
                            value = phone,
                            onValueChange = { phone = it },
                            keyboardType = KeyboardType.Phone
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- 약관 동의 ---
                AgreementSection(
                    agreeAll = agreeAll,
                    onAgreeAllChange = { agreeAll = it },
                    agreeAge = agreeAge,
                    onAgreeAgeChange = { agreeAge = it },
                    agreeTerms = agreeTerms,
                    onAgreeTermsChange = { agreeTerms = it },
                    agreePrivacy = agreePrivacy,
                    onAgreePrivacyChange = { agreePrivacy = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- 가입하기 버튼 ---
                Button(
                    onClick = { /* TODO: 가입하기 로직 */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "가입하기",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp
                    )
                }

                // 'or' 구분선
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Divider(modifier = Modifier.weight(1f))
                    Text("or", color = Color.Gray, fontSize = 12.sp)
                    Divider(modifier = Modifier.weight(1f))
                }

                // --- 소셜 가입 버튼 ---
                SocialJoinButton(
                    text = "구글 계정으로 가입하기",
                    icon = Icons.Default.AccountCircle, // TODO: painterResource(id = R.drawable.google_logo)로 교체
                    tint = Color.Red,
                    onClick = { /* TODO: 구글 가입 */ }
                )
                SocialJoinButton(
                    text = "네이버 계정으로 가입하기",
                    icon = Icons.Default.AccountCircle,
                    tint = Color.Green,
                    onClick = { /* TODO: 네이버 가입 */ }
                )
                SocialJoinButton(
                    text = "SSO로 가입하기",
                    icon = Icons.Default.AccountCircle,
                    onClick = { /* TODO: SSO 가입 */ }
                )
                SocialJoinButton(
                    text = "카카오워크로 가입하기",
                    icon = Icons.Default.AccountCircle,
                    tint = Color.Yellow,
                    onClick = { /* TODO: 카카오워크 가입 */ }
                )
                Spacer(modifier = Modifier.height(16.dp)) // 스크롤 하단 여백
            }
        }
    )
}

/**
 * 라벨이 있는 텍스트 입력 필드
 */
@Composable
fun LabelledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    helperText: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            )
        )
        helperText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * 약관 동의 섹션
 */
@Composable
fun AgreementSection(
    agreeAll: Boolean, onAgreeAllChange: (Boolean) -> Unit,
    agreeAge: Boolean, onAgreeAgeChange: (Boolean) -> Unit,
    agreeTerms: Boolean, onAgreeTermsChange: (Boolean) -> Unit,
    agreePrivacy: Boolean, onAgreePrivacyChange: (Boolean) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreeAll, onCheckedChange = onAgreeAllChange)
            Text("모두 동의합니다", fontWeight = FontWeight.Bold)
        }
        Divider(color = Color.LightGray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreeAge, onCheckedChange = onAgreeAgeChange)
            Text("[필수] 만 14세 이상입니다", color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreeTerms, onCheckedChange = onAgreeTermsChange)
            Text("[필수] 최종이용자 이용약관에 동의합니다", color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreePrivacy, onCheckedChange = onAgreePrivacyChange)
            Text("[필수] 개인정보 수집 및 이용에 동의합니다", color = Color.Gray)
        }
    }
}

/**
 * 재사용 가능한 소셜 가입 버튼
 */
@Composable
fun SocialJoinButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = tint)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, textAlign = TextAlign.Center)
        }
    }
}


// Android Studio에서 미리보기
@Preview(showBackground = true)
@Composable
fun JoinScreenPreview() {
    // 앱의 테마로 감싸주면 더 정확한 미리보기가 가능합니다.
    // 예: YourAppTheme { JoinScreen() }
    JoinScreen()
}