package com.example.fitup.editing

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitup.body.BodyTension
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.hilt.EnergyViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.StorageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditBodyEnergy(
    dbViewModel: DatabaseViewModel = hiltViewModel(),
    localViewModel: LocalViewModel = hiltViewModel(),
    energyViewModel: EnergyViewModel = hiltViewModel()
){

    var armEnergy  =  remember { mutableStateOf("") }
    var legEnergy  =  remember { mutableStateOf("") }
    var bodyEnergy  =  remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        // showing character
        BodyTension()
        //button? to change perso

        HorizontalDivider(thickness = 15.dp, color = Color.Transparent)

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ){

            Button(
                onClick = {

                    if (! (armEnergy.value.isEmpty() && legEnergy.value.isEmpty() && bodyEnergy.value.isEmpty())){
                        val changing = mutableListOf<String>()
                        val changin_uri = mutableMapOf<String, String>()

                        if (armEnergy.value.isNotEmpty()) {
                            changing.add("right_arm")
                            changin_uri["right_arm"] = armEnergy.value
                            changin_uri["left_arm"] = armEnergy.value

                        }
                        if (legEnergy.value.isNotEmpty()) {

                            changin_uri["leg"] = legEnergy.value
                            changin_uri["left_leg"] = legEnergy.value
                        }

                        if (bodyEnergy.value.isNotEmpty()) {
                            changing.add("body")
                            changin_uri["body"] = bodyEnergy.value
                        }

                        scope.launch(Dispatchers.IO) {
                            dbViewModel.editEnergyLevel(changin_uri).collect{task ->

                            }

                        }
                        energyViewModel.updateStates(changin_uri, false)

                    }

                },
                modifier = Modifier.height(30.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        "готово",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                        )
                    )
                }


            }
        }


        HorizontalDivider(thickness = 15.dp, color = Color.Transparent)

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(text = "Изменение",
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                ))
            HorizontalDivider(thickness = 15.dp, color = Color.Transparent)

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {

                Box(
                    modifier = Modifier.width(125.dp)
                ){
                    Text(text = "Туловище: ",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            bodyEnergy.value = "easy"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    content = {}

                )

               // VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            bodyEnergy.value = "bad"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    content = {}

                )

               // VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            bodyEnergy.value = "smert"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surface,
                    content = {}

                )

            }

            HorizontalDivider(thickness = 15.dp, color = Color.Transparent)

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.width(130.dp)
                ){
                    Text(text = "Руки: ",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary))
                }

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            armEnergy.value = "easy"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    content = {}

                )

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            armEnergy.value = "bad"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    content = {},


                )

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            armEnergy.value = "smert"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surface,
                    content = {}

                )

            }

            HorizontalDivider(thickness = 15.dp, color = Color.Transparent)

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.width(130.dp)
                ){
                    Text(text = "Ноги: ",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary))
                }


                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            legEnergy.value = "easy"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    content = {}

                )

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            legEnergy.value = "bad"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    content = {}

                )

                //VerticalDivider(thickness = 10.dp)

                Surface (
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                        .clickable {
                            legEnergy.value = "smert"
                        },
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surface,
                    content = {}

                )

            }
        }
    }


}