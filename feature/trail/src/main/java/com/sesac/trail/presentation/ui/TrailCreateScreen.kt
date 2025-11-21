package com.sesac.trail.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.NoteBox
import com.sesac.common.ui.theme.PaddingSection
import com.sesac.common.ui.theme.PrimaryPurpleLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.Red500
import com.sesac.common.ui.theme.SheetHandle
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingNone
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Coord
import com.sesac.domain.model.UiEvent
import com.sesac.domain.result.AuthUiState
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.TagFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// --- 데이터 클래스 ---
data class WalkPathData(
    val name: String,
    val difficulty: String,
    val distance: String,
    val estimatedTime: String,
    val description: String,
    val tags: List<String>,
    val imageUri: String?
)

data class ValidationState(
    val isNameInvalid: Boolean = false,
    val isDistanceInvalid: Boolean = false,
    val isTimeInvalid: Boolean = false
)

// --- 메인 Composable ---

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TrailCreateScreen(
    viewModel: TrailViewModel = hiltViewModel(),
    navController: NavController,
    uiState: AuthUiState,
) {
    val context = LocalContext.current
    val path by viewModel.selectedPath.collectAsStateWithLifecycle()
    Log.d("Tag-TrailCreateScreen", "selectedCreatePath = $path")

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var validationState by remember { mutableStateOf(ValidationState()) }

    LaunchedEffect(Unit) {
        viewModel.invalidToken.collectLatest { event ->
            if (event is UiEvent.ToastEvent) Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        if (path == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        var uploadedImageUri by remember { mutableStateOf<String?>(null) }
        var distanceString by remember { mutableStateOf(path?.distance?.takeIf { it > 0f }?.toString() ?: "") }
        var timeString by remember { mutableStateOf(path?.time?.takeIf { it > 0 }?.toString() ?: "") }

        LaunchedEffect(path) {
            distanceString = path?.distance?.takeIf { it > 0f }?.toString() ?: ""
            timeString = path?.time?.takeIf { it > 0 }?.toString() ?: ""
        }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                uploadedImageUri = uri.toString()
                scope.launch { snackbarHostState.showSnackbar("이미지가 업로드되었습니다") }
            }
        }

        val handleTagToggle: (String) -> Unit = { tag ->
            path?.let {
                val newTags = if (it.tags.contains(tag)) {
                    it.tags - tag
                } else {
                    if (it.tags.size < 5) {
                        it.tags + tag
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("태그는 최대 5개까지 선택 가능합니다")
                        }
                        it.tags
                    }
                }
                viewModel.updateSelectedPath(it.copy(tags = newTags))
            }
        }

        fun handleSave() {
            scope.launch {
                path?.let {
                    val isNameInvalid = it.name.isBlank()
                    val isDistanceInvalid = it.distance <= 0f
                    val isTimeInvalid = it.time <= 0

                    validationState = ValidationState(
                        isNameInvalid = isNameInvalid,
                        isDistanceInvalid = isDistanceInvalid,
                        isTimeInvalid = isTimeInvalid
                    )

                    if (isNameInvalid || isDistanceInvalid || isTimeInvalid) {
                        return@launch
                    }

                    // 서버 저장이 아닌 RoomDB에 임시 저장
                    viewModel.saveDraft(it)
                    snackbarHostState.showSnackbar("산책로가 임시 저장되었습니다.")

                    navController.popBackStack()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(paddingLarge),
            verticalArrangement = Arrangement.spacedBy(PaddingSection)
        ) {
            ImageUploader(
                imageUri = uploadedImageUri,
                onUploadClick = { imagePickerLauncher.launch("image/*") },
                onRemoveClick = { uploadedImageUri = null }
            )

            FormTextField(
                label = "산책로 이름",
                value = path?.name ?: "",
                onValueChange = { newValue -> path?.let { viewModel.updateSelectedPath(it.copy(name = newValue)) } },
                placeholder = "예: 한강공원 벚꽃길",
                isRequired = true,
                isError = validationState.isNameInvalid
            )

            DifficultySelector(
                selectedLevel = path?.difiiculty ?: "초급",
                onLevelSelect = { newDifficulty -> path?.let { viewModel.updateSelectedPath(it.copy(difiiculty = newDifficulty)) } }
            )

            DistanceTimeInputs(
                distance = distanceString,
                onDistanceChange = { newString ->
                    if (newString.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        distanceString = newString
                        path?.let {
                            viewModel.updateSelectedPath(it.copy(distance = newString.toFloatOrNull() ?: 0f))
                        }
                    }
                },
                isDistanceError = validationState.isDistanceInvalid,
                time = timeString,
                onTimeChange = { newString ->
                    if (newString.matches(Regex("^\\d*\$"))) {
                        timeString = newString
                        path?.let {
                            viewModel.updateSelectedPath(it.copy(time = newString.toIntOrNull() ?: 0))
                        }
                    }
                },
                isTimeError = validationState.isTimeInvalid
            )

            FormTextField(
                label = "산책로 소개",
                value = path?.description ?: "",
                onValueChange = { newDescription -> path?.let { viewModel.updateSelectedPath(it.copy(description = newDescription)) } },
                placeholder = "이 산책로의 특징이나 추천 이유를 작성해주세요...",
                minLines = 4
            )

            TagFlow(
                selectedTags = path?.tags ?: emptyList(),
                onTagToggle = handleTagToggle,
                editable = true
            )

            CreateBottomActions(
                onCancel = {
                    scope.launch {
                        viewModel.saveDraftIfNotEmpty()
                        viewModel.clearSelectedPath()
                        navController.popBackStack()
                    }
                },
                onSave = {
                    handleSave()
                },
                isEditing = path?.id != -1
            )
        }
    }
}


// --- 공통 부분 컴포넌트화 ---

@Composable
fun CreateBottomActions(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isEditing: Boolean
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
                Text(if (isEditing) "수정하기" else "등록하기")
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
    isError: Boolean = false,
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
            shape = MaterialTheme.shapes.medium,
            isError = isError
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
    isDistanceError: Boolean,
    time: String,
    onTimeChange: (String) -> Unit,
    isTimeError: Boolean
) {
    Row(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
        FormTextField(
            label = "거리 (km)",
            value = distance,
            onValueChange = onDistanceChange,
            placeholder = "1.5",
            isRequired = true,
            isError = isDistanceError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(paddingNone).weight(1f, fill = true)
        )
        FormTextField(
            label = "예상 시간 (분)",
            value = time,
            onValueChange = onTimeChange,
            placeholder = "25",
            isRequired = true,
            isError = isTimeError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(paddingNone).weight(1f, fill = true)
        )
    }
}

// --- Preview ---

//@Preview(showBackground = true, device = "id:pixel_5")
//@Composable
//fun WalkPathCreatePagePreview() {
//    MaterialTheme {
//        TrailCreateScreen(
//            navController = rememberNavController(),
////            onSave = { }, // ✅ 타입 명시
//        )
//    }
//}