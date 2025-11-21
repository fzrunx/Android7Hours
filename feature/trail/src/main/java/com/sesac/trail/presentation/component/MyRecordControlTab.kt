package com.sesac.trail.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.Gray200
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.PrimaryGreenDark
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.Red500
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.UserPath
import com.sesac.trail.presentation.TrailViewModel


@Composable
fun MyRecordsTabContent(
    viewModel: TrailViewModel,
    myPaths: List<UserPath>,
    isEditMode: Boolean,
    onPathClick: (UserPath) -> Unit,
    onFollowClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onEditModeToggle: () -> Unit,
//    onModifyClick: (UserPath) -> Unit,
    onDeleteClick: (Int) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onEditModeToggle) {
                Text(if (isEditMode) "완료" else "편집")
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(vertical = paddingSmall),
            verticalArrangement = Arrangement.spacedBy(paddingMicro)
        ) {
            items(myPaths) { myPath ->
                MyRecordItem(
                    viewModel = viewModel,
                    myPath = myPath,
                    isEditMode = isEditMode,
                    onPathClick = onPathClick,
                    onFollowClick = onFollowClick,
                    onRegisterClick = onRegisterClick,
//                    onModifyClick = onModifyClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
fun MyRecordItem(
    viewModel: TrailViewModel,
    myPath: UserPath,
    isEditMode: Boolean,
    onPathClick: (UserPath) -> Unit,
    onFollowClick: () -> Unit,
    onRegisterClick: () -> Unit,
//    onModifyClick: (UserPath) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.clickable { onPathClick(myPath) },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO : Box -> 이미지 썸내일 수정
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Primary, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Pets, contentDescription = "Walk", tint = White)
            }
            Spacer(Modifier.width(paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(myPath.name, fontWeight = FontWeight.Bold)
                    Text(text = myPath.difiiculty.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Text("${myPath.distance} · ${myPath.time}", fontSize = 14.sp)
                Row {
//                    Text("👣 ${myPath.steps.toLocaleString()}", fontSize = 12.sp)
//                    Text(" • ", fontSize = 12.sp)
//                    Text("🔥 ${myPath.calories}kcal", fontSize = 12.sp)
                }
            }
            Spacer(Modifier.width(paddingSmall))
            if (isEditMode) {
                Column(verticalArrangement = Arrangement.spacedBy(paddingMicro)) {
//                    Button(
//                        onClick = { onModifyClick(myPath) },
//                        colors = ButtonDefaults.buttonColors(containerColor = Purple600),
//                        contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
//                    ) { Text("수정") }
                    IconButton(
                        onClick = { onDeleteClick(myPath.id) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "삭제",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

//                    Button(
//                        onClick = { onDeleteClick(myPath.id) },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                        contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
//                    ) { Text("삭제") }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(paddingMicro)) {
                    Button(
                        onClick = onFollowClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Purple600),
                        contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
                    ) { Text("따라가기", color = White) }
                    Button(
                        onClick = {
                            viewModel.updateSelectedPath(myPath)
                            Log.d("Tag-MyRecordControlTab", "record = $myPath")
                            onRegisterClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreenDark),
                        contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
                    ) { Text("등록하기", color = White) }
                }
            }
        }
    }
}

// LocaleString 포맷 헬퍼
private fun Int.toLocaleString(): String {
    return "%,d".format(this)
}