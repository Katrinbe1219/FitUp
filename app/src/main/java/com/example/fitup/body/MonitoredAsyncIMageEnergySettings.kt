package com.example.fitup.body

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.fitup.hilt.EnergyViewModel
import com.example.fitup.hilt.ProfileViewModel

@Composable
fun MonitoredEnergyAsyncImage (
    id : String,
    imageUrl: Uri?,
    energyViewModel: EnergyViewModel = hiltViewModel(),
    onCLick: (() -> Unit)? = null,
    modifier: Modifier = Modifier

) {

    val painter = rememberAsyncImagePainter(model = imageUrl)




    LaunchedEffect(painter) {
        snapshotFlow { painter.state }.collect { state ->
            if (state is AsyncImagePainter.State.Success) {

                energyViewModel.updateState(id, true)

            } else {
                Log.e("AsyncImageLogs", "  problems energy ${id}  ${imageUrl}    ${state}")
            }
        }
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = id,


        )

}