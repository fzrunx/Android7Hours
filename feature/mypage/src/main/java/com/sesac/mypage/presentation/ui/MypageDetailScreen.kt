package com.sesac.mypage.presentation.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.Background
import com.sesac.common.ui.theme.Gray200
import com.sesac.common.ui.theme.Gray300
import com.sesac.common.ui.theme.Gray500
import com.sesac.common.ui.theme.LightBlue
import com.sesac.common.ui.theme.LightGreen
import com.sesac.common.ui.theme.LightPurple
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.Typography
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.CommonAuthUiState
import com.sesac.domain.model.Pet
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.mypage.presentation.MypageViewModel
import com.sesac.common.R as cR

@Composable
fun MypageDetailScreen(
    navController: NavController,
    viewModel: MypageViewModel = hiltViewModel(),
    uiState: CommonAuthUiState,
) {
    val pets by viewModel.userPets.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.id) {
        if (uiState.id != -1 && !uiState.token.isNullOrEmpty()) {
//            viewModel.getUserPets(uiState.id)
            viewModel.getAllUserPets(uiState.token!!)
            Log.d("TAG-MypageDetailScreen", "pets : $pets")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MypageDetailHeader(
                name = uiState.fullName ?: "-",
                // The image doesn't contain a description for the user, so I'm using a placeholder
                description = "반려견과 함께하는 즐거운 산책",
                imageUrl = null // No image URL in uiState
            )
        }

        item { Spacer(modifier = Modifier.height(paddingMedium)) }

        item {
            UserInfoSection(
                email = uiState.email ?: "-",
                phone = "-", // Not available in uiState
                address = "-" // Not available in uiState
            )
        }

        item { Spacer(modifier = Modifier.height(paddingLarge)) }

        item {
            Divider(color = Gray200, thickness = 8.dp)
        }

        item {
            PetInfoSection(
                pets = pets,
                uiState = uiState,
                onAddPetClicked = { navController.navigate(MypageNavigationRoute.AddPetScreen) }
            )
        }
    }
}

@Composable
fun MypageDetailHeader(name: String, description: String, imageUrl: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingLarge, start = paddingLarge, end = paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = imageUrl ?: cR.drawable.placeholder,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = cR.drawable.placeholder)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(Primary, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Change Picture",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(paddingMedium))
        Text(text = name, style = Typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(paddingSmall))
        Text(text = description, style = Typography.bodyMedium, color = Gray500)
    }
}

@Composable
fun UserInfoSection(email: String, phone: String, address: String) {
    Column(
        modifier = Modifier.padding(horizontal = paddingLarge),
        verticalArrangement = Arrangement.spacedBy(paddingMedium)
    ) {
        UserInfoRow(icon = Icons.Default.Email, label = "이메일", value = email, iconBgColor = LightPurple)
        UserInfoRow(icon = Icons.Default.Call, label = "전화번호", value = phone, iconBgColor = LightBlue)
        UserInfoRow(icon = Icons.Default.LocationOn, label = "주소", value = address, iconBgColor = LightGreen)
    }
}

@Composable
fun UserInfoRow(icon: ImageVector, label: String, value: String, iconBgColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(paddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = Primary)
            }
            Spacer(modifier = Modifier.width(paddingMedium))
            Column {
                Text(text = label, style = Typography.bodySmall, color = Gray500)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = value, style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PetInfoSection(
    pets: List<Pet>,
    uiState: CommonAuthUiState,
    onAddPetClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingLarge)
    ) {
        Text(text = "반려견 정보", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(paddingMedium))

        if (pets.isEmpty()) {
            EmptyPetView(onAddPetClicked = onAddPetClicked)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(paddingMedium)) {
                pets.forEach { pet ->
                    pet.takeIf { it.owner == uiState.email }?.let {
                        PetInfoCard(pet = it)
                    }
                }
                Spacer(modifier = Modifier.height(paddingMedium))
                Button(
                    onClick = onAddPetClicked,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0F2))
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pet", tint = Gray500)
                    Spacer(modifier = Modifier.width(paddingSmall))
                    Text(text = "반려견 추가", color = Gray500, modifier = Modifier.padding(vertical = paddingSmall))
                }
            }
        }
    }
}

@Composable
fun PetInfoCard(pet: Pet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(paddingLarge)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = cR.drawable.placeholder, // Pet model doesn't have an image URL
                    contentDescription = pet.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(paddingMedium))
                Column {
                    Text(text = pet.name, style = Typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = pet.breed, style = Typography.bodyMedium, color = Gray500)
                }
            }
            Spacer(modifier = Modifier.height(paddingMedium))
            Row(horizontalArrangement = Arrangement.spacedBy(paddingMedium)) {
                PetDetailChip(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Cake,
                    label = "생일",
                    value = pet.birthday,
                    bgColor = Color(0xFFFDF0F3)
                )
                PetDetailChip(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.FavoriteBorder,
                    label = "성별",
                    value = pet.gender,
                    bgColor = Color(0xFFEFF5FF)
                )
            }
        }
    }
}

@Composable
fun PetDetailChip(modifier: Modifier = Modifier, icon: ImageVector, label: String, value: String, bgColor: Color) {
    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Primary)
        Spacer(modifier = Modifier.width(paddingSmall))
        Column {
            Text(text = label, style = Typography.bodySmall, color = Gray500)
            Text(text = value, style = Typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
fun EmptyPetView(onAddPetClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = cR.drawable.placeholder), // Using a placeholder image
            contentDescription = "Empty Pet",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(paddingMedium))
        Text(
            text = "등록된 반려견이 없어요",
            style = Typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Gray500
        )
        Spacer(modifier = Modifier.height(paddingSmall))
        Text(
            text = "반려견을 추가하고 산책을 기록해보세요",
            fontSize = 14.sp,
            color = Gray300,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(paddingLarge))
        Button(
            onClick = onAddPetClicked,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pet", tint = White)
            Spacer(modifier = Modifier.width(paddingSmall))
            Text(text = "반려견 추가하기", color = White, modifier = Modifier.padding(vertical = paddingSmall))
        }
    }
}
