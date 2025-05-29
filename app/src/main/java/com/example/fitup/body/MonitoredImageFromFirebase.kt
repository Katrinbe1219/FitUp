package com.example.fitup.body

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.fitup.hilt.SplittingBodyViewModel

@Composable
fun MonitoredAsyncImage (
    id : String,
    imageUrl: Uri?,
    splittingBodyViewModel: SplittingBodyViewModel = hiltViewModel(),
    onCLick: (() -> Unit)? = null,
    modifier: Modifier = Modifier

){

    val painter = rememberAsyncImagePainter(model = imageUrl)

    LaunchedEffect(painter.state) {
         if (painter.state is AsyncImagePainter.State.Success){
             splittingBodyViewModel.updateState(id, true)
         }
    }

    LaunchedEffect(painter) {
        snapshotFlow { painter.state }.collect{ state ->
            if (state is AsyncImagePainter.State.Success){
                splittingBodyViewModel.updateState(id, true)
            } else {
                Log.e("AsyncImageLogs", " problems with images from firebase - ${state}")
            }
        }
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = id,


    )

}