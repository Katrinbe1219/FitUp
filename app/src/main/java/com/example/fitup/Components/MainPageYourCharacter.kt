package com.example.fitup.Components


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fitup.R
import com.example.fitup.body.MonitoredAsyncImage
import com.example.fitup.body.MonitoredProfileAsyncImage
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.ProfileViewModel
import com.example.fitup.hilt.StorageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun myCharacter(
    storageViewModel: StorageViewModel = hiltViewModel(),
    localViewModel: LocalViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
){

    var background by remember { mutableStateOf<Uri?>(null) }

    var rightArm by remember { mutableStateOf<Uri?>(null) }
    var leftArm by remember { mutableStateOf<Uri?>(null) }
    var head by remember { mutableStateOf<Uri?>(null) }
    var body by remember { mutableStateOf<Uri?>(null) }
    var leg by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val level = localViewModel.getBodyLevel()
            val resources = if (level != null) { storageViewModel.getProfileBodyParts(level)} else null
            val background_pic = storageViewModel.getBackground()

            withContext(Dispatchers.Main) {


                if ( resources != null && resources.size == 5 ) {


                    background = background_pic
                    profileViewModel.addImagesToControl(resources)
                    resources.forEach{ key, value ->

                        if (key == "right_arm" ) {  rightArm = value }
                        else if ( key == "left_arm" ) { leftArm = value }
                        else if ( key == "body" ) { body = value }
                        else if ( key == "head" ) { head = value }
                        else if (key == "leg" ) {leg = value}

                    }
                }
            }
        }
    }


    ElevatedCard(
        modifier = Modifier.size(170.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (background != null){

                AsyncImage(
                    model = background,
                    contentDescription = "background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),

                    )
            }


            // сделать mini версию частей тела
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.wrapContentSize()
            ) {
                 MonitoredProfileAsyncImage(
                    id = "head",
                    imageUrl = head

                )

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {

                    MonitoredProfileAsyncImage(
                        id = "left_arm",
                        imageUrl = leftArm

                    )
                    MonitoredProfileAsyncImage(
                        id = "body",
                        imageUrl = body

                    )
                    MonitoredProfileAsyncImage(
                        id = "right_arm",
                        imageUrl = rightArm

                    )



                }
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MonitoredProfileAsyncImage(
                        id = "left_leg",
                        imageUrl = leg
                    )
                    MonitoredProfileAsyncImage(
                        id = "leg",
                        imageUrl =leg

                    )


                }


            }
        }
    }
}