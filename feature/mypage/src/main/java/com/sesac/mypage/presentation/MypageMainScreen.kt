package com.sesac.mypage.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sesac.common.R// ⚠️ 본인의 R 패키지 경로로 수정하세요.

// --- 1. 테마 및 상수 (AppTheme.kt) ---

object AppTheme {
    // Colors
    val background = Color(0xFFF9FAFB) // bg-gray-50
    val surface = Color.White
    val header = Color(0xFFDBE8CC)
    val textPrimary = Color(0xFF1F2937) // text-gray-900
    val textSecondary = Color(0xFF6B7280) // text-gray-500
    val iconTint = Color(0xFF9CA3AF) // text-gray-400

    val accentPurple = Color(0xFF8B5CF6)
    val accentBlue = Color(0xFF3B82F6)
    val accentGreen = Color(0xFF22C55E)

    val accentPurpleContainer = Color(0xFFEDE9FE) // bg-purple-100
    val onAccentPurpleContainer = Color(0xFF6D28D9) // text-purple-600

    val statPurple = Brush.verticalGradient(listOf(Color(0xFFA855F7), Color(0xFF8B5CF6)))
    val statBlue = Brush.verticalGradient(listOf(Color(0xFF60A5FA), Color(0xFF3B82F6)))
    val statGreen = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF22C55E)))

    val accentRed = Color(0xFFEF4444) // text-red-500
    val accentRedBorder = Color(0xFFFEE2E2) // border-red-200
    val accentRedContainer = Color(0xFFFEF2F2) // bg-red-50

    // Dimensions
    val paddingLarge = 16.dp
    val paddingMedium = 12.dp
    val paddingSmall = 8.dp
    val headerHeight = 64.dp
    val avatarSize = 80.dp
    val statIconBoxSize = 48.dp

    // Shapes
    val shapeCard = RoundedCornerShape(16.dp)
    val shapeButton = RoundedCornerShape(12.dp)
}

// --- 2. 데이터 모델 (MyPageData.kt) ---

data class StatItem(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val brush: Brush
)

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int? = null,
    val route: String // 클릭 시 이동할 경로
)

object MyPageDataSource {
    val stats = listOf(
        StatItem(Icons.Default.LocationOn, "산책 거리", "42.5km", AppTheme.statPurple),
        StatItem(Icons.Default.Schedule, "산책 시간", "12.5시간", AppTheme.statBlue),
        StatItem(Icons.Default.CalendarToday, "산책 횟수", "28회", AppTheme.statGreen)
    )

    val menuItems = listOf(
        MenuItem(Icons.Default.CalendarToday, "일정관리", route = "schedule"),
        MenuItem(Icons.Default.Star, "즐겨찾기", route = "favorites"),
        MenuItem(Icons.Default.Settings, "설정", route = "settings"),
        MenuItem(Icons.Default.Notifications, "알림", badgeCount = 3, route = "notifications"),
        MenuItem(Icons.AutoMirrored.Filled.Help, "도움말", route = "help"),
        MenuItem(Icons.Default.Shield, "개인정보 처리방침", route = "privacy")
    )
}

// --- 3. 내비게이션 및 화면 (MyPageScreen.kt) ---

/**
 * React의 <MyPage> 컴포넌트와 동일한 진입점.
 * NavHost를 포함하여 하위 페이지 간의 이동을 관리합니다.
 */
@Composable
fun MyPageScreen(
    onNavigateToHome: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        // 메인 마이페이지
        composable("main") {
            MyPageMainScreen(
                navController = navController,
                onNavigateToHome = onNavigateToHome
            )
        }

        // 하위 페이지들
        composable("profile") {
            ProfileDetailPage(onBack = { navController.popBackStack() })
        }
        composable("schedule") {
            SchedulePage(onBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsPage(onBack = { navController.popBackStack() })
        }
        composable("favorites") {
            FavoritesPage(onBack = { navController.popBackStack() })
        }

        // 메뉴 항목의 나머지 페이지 (임시)
        composable("notifications") {
            PlaceholderSubPage("알림", onBack = { navController.popBackStack() })
        }
        composable("help") {
            PlaceholderSubPage("도움말", onBack = { navController.popBackStack() })
        }
        composable("privacy") {
            PlaceholderSubPage("개인정보 처리방침", onBack = { navController.popBackStack() })
        }
    }
}

/**
 * React 코드의 'main' 상태 UI
 */
