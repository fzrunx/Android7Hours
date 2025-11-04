package com.sesac.mypage.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.tooling.preview.Preview
import com.sesac.common.navigation.CommonHeader
import com.sesac.common.ui.theme.*

// --- 데이터 클래스 ---
data class ScheduleItem(
    val id: Long,
    val date: LocalDate,
    val title: String,
    val memo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageManageScreen(onBack: () -> Unit) {
    // --- 상태 관리 ---
    val context = LocalContext.current

    // 1. 선택된 날짜 (LocalDate 사용)
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    // 2. 일정 추가 다이얼로그
    var isAddDialogOpen by remember { mutableStateOf(false) }
    // 3. 다이얼로그 내 입력 값
    var scheduleTitle by remember { mutableStateOf("") }
    var scheduleMemo by remember { mutableStateOf("") }
    // 4. 전체 일정 목록 (샘플 데이터 포함)
    val schedules = remember {
        mutableStateListOf(
            ScheduleItem(
                id = 1L,
                date = LocalDate.now(),
                title = "초코 백신접종",
                memo = "종합 백신 접종 - 오전 10시 동물병원"
            ),
            ScheduleItem(
                id = 2L,
                date = LocalDate.now(),
                title = "복순이 건강검진",
                memo = "정기 건강검진 및 심장사상충 예방약 처방 - 오후 2시"
            )
        )
    }
    // 5. 선택된 날짜의 일정 목록 (Derived State)
    val todaySchedules = remember(selectedDate, schedules.size) {
        schedules.filter { it.date == selectedDate }
    }

    Scaffold(
        topBar = {
            CommonHeader(
                title = "일정관리",
                onBack = { /* 뒤로가기 */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddDialogOpen = true },
                containerColor = PrimaryPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "일정 추가")
            }
        },
        containerColor = Color(0xFFF9FAFB) // bg-gray-50
    ) { paddingValues ->

        ScheduleContent(
            modifier = Modifier.padding(paddingValues),
            selectedDate = selectedDate,
            schedules = todaySchedules,
            onDateSelected = { newDate -> selectedDate = newDate },
            onAddClick = { isAddDialogOpen = true },
            onDeleteClick = { id -> schedules.removeAll { it.id == id } }
        )
    }
    // --- 다이얼로그 ---
    if (isAddDialogOpen) {
        AddScheduleDialog(
            selectedDate = selectedDate,
            title = scheduleTitle,
            memo = scheduleMemo,
            onTitleChange = { scheduleTitle = it },
            onMemoChange = { scheduleMemo = it },
            onDismiss = { isAddDialogOpen = false },
            onConfirm = {
                if (scheduleTitle.isNotBlank()) {
                    schedules.add(
                        ScheduleItem(
                            id = System.currentTimeMillis(),
                            date = selectedDate,
                            title = scheduleTitle,
                            memo = scheduleMemo
                        )
                    )
                    scheduleTitle = ""
                    scheduleMemo = ""
                    isAddDialogOpen = false
                    Toast.makeText(context, "일정이 추가되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    schedules: List<ScheduleItem>,
    onDateSelected: (LocalDate) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = paddingMedium),
        contentPadding = PaddingValues(bottom = 80.dp) // FAB와 겹치지 않도록
    ) {
        // 캘린더
        item {
            ScheduleCalendar(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
            Spacer(Modifier.height(paddingLarge))
        }

        // 일정 헤더 + EmptyState 통합 섹션
        item {
            ScheduleListSection(
                selectedDate = selectedDate,
                schedules = schedules,
                onAddClick = onAddClick
            )
            Spacer(Modifier.height(paddingMedium))
        }

        // 일정 카드 리스트
        if (schedules.isNotEmpty()) {
            items(schedules, key = { it.id }) { schedule ->
                ScheduleItemCard(
                    schedule = schedule,
                    onDeleteClick = { onDeleteClick(schedule.id) }
                )
                Spacer(Modifier.height(paddingSmall))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Material 3 DatePicker 상태 관리
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    // DatePicker의 선택이 변경되면, 상위 Composable의 selectedDate 상태를 업데이트
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val newDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            if (newDate != selectedDate) {
                onDateSelected(newDate)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(paddingMedium),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            modifier = Modifier.padding(paddingSmall)
        )
    }
}

@Composable
private fun ScheduleListSection(
    selectedDate: LocalDate,
    schedules: List<ScheduleItem>,
    onAddClick: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("M월 d일") }

    if (schedules.isEmpty()) {
        // --- 일정이 없을 때 ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = paddingLarge),
            shape = RoundedCornerShape(paddingMedium),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(paddingLarge * 2)
            ) {
                Text(
                    text = "${selectedDate.format(formatter)} 일정이 없습니다",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(paddingMedium))
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("일정 추가하기")
                }
            }
        }
    } else {
        // --- 일정이 있을 때 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${selectedDate.format(formatter)}의 일정",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .background(PrimaryPurpleLight, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${schedules.size}개",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ScheduleItemCard(
    schedule: ScheduleItem,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(paddingMedium),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(paddingMedium),
            verticalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = schedule.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(24.dp) // 터치 영역 확보
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = Red500
                    )
                }
            }

            if (schedule.memo.isNotBlank()) {
                Text(
                    text = schedule.memo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(paddingSmall)
                        )
                        .padding(paddingMedium)
                )
            }
        }
    }
}

@Composable
private fun AddScheduleDialog(
    selectedDate: LocalDate,
    title: String,
    memo: String,
    onTitleChange: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("일정 추가") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(paddingMedium)) {
                Text("선택한 날짜에 새로운 일정을 추가하세요.")
                // 날짜 표시
                Text(
                    text = selectedDate.format(formatter),
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryPurple,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryPurpleLight, RoundedCornerShape(paddingSmall))
                        .padding(paddingMedium)
                )
                // 일정 제목
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("일정 제목") },
                    placeholder = { Text("일정 제목을 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // 메모
                OutlinedTextField(
                    value = memo,
                    onValueChange = onMemoChange,
                    label = { Text("메모") },
                    placeholder = { Text("메모를 입력하세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MyPageManageScreenPreview() {
    MypageManageScreen (onBack = {})
}