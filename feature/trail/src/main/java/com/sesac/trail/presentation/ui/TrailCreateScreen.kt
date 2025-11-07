package com.sesac.trail.presentation.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import com.sesac.common.ui.theme.*
import com.sesac.trail.presentation.component.TagFlow

// --- 데이터 클래스 (onSave로 전달될 데이터) ---
data class WalkPathData(
    val name: String,
    val difficulty: String,
    val distance: String,
    val estimatedTime: String,
    val description: String,
    val tags: List<String>,
    val imageUri: String? // React의 uploadedImage (String URL)
)

// --- 메인 Composable ---

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TrailCreateScreen(
    onBack: () -> Unit,
    onSave: (WalkPathData) -> Unit
) {
    // --- State (React의 useState) ---
    var pathName by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("초급") }
    var distance by remember { mutableStateOf("") }
    var estimatedTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(listOf<String>()) }
    var uploadedImageUri by remember { mutableStateOf<String?>(null) }

    // --- Toast/Snackbar (React의 sonner) ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- Image Picker ---
    // React의 데모용 Unsplash URL 대신 실제 갤러리 런처 사용
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            uploadedImageUri = uri.toString()
            scope.launch { snackbarHostState.showSnackbar("이미지가 업로드되었습니다") }
        }
    }

    // --- Handlers (React의 handle...) ---
    val handleTagToggle: (String) -> Unit = { tag -> // ✅ 타입 명시 추가됨
        if (selectedTags.contains(tag)) {
            selectedTags = selectedTags - tag
        } else {
            if (selectedTags.size < 5) {
                selectedTags = selectedTags + tag
            } else {
                // ✅ snackbar는 suspend 함수라 launch로 감싸야 함
                scope.launch {
                    snackbarHostState.showSnackbar("태그는 최대 5개까지 선택 가능합니다")
                }
            }
        }
    }

    fun handleSave() {
        scope.launch {
            if (pathName.isBlank()) {
                snackbarHostState.showSnackbar("산책로 이름을 입력해주세요")
                return@launch
            }
            if (distance.isBlank()) {
                snackbarHostState.showSnackbar("거리를 입력해주세요")
                return@launch
            }
            if (estimatedTime.isBlank()) {
                snackbarHostState.showSnackbar("예상 소요 시간을 입력해주세요")
                return@launch
            }

            val newPathData = WalkPathData(
                name = pathName,
                difficulty = difficulty,
                distance = distance,
                estimatedTime = estimatedTime,
                description = description,
                tags = selectedTags,
                imageUri = uploadedImageUri
            )
            onSave(newPathData)
            snackbarHostState.showSnackbar("산책로가 등록되었습니다!")
        }
    }

// --- UI (React의 return) ---
    Scaffold { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingLarge),
            verticalArrangement = Arrangement.spacedBy(PaddingSection)
        ) {
            // 1. Image Upload
            ImageUploader(
                imageUri = uploadedImageUri,
                onUploadClick = {
                    imagePickerLauncher.launch("image/*")
                },
                onRemoveClick = { uploadedImageUri = null }
            )

            // 2. Path Name
            FormTextField(
                label = "산책로 이름",
                value = pathName,
                onValueChange = { pathName = it },
                placeholder = "예: 한강공원 벚꽃길",
                isRequired = true
            )

            // 3. Difficulty
            DifficultySelector(
                selectedLevel = difficulty,
                onLevelSelect = { difficulty = it }
            )

            // ✅ 4. Distance & Time (리팩터링: Row → DistanceTimeInputs)
            DistanceTimeInputs(
                distance = distance,
                onDistanceChange = { distance = it },
                time = estimatedTime,
                onTimeChange = { estimatedTime = it }
            )

            // 5. Description
            FormTextField(
                label = "산책로 소개",
                value = description,
                onValueChange = { description = it },
                placeholder = "이 산책로의 특징이나 추천 이유를 작성해주세요...",
                minLines = 4
            )

            // 6. Tags
            TagFlow(
                selectedTags = selectedTags,
                onTagToggle = { tag ->
                    selectedTags = if (selectedTags.contains(tag)) {
                        selectedTags - tag
                    } else if (selectedTags.size < 5) {
                        selectedTags + tag
                    } else selectedTags
                },
                editable = true
            )

            // 7. 하단 버튼 (그대로 유지)
            CreateBottomActions(
                onCancel = onBack,
                onSave = { handleSave() }
            )
        }
    }
}


