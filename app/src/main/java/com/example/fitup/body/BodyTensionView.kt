package com.example.fitup.body


import android.icu.util.LocaleData
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fitup.dataStore.UserLocalData
import com.example.fitup.hilt.EnergyViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.StorageViewModel
import com.example.fitup.items.Dificulty
import com.example.fitup.items.Exercsice
import com.example.fitup.items.MedicalGrounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate

// получаем your tension level and fitTYpe

@Composable
fun BodyTension(
    storageViewModel: StorageViewModel = hiltViewModel(),
    localViewModel: LocalViewModel = hiltViewModel(),
    energyViewModel: EnergyViewModel = hiltViewModel()
){

    val allImagesReady by energyViewModel.allImagesReady.collectAsState()



    var legPath by remember { mutableStateOf<Uri?>(null) }
    var armRightPath by remember { mutableStateOf<Uri?>(null)  }
    var armLeftPath by remember { mutableStateOf<Uri?>(null)  }
    var headPath by remember{ mutableStateOf<Uri?>(null)  }
    var bodyPath by remember { mutableStateOf<Uri?>(null)  }

    var backroundPic by remember {mutableStateOf<Uri?>(null)}
    var bodyImgSize by remember{ mutableStateOf(Size.Zero) };

    LaunchedEffect(Unit) {

        withContext(Dispatchers.IO) {
            val background = storageViewModel.getBackground()

            val getting_energy = localViewModel.getAllEnergies()
            val level_ph = localViewModel.getBodyLevel()
            if (getting_energy.size == 4 && level_ph!= null){
                val arm_energy =  if (getting_energy["right_arm"] != "null") storageViewModel.getArmEnergyPictures(getting_energy["right_arm"]!!, level_ph)
                                  else  storageViewModel.getArmEnergyPictures("easy", level_ph)


                val leg_energy = if (getting_energy["leg"] != "null") storageViewModel.getLegEnergyPictures(getting_energy["leg"]!!, level_ph)
                                else storageViewModel.getLegEnergyPictures("easy", level_ph)

                val body_energy = if (getting_energy["body"] != "null") storageViewModel.getBodyEnergyPictures(getting_energy["body"]!!, level_ph)
                                    else storageViewModel.getBodyEnergyPictures("easy", level_ph)
                val head_energy = storageViewModel.getHeadEnergyPictures()


                withContext(Dispatchers.Main){
                    armRightPath = arm_energy["right_arm"]
                    armLeftPath = arm_energy["left_arm"]
                    legPath = leg_energy
                    headPath = head_energy

                    bodyPath = body_energy
                    energyViewModel.addImagesToControl(
                        mapOf("right_arm" to arm_energy["right_arm"]!!,
                            "left_arm" to arm_energy["right_arm"]!!,
                            "leg" to legPath!!,
                            "head" to headPath!!,
                            "body" to bodyPath!!)
                    )
                }
            }

            withContext(Dispatchers.Main){
                backroundPic = background
            }

        }

    }

    val changes by energyViewModel.changes.collectAsState()

    LaunchedEffect(changes) {
        if (changes.size != 0) {
            withContext(Dispatchers.IO) {
                val level_ph = localViewModel.getBodyLevel()

                for (i in changes ){

                    when (i.key) {
                        "right_arm" -> {

                            val arm_energ = storageViewModel.getArmEnergyPictures(i.value, level_ph!!)
                            withContext(Dispatchers.Main){
                                armRightPath = arm_energ["right_arm"]
                                armLeftPath = arm_energ["left_arm"]
                            }
                        }

                        "leg" -> {

                            val leg_energy = storageViewModel.getLegEnergyPictures(i.value, level_ph!!)
                            withContext(Dispatchers.Main){
                                legPath = leg_energy
                            }
                        }

                        "body" -> {
                            val body_energy = storageViewModel.getBodyEnergyPictures(i.value, level_ph!!)
                            withContext(Dispatchers.Main) {
                                bodyPath = body_energy
                            }
                        }
                    }
                }
            }
        }


    }



    Box(
        modifier = Modifier.wrapContentSize()
    ){
        Column(
            modifier = Modifier.wrapContentSize()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
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
                    if (backroundPic!= null ){

                        AsyncImage(
                            model = backroundPic,
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

                        MonitoredEnergyAsyncImage(
                            id = "head",
                            imageUrl = headPath,
                            modifier = Modifier
                        )


                        Row (modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Center){

                            MonitoredEnergyAsyncImage(
                                imageUrl = armLeftPath,
                                id = "left_arm",
                                modifier = Modifier

                            )

                            MonitoredEnergyAsyncImage (
                                imageUrl= bodyPath,
                                id = "body",
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    bodyImgSize = coordinates.size.toSize()

                                }.clickable {

                                },
                            )

                            MonitoredEnergyAsyncImage(
                                imageUrl = armRightPath,
                                id = "right_arm",


                            )
                        }

                        Row (modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){

                            MonitoredEnergyAsyncImage(
                                id = "left_leg",
                                imageUrl = legPath,
                            )

                            MonitoredEnergyAsyncImage(
                                id = "leg",
                                imageUrl = legPath)


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