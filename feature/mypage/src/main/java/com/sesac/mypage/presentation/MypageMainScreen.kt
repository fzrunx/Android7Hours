package com.sesac.mypage.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sesac.common.component.CommonSegmentedButton

import com.sesac.common.R as cR

@Composable
fun MypageMainScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    ) {
    val tabselection = remember { mutableStateOf(0) }
    val space = dimensionResource(cR.dimen.default_space)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            CommonSegmentedButton(
                listOf("내정보" ,"관리", "즐겨찾기", "설정"),
                tabselection,
                verticalAlignment = Arrangement.Top
            )
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.Top
//            ) {

//                CustomButton(
//                    buttonLabels = listOf("관리", "즐겨찾기", "설정")
//                ) { label ->
//                    when (label) {
//                        "관리" -> { /* 관리 화면으로 이동 */ }
//                        "즐겨찾기" -> { /* 즐겨찾기 화면으로 이동 */ }
//                        "설정" -> { /* 설정 화면으로 이동 */ }
//                    }
//                }
//            }
//            Spacer(Modifier.height(space))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(space),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        text = "반려견 정보",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
//                    Spacer(Modifier.height(10.dp))
//                    Box(
//                        modifier = Modifier
//                            .size(90.dp)
//                            .background(Color.LightGray, shape = RoundedCornerShape(50.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "사진",
//                            color = Color.DarkGray
//                        )
//                    }
                    Image(
                        modifier = Modifier
                            .padding(top = space)
                            .background(Color.LightGray)
                            .size(100.dp),
                        painter = painterResource(cR.drawable.icons8_dog_50),
                        contentDescription = "댕댕이 사진"
                    )


//                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(top = space),
                        verticalArrangement = Arrangement.spacedBy(space/2) // 각 항목 사이 간격
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "이름: 댕댕이",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "생년월일: 2022-01-01",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "성별: 남아",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                }
            }
//            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(space),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "사용자 정보",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
//                    Spacer(Modifier.height(10.dp))
//                    Spacer(Modifier.height(10.dp))
//                    Box(
//                        modifier = Modifier
//                            .size(90.dp)
//                            .background(Color.LightGray, shape = RoundedCornerShape(50.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "사진",
//                            color = Color.DarkGray
//                        )
//                    }

                    Image(
                        modifier = Modifier
                            .padding(top = space)
                            .background(Color.LightGray)
                            .size(100.dp),
                        painter = painterResource(cR.drawable.icons8_dog_50),
                        contentDescription = "사진"
                    )

//                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(space),
                        verticalArrangement = Arrangement.spacedBy(space/2) // 각 항목 사이 간격
                    ) {
                        Text(
                            text = "이름: 사용자",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "생년월일: 1900-01-01",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CustomButton(buttonLabels: List<String>, modifier: Modifier = Modifier, onClick: (label: String) -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp) // 버튼 사이 간격
    ) {
        buttonLabels.forEach { label ->
            Button(
                onClick = { onClick(label) },
                modifier = modifier
                    .weight(1f)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MypageMainScreenPreview() {
    MypageMainScreen(
        navController = rememberNavController()
    )
}