// --- 공통 부분 컴포넌트화 ---

@Composable
fun CreateBottomActions(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NoteBox,
                    contentColor = GrayTabText
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("나중에 하기")
            }
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple600
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("등록하기")
            }
        }
    }
}

@Composable
fun ImageUploader(
    imageUri: String?,
    onUploadClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Column {
        Text("대표 사진", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(paddingMicro))

        if (imageUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp) // h-48
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Uploaded",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(paddingMicro)
                        .background(Red500, CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Remove image")
                }
            }
        } else {
            val dashedBorderColor = SheetHandle

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onUploadClick() }
                    .background(PrimaryPurpleLight.copy(alpha = 0.5f))
                    .drawBehind {
                        // 점선 테두리 그리기
                        val strokeWidth = 2.dp.toPx()
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        val shape = RoundedCornerShape(12.dp)
                        val outline = shape.createOutline(size, layoutDirection, this)

                        drawOutline(
                            outline = outline,
                            color = dashedBorderColor,
                            style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Upload, contentDescription = null, tint = GrayTabText)
                    Text("사진 업로드", color = GrayTabText)
                    Text("선택 사항", style = MaterialTheme.typography.bodySmall, color = SheetHandle)
                }
            }
        }
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    minLines: Int = 1
) {
    Column(modifier = modifier) { // ✅ 여기에 modifier 적용
        Row {
            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (isRequired) {
                Text(" *", color = Red500, style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(Modifier.height(paddingMicro))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            keyboardOptions = keyboardOptions,
            minLines = minLines,
            modifier = Modifier.fillMaxWidth(), // ✅ 내부는 fillMaxWidth 유지
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun DifficultySelector(
    selectedLevel: String,
    onLevelSelect: (String) -> Unit
) {
    val levels = listOf("초급", "중급", "고급")

    LabeledSection(label = "난이도", isRequired = true) {
        Row(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
            levels.forEach { level ->
                Button(
                    onClick = { onLevelSelect(level) },
                    colors = if (selectedLevel == level) {
                        ButtonDefaults.buttonColors(containerColor = Purple600)
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = NoteBox,
                            contentColor = GrayTabText
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(level)
                }
            }
        }
    }
}

// --- 공통 Wrapper (라벨 + 별표 + 내용)
@Composable
fun LabeledSection(
    label: String,
    isRequired: Boolean = false,
    content: @Composable () -> Unit
) {
    Column {
        Row {
            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (isRequired) {
                Text(" *", color = Red500, style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(Modifier.height(paddingMicro))
        content()
    }
}

// --- 거리 / 시간 입력 묶음
@Composable
fun DistanceTimeInputs(
    distance: String,
    onDistanceChange: (String) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
        FormTextField(
            label = "거리 (km)",
            value = distance,
            onValueChange = onDistanceChange,
            placeholder = "1.5",
            isRequired = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(0.dp).weight(1f, fill = true)
        )
        FormTextField(
            label = "예상 시간 (분)",
            value = time,
            onValueChange = onTimeChange,
            placeholder = "25",
            isRequired = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(0.dp).weight(1f, fill = true)
        )
    }
}

// --- Preview ---

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun WalkPathCreatePagePreview() {
    MaterialTheme {
        TrailCreateScreen(
            onBack = {},
            onSave = { _: WalkPathData -> } // ✅ 타입 명시
        )
    }
}