package com.example.fitup.auth

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitup.R

import com.example.fitup.items.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun regPage(nav: NavController,
            context: Context,
            authViewModel: AuthViewModel = hiltViewModel()
){
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }


    Column (
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("ТЫ",
            style = TextStyle(
                fontSize = 58.sp,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        HorizontalDivider(thickness = 10.dp, color = Color.Transparent)

        //email
        Box (
            modifier = Modifier.wrapContentSize().padding(10.dp)
        ){

            Image(
                painter = painterResource(R.drawable.wide_back),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            TextField(
                value = email.value,
                onValueChange = {email.value = it},
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                label = {Text("email")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next // moving to the next textField
                )


            )
        }

        //password
        Box (
            modifier = Modifier.wrapContentSize().padding(10.dp)
        ){

            Image(
                painter = painterResource(R.drawable.wide_back),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            TextField(
                value = password.value,
                onValueChange = {password.value = it},
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                label = {Text("пароль")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation()


            )
        }

        HorizontalDivider(thickness = 10.dp, color = Color.Transparent)

        // registration button
        Button(
            onClick = {
                isLoading.value = true

            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                Image(
                    painter = painterResource(R.drawable.colored_dumbbell),
                    contentDescription = ""
                )
                Text("подтверждаю",
                    fontSize = 18.sp,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    )
            }
        }

    }

    if (isLoading.value){
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.width(128.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        // here putting async function is not reliable because of recompositions
        //Launched Effect will call finction only when the value becomes true, not when the composition is taking place
    }

    //
    LaunchedEffect(isLoading.value) {

        if(isLoading.value){
            if(email.value.isNotEmpty() && password.value.isNotEmpty()){
                authViewModel.signUpWithEmail(email.value, password.value).collect{ isCreated ->

                    isLoading.value = false

                    if (isCreated == "done"){

                        withContext(Dispatchers.Main) {
                            nav.navigate("logIn")
                        }

                    } else if (isCreated == "The email address is already in use by another account."){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Такой пользователь уже существует",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else if (isCreated == "") {
                        withContext(Dispatchers.Main) {
                            nav.navigate("logIn")
                        }
                    }else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Попробуйте чуть позже",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
            //withContext blocks not the main Thread, but it suspends the current coroutine - Launched Effect

            // using simple variable checking is not right , since withContext is async,
            // var will not be immediatly updated, because listenter is a callback


        }




    }



}