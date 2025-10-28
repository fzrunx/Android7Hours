package com.sesac.mypage.presentation

import android.widget.Space
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.component.CommonSegmentedButton

import com.sesac.common.R as cR

@Composable
fun MypageSettingScreen (modifier: Modifier = Modifier) {
    val space = dimensionResource(cR.dimen.default_space)
    val tabSelectedList = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(space),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CommonSegmentedButton(
                tabOptions = listOf("관리", "즐겨찾기", "설정"),
                tabSelectedIndex = tabSelectedList,
            )

            SettingOptionItem(
                title = "알림 설정",
                optionLists = listOf("강아지 GPS 알림 설정", "일정 알림 설정"))

            Spacer(Modifier.height(space))

            SettingOptionItem(
                title = "권한 설정",
                optionLists = listOf("카메라 권한 설정", "GPS 권한 설정")
            )

        }
        Text(
            text = "버전 정보 v1.0.0",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOptionItem(
    title: String,
    optionLists: List<String>,
    space: Dp = dimensionResource(cR.dimen.default_space),
    fontSize: TextUnit = 16.sp,
) {
//    val subSpace = (space.value*1.5).dp
    val subSpace = space

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = subSpace, top = space),
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )

            optionLists.forEach { optionList ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = subSpace)
                            .wrapContentHeight(Alignment.CenterVertically),
                        text = optionList,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        modifier = Modifier.padding(horizontal = space),
                        checked = true,
                        onCheckedChange = {} // 현재 클릭해도 아무 동작 안 함
                    )
                }
            }

        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MypageSettingScreenPreview() {
    MypageSettingScreen()
}