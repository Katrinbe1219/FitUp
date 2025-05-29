package com.example.fitup.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitup.R
import kotlinx.coroutines.delay


@Composable
fun firstPage(
    nav: NavController
){

    var isVisibleProkach by remember { mutableStateOf(false) }
    var isVisibleSebya by remember { mutableStateOf(false) }
    var isVisibleIPersonaj  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisibleProkach = true
        delay(100)
        isVisibleSebya = true
        delay(100)
        isVisibleIPersonaj = true
    }


    // main column
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 180.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
                Box (modifier = Modifier.size(height = 120.dp, width =170.dp)){
                    Image(
                        painter = painterResource(R.drawable.colored_dumbbell),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                HorizontalDivider(thickness = 50.dp, color= Color.Transparent)

                //прокачай
                AnimatedVisibility(
                    visible = isVisibleProkach,
                    enter =  fadeIn() + slideInVertically (
                        initialOffsetY = {-it},
                        animationSpec =  tween(durationMillis = 800)
                    )
                ) {
                    Text("прокачай",
                        style = TextStyle(
                            fontSize = 46.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                //себя
                AnimatedVisibility(
                    visible = isVisibleSebya,
                    enter = fadeIn() + slideInHorizontally (
                        initialOffsetX = {-it},
                        animationSpec = tween(800)
                    )
                ) {
                    Text("СЕБЯ",

                        style = TextStyle(
                            fontSize = 48.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                HorizontalDivider(thickness = 10.dp, color = Color.Transparent)


                // и персонажа
                AnimatedVisibility(
                    visible = isVisibleIPersonaj,
                    enter = fadeIn() + slideInHorizontally (
                        initialOffsetX = {it},
                        animationSpec = tween(800)

                    )
                ) {
                    Row (horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ){
                        Text("и",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.primary
                            ))
                        Text("персонажа",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.primary
                            ))
                    }
                }


                HorizontalDivider(thickness = 50.dp, color = Color.Transparent)
                // buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Box(
                        modifier = Modifier.size(width = 210.dp, height = 40.dp)
                            .clickable { nav.navigate("logIn") }
                            .clip(RoundedCornerShape(8.dp))
                            .border(width = 2.dp, color = Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.wide_back),
                            contentDescription = "main_background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text("войти в аккаунт",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface //white
                            ))
                    }
                  Box(
                        modifier = Modifier.size(width = 210.dp, height = 40.dp)
                            .clickable { nav.navigate("register") }
                            .clip(RoundedCornerShape(8.dp))
                            .border(width = 2.dp, color = Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.wide_back),
                            contentDescription = "main_background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text("зарегистрироваться",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface //white
                            ))
                    }


                }





//            }
//        }
    }

}