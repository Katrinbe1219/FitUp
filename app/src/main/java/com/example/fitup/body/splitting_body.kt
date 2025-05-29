package com.example.fitup.body

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.SplittingBodyViewModel
import com.example.fitup.hilt.StorageViewModel
import com.example.fitup.items.Dificulty
import com.example.fitup.items.Exercsice
import com.example.fitup.items.MedicalGrounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


//@Preview(showBackground = true)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SplitABodyAnimation(
    updateListExercices: (List<Exercsice>, String) -> Unit,
    storageViewModel: StorageViewModel = hiltViewModel(),
    localViewModel: LocalViewModel = hiltViewModel(),
    splittingBodyViewModel: SplittingBodyViewModel = hiltViewModel()
    ){

    var isSplit by remember { mutableStateOf(false) }
    var rightArm by remember { mutableStateOf<Uri?>(null) }
    var leftArm by remember { mutableStateOf<Uri?>(null) }
    var head by remember { mutableStateOf<Uri?>(null) }
    var body by remember { mutableStateOf<Uri?>(null) }
    var leg by remember { mutableStateOf<Uri?>(null) }

    val allImagesReady by splittingBodyViewModel.allImagesReady.collectAsState()


    val headOffsets by animateDpAsState(
        targetValue = if(isSplit) -10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val leftArmOffsets by animateDpAsState(
        targetValue =  if(isSplit) -10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )
    val rightArmOffsets by animateDpAsState(
        targetValue =  if(isSplit) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val legsYOffsets by animateDpAsState(
        targetValue =  if(isSplit) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val rightLegXOffsets by animateDpAsState(
        targetValue =  if(isSplit) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val leftLegXOffsets by animateDpAsState(
        targetValue =  if(isSplit) -10.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    var bodyImgSize by remember{ mutableStateOf(Size.Zero) };

    var backgroundPic by remember { mutableStateOf<Uri?>(null) };



    LaunchedEffect(Unit) {


        withContext(Dispatchers.IO){
            val level = localViewModel.getBodyLevel()

            val resources = if (level != null ) storageViewModel.getImages(level) else null
            val background = storageViewModel.getBackground()

            withContext(Dispatchers.Main) {

                if ( resources != null && resources.size == 5 ){
                        backgroundPic = background

                    splittingBodyViewModel.addImagesToControl(resources)

                    resources.forEach{ key, value ->
                            //Log.d("KATRIN_BE"," hererere  key ${key} and vlaue ${value}" )
                            if (key == "right_arm" ) {  rightArm = value }
                            else if ( key == "left_arm" ) { leftArm = value }
                            else if ( key == "body" ) { body = value }
                            else if ( key == "head" ) { head = value }
                            else if (key == "leg" ) {leg = value}

                    }


                }
//                isReady = true

            }

//            delay(2000L)
//            isSplit = true



        }
    }

        Box (
            modifier = Modifier.wrapContentSize()
        ){
            Column (modifier = Modifier.wrapContentSize()
                .padding(vertical = 10.dp, horizontal = 20.dp)
//            .consumeWindowInsets(innerPadding) // disable innerPadding
            ){

                ElevatedCard (
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    modifier = Modifier.size( 400.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ){

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        if (backgroundPic != null ){

                            AsyncImage(
                                model = backgroundPic,
                                contentDescription = "background",
                                modifier = Modifier.fillMaxSize(),

                                contentScale = ContentScale.Crop // Crop the image to fit the card
                            )

                        }

                        Column (
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            MonitoredAsyncImage(
                                id = "head",
                                imageUrl = head,
                                modifier = Modifier
                                    //.offset(y = headOffsets)

                                    .clickable {
                                        updateListExercices(listOf(), "ГОЛОВУ")

                                    }
                            )

//                            )




                            Row (modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Center){

                                MonitoredAsyncImage(
                                    imageUrl = leftArm,
                                    id = "left_arm",
                                    modifier = Modifier.offset(x = leftArmOffsets)
                                    ,
                                )

                                MonitoredAsyncImage (
                                    imageUrl= body,
                                    id = "body",
                                    modifier = Modifier.onGloballyPositioned { coordinates ->
                                        bodyImgSize = coordinates.size.toSize()
                                        splittingBodyViewModel.updateState("body", true)
                                    }.clickable {
                                        updateListExercices(listOf(Exercsice("Наклоны", Dificulty.BEGGINER, MedicalGrounds.NONE),
                                            Exercsice("Круговые движения", Dificulty.BEGGINER, MedicalGrounds.NONE)), "туловище")
                                    },
                                )

                                MonitoredAsyncImage(
                                    imageUrl = rightArm,
                                    id = "right_arm",
                                    modifier = Modifier.offset(x = rightArmOffsets)
                                    ,
                                )



                            }

                            Row (modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){

                                MonitoredAsyncImage(
                                    id = "left_leg",
                                    imageUrl = leg,
                                    modifier = Modifier.offset(y = legsYOffsets, x = leftLegXOffsets)

                                )




                                MonitoredAsyncImage(
                                    id = "leg",
                                    modifier =   Modifier.offset(y = legsYOffsets, x = rightLegXOffsets)
                                    ,
                                    imageUrl = leg)


                            }



                        }
                    }
                }

            }

            if (!allImagesReady){

                Box(
                    modifier = Modifier.matchParentSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.width(120.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }


            }
        }




}









