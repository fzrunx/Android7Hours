package com.sesac.monitor.presentation.ui

import com.sesac.common.R as cR
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sesac.common.component.IconRowButtons
import com.sesac.monitor.presentation.componoent.MonitorTempTabButton


@Composable
fun MonitorCamScreen(
    modifier: Modifier = Modifier,
    tabOptionsName: List<String> = listOf(stringResource(cR.string.monitor_button_webcam), stringResource(cR.string.monitor_button_GPS)),
    ) {
    val buttonLists = listOf(painterResource(cR.drawable.icons8_palay_button), painterResource(cR.drawable.icons8_stop_button))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonitorTempTabButton(
            buttonLabels = tabOptionsName
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .aspectRatio(1 / 1f)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center

        ){
            Text(
                text = "영상 영역",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

        IconRowButtons(modifier = Modifier, buttonLists = buttonLists)

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonitorCamScreenPreview() {
        MonitorCamScreen()
}

