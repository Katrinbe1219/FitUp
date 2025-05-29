package com.example.fitup.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitup.R
import com.example.fitup.items.Achievement

@Composable
fun scrollAchievemnts(achievemnts: List<Achievement>,
                      status: String,
                      eliminateChild: (Achievement) -> Unit,
                      ){
    // you can not set guaranteed pagerCount
    // set int.MAX-VALUE
    // then working with page(index) -> items[page%items.size] - УМНО

    var pagerState = rememberPagerState (pageCount = { Int.MAX_VALUE })



    if (achievemnts.isNotEmpty()) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth().size(300.dp),
            state = pagerState,
            pageSpacing = 20.dp

        ) { page ->
            // achievemnets i will get from firebase, not from dataStore
            // create ElevatedCard which will represent achievement
            // card has box to set background
            // name of achievement, progress of steps
            // gotten coins / all coins
            // description
            val achiev: Achievement = achievemnts[page%achievemnts.size]
            AchievementPagerComponent(achiev, eliminateYourself = eliminateChild)

        }
    }
    else {
        Text(text =  "${status} достижений нет ",
            style = TextStyle(
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontSize = 16.sp
            ))
    }



}