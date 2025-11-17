package com.sesac.mypage.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.Typography
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.model.Pet
import com.sesac.mypage.presentation.MypageViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    navController: NavController,
    viewModel: MypageViewModel = hiltViewModel(),
    uiState: AuthUiState,
) {
    val context = LocalContext.current
    val breeds by viewModel.breeds.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("남아") }
    var birthday by remember { mutableStateOf("날짜를 선택해주세요") }
    var isNeutered by remember { mutableStateOf(false) }
    var selectedBreed by remember { mutableStateOf("") }
    var isBreedDropdownExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getBreeds()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(paddingLarge)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "반려견 정보 입력", style = Typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(paddingLarge))

            // Name
            AddPetFormItem(label = "이름") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("이름을 입력해주세요") }
                )
            }

            // Gender
            AddPetFormItem(label = "성별") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    listOf("남아", "여아").forEach { gender ->
                        RadioButton(
                            selected = selectedGender == gender,
                            onClick = { selectedGender = gender },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Text(text = gender, modifier = Modifier.padding(start = paddingSmall))
                        Spacer(modifier = Modifier.width(paddingMedium))
                    }
                }
            }

            // Birthday
            AddPetFormItem(label = "생일") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                        .padding(vertical = paddingMedium)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Birthday")
                        Spacer(modifier = Modifier.width(paddingSmall))
                        Text(text = birthday)
                    }
                }
            }

            // Neutered
            AddPetFormItem(label = "중성화") {
                Switch(
                    checked = isNeutered,
                    onCheckedChange = { isNeutered = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Primary)
                )
            }

            // Breed
            AddPetFormItem(label = "품종") {
                ExposedDropdownMenuBox(
                    expanded = isBreedDropdownExpanded,
                    onExpandedChange = { isBreedDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedBreed,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isBreedDropdownExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        placeholder = { Text("품종을 선택해주세요") }
                    )
                    ExposedDropdownMenu(
                        expanded = isBreedDropdownExpanded,
                        onDismissRequest = { isBreedDropdownExpanded = false }
                    ) {
                        breeds.forEach { breed ->
                            DropdownMenuItem(
                                text = { Text(breed.breedName) },
                                onClick = {
                                    selectedBreed = breed.breedName
                                    isBreedDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(paddingLarge))

        // Save Button
        Button(
            onClick = {
                if (name.isBlank() || birthday == "날짜를 선택해주세요" || selectedBreed.isBlank()) {
                    Toast.makeText(context, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val genderValue = if (selectedGender == "남아") "M" else "F"
                    val newPet = Pet(
                        id = 0, // ID is usually generated by the server
                        name = name,
                        gender = genderValue,
                        birthday = birthday,
                        neutering = isNeutered,
                        breed = selectedBreed,
                        owner = uiState.id.toString()
                    )
                    uiState.token?.let {
                        viewModel.addPet("Bearer $it", newPet) { success ->
                            if (success) {
                                Toast.makeText(context, "반려견이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text(text = "저장", color = White)
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        birthday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun AddPetFormItem(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = paddingMedium)) {
        Text(text = label, style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(paddingSmall))
        content()
    }
}