@Composable
fun MyPageMainScreen(
    navController: NavController,
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            MyPageAppBar(
                title = "마이페이지",
                onNavigateToHome = onNavigateToHome
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.background),
            contentPadding = PaddingValues(bottom = AppTheme.paddingLarge)
        ) {
            // --- 프로필 섹션 ---
            item {
                ProfileHeader(
                    name = "김반려",
                    email = "kimbanrye@email.com",
                    imageUrl = "https://images.unsplash.com/photo-1724435811349-32d27f4d5806?...",
                    onNavigateToProfile = {
                        navController.navigate("profile")
                    }
                )
            }

            // --- 통계 섹션 ---
            item {
                StatsSection(stats = MyPageDataSource.stats)
            }

            // --- 메뉴 섹션 ---
            item {
                Text(
                    text = "메뉴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.textPrimary,
                    modifier = Modifier.padding(
                        start = AppTheme.paddingLarge,
                        top = AppTheme.paddingLarge,
                        bottom = AppTheme.paddingSmall
                    )
                )
            }
            items(MyPageDataSource.menuItems) { item ->
                MenuItemRow(
                    item = item,
                    onClick = {
                        navController.navigate(item.route)
                    }
                )
            }

            // --- 로그아웃 버튼 ---
            item {
                LogoutButton(
                    onClick = { /* TODO: 로그아웃 로직 */ },
                    modifier = Modifier.padding(AppTheme.paddingLarge)
                )
            }
        }
    }
}

// --- 4. 공통 컴포넌트 (MyPageComponents.kt) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageAppBar(
    title: String,
    onNavigateToHome: () -> Unit,
    onBack: (() -> Unit)? = null // 뒤로가기 버튼 (선택 사항)
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = AppTheme.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            } else {
                IconButton(onClick = onNavigateToHome) {
                    // ⚠️ 'logo_image'를 res/drawable에 추가한 리소스 이름으로 변경하세요.
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.drawable.image7hours)
                            .crossfade(true)
                            .build(),
                        contentDescription = "7hours",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        actions = {
            // 헤더 중앙 정렬을 위한 빈 공간
            if (onBack == null) {
                Spacer(modifier = Modifier.width(48.dp)) // IconButton과 동일한 너비
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.header
        )
    )
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    imageUrl: String,
    onNavigateToProfile: () -> Unit
) {
    Surface(color = AppTheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(AppTheme.avatarSize)
                        .clip(CircleShape)
                        .border(4.dp, AppTheme.surface, CircleShape),
                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder 이미지 추가
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(AppTheme.accentGreen, CircleShape)
                        .border(4.dp, AppTheme.surface, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.textPrimary
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.textSecondary
                )
            }
            IconButton(onClick = onNavigateToProfile) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "프로필 수정",
                    tint = AppTheme.iconTint
                )
            }
        }
    }
}

@Composable
fun StatsSection(stats: List<StatItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.paddingLarge)
    ) {
        Text(
            text = "내 산책 기록",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.textPrimary,
            modifier = Modifier.padding(bottom = AppTheme.paddingMedium)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall)
        ) {
            stats.forEach { stat ->
                StatCard(
                    item = stat,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = AppTheme.shapeCard,
        colors = CardDefaults.cardColors(containerColor = AppTheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(AppTheme.paddingMedium)) {
            Box(
                modifier = Modifier
                    .size(AppTheme.statIconBoxSize)
                    .clip(AppTheme.shapeButton)
                    .background(item.brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.paddingSmall))
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.textSecondary
            )
            Text(
                text = item.value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.textPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemRow(item: MenuItem, onClick: () -> Unit) {
    Surface(
        color = AppTheme.surface,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = AppTheme.paddingLarge, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgedBox(
                badge = {
                    item.badgeCount?.let {
                        Badge(
                            containerColor = AppTheme.accentRed,
                            contentColor = Color.White
                        ) {
                            Text(it.toString())
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = AppTheme.paddingSmall)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(AppTheme.shapeButton)
                        .background(AppTheme.accentPurpleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = AppTheme.onAccentPurpleContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(AppTheme.paddingMedium))
            Text(
                text = item.label,
                color = AppTheme.textPrimary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppTheme.iconTint
            )
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = AppTheme.shapeCard,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.surface,
            contentColor = AppTheme.accentRed
        ),
        border = BorderStroke(1.dp, AppTheme.accentRedBorder),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
            Text(
                text = "로그아웃",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = AppTheme.paddingSmall)
            )
        }
    }
}

// --- 5. 하위 페이지 (자리 표시자) ---
// React의 ProfileDetailPage, SchedulePage 등
// 실제 구현 시 별도 파일로 분리

@Composable
fun ProfileDetailPage(onBack: () -> Unit) {
    PlaceholderSubPage("프로필 상세", onBack)
}

@Composable
fun SchedulePage(onBack: () -> Unit) {
    PlaceholderSubPage("일정관리", onBack)
}

@Composable
fun SettingsPage(onBack: () -> Unit) {
    PlaceholderSubPage("설정", onBack)
}

@Composable
fun FavoritesPage(onBack: () -> Unit) {
    PlaceholderSubPage("즐겨찾기", onBack)
}

/**
 * 하위 페이지를 위한 임시 Composable
 */
@Composable
fun PlaceholderSubPage(title: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            MyPageAppBar(
                title = title,
                onNavigateToHome = { /* 사용 안 함 */ },
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("$title 페이지")
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun MyPageMainScreenPreview() {
    MyPageMainScreen(
        navController = rememberNavController(), // preview에서는 navigate 호출 금지
        onNavigateToHome = {}
    )
}