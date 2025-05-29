package com.example.fitup.editing

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.fitup.R
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.StorageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EditNickname(
    localViewModel: LocalViewModel = hiltViewModel(),
    storageViewModel: StorageViewModel = hiltViewModel(),
    dbViewModel: DatabaseViewModel = hiltViewModel(),
    nav: NavController,
    context:Context
) {

    var new_nickname = remember { mutableStateOf("") }
    var current_nickname by remember { mutableStateOf("") }
    var backgroundPic by remember { mutableStateOf<Uri?>(null) };

    var isLoading by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        val getting_cur_username = localViewModel.getUserName()
        withContext(Dispatchers.Main) {
            current_nickname = getting_cur_username
        }

        val background = storageViewModel.getTextFieldBackground()
        backgroundPic = background
    }




    Box(modifier = Modifier.wrapContentSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            //header
            Text(
                text = "Изменение имени",
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            )

            // current name
            Text(
                text = "текущее имя: " + current_nickname,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            //changing field
            Box(
                modifier = Modifier.wrapContentSize().padding(10.dp)
            )
            {

                AsyncImage(
                    model = backgroundPic,
                    contentDescription = "background for textField",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                TextField(
                    value = new_nickname.value,
                    onValueChange = { new_nickname.value = it },
                    singleLine = true,
                    //label = {Text("Пароль")},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            //submit
            Button(
                onClick = {
                    isLoading = true
                }
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
//

                    Text(
                        "Подтверждаю",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                        )
                    )
                }


            }


        }

    }


    LaunchedEffect(isLoading) {
        if (isLoading) {
            if (new_nickname.value.isNotEmpty()){
                dbViewModel.changeNickname(new_nickname.value).collect{ task ->

                    if (task) {
                        nav.navigate("mainPage")
                    }else {
                        Toast.makeText(
                            context,
                            "попробуйте чуть позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            else {
                Toast.makeText(
                    context,
                    "некорректное имя",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}