package com.example.fitup.Components

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitup.R
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.items.Achievement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AchievementPagerComponent(
    achiev: Achievement,
    //imageUrl: Uri,
    dbViewModel: DatabaseViewModel = hiltViewModel(),
    eliminateYourself : (Achievement) -> Unit,
){
    var showMenu by remember { mutableStateOf(false) }
    var imageWidth by remember { mutableStateOf(0) }
    var isClicked by remember { mutableStateOf(false) }

    LaunchedEffect(isClicked) {
        if (isClicked){
            val status = if (achiev.status == "current") "old" else "current"
            dbViewModel.changeAchievementStatus(achiev.name!!,status ).collect{ task ->
                if (task) {
                    withContext(Dispatchers.Main){
                        eliminateYourself(achiev)
                    }

                }
            }


            isClicked = false
        }

    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().height(250.dp).width(290.dp),

        elevation = CardDefaults.elevatedCardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)

    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text=achiev.name!!,
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                    )
                )
                Box(
                    modifier = Modifier.wrapContentSize()
                ){
                    Image(
                        painter = painterResource(R.drawable.big_dumbbel),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            showMenu = true
                        }
                            .onSizeChanged { imageWidth = it.width }

                    )
                    if (showMenu) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {showMenu = false},

                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    isClicked = true
                                },
                                text = {Text(
                                    text = if (achiev.status == "current") "на упокой" else "вернуть"
                                )}
                            )
                        }
                    }
                }

            }


            HorizontalDivider(thickness = 5.dp, color = Color.Transparent)

            HorizontalDivider(thickness = 2.dp, color = Color.Black)
            HorizontalDivider(thickness = 5.dp, color = Color.Transparent)

            Text(text = achiev.description!!)
            HorizontalDivider(thickness = 5.dp, color = Color.Transparent)

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = achiev.cur_step.toString() + "/" + achiev.all_steps.toString() + " шагов")

                Row {
                    Text(text = achiev.earned_coins.toString() + "/" + achiev.all_coins.toString() + "")
                    // image of coins
                }


            }
        }

    }
}