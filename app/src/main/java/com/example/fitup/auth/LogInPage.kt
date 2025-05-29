package com.example.fitup.auth

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.fitup.MainActivity
import com.example.fitup.R
import com.example.fitup.dataStore.OnBoardingViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

@Composable
fun logIn(nav: NavController,
          context: Context,
          authViewModel: AuthViewModel = hiltViewModel()
){

    var isLoading = remember { mutableStateOf(false) }
    var emailText = remember { mutableStateOf("") }
    var passwordText = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Text("КТО ТЫ?",
            style = TextStyle(
                fontSize = 58.sp,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                color = MaterialTheme.colorScheme.tertiary
            )
        )

        HorizontalDivider(thickness = 20.dp, color = Color.Transparent)

        // email
        Box (
            modifier =Modifier.wrapContentSize().padding(10.dp))
        {
            Image(
                painter = painterResource(R.drawable.wide_back),
                contentDescription = "bacground for input",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            TextField(value=emailText.value,
                onValueChange = { emailText.value = it },
                singleLine = true,
                label = { Text("Почта")},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
                )
        }

        // password
        Box (
            modifier =Modifier.wrapContentSize().padding(10.dp))
        {
            Image(
                painter = painterResource(R.drawable.wide_back),
                contentDescription = "bacground for input",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            TextField(value=passwordText.value,
                onValueChange = { passwordText.value = it },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                label = {Text("Пароль")},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }

        HorizontalDivider(thickness = 20.dp, color = Color.Transparent)

        // log in button
        Button(
            onClick = {
                isLoading.value = true
            }
        ) {
            Row (modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Image(
                    painter = painterResource(R.drawable.colored_dumbbell),
                    contentDescription = null)
                Text("Подтверждаю",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                    ))
            }

        }

    }

    if(isLoading.value){
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.width(70.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

    }

    LaunchedEffect(isLoading.value) {

        if(isLoading.value){

            if(emailText.value.isNotEmpty() && passwordText.value.isNotEmpty()){
                authViewModel.signInWithEmail(emailText.value, passwordText.value).collect{ isOnBoardingState ->
                    isLoading.value = false
                    if (isOnBoardingState){
                        Toast.makeText(
                            context,
                            "Проверка пройдена",
                            Toast.LENGTH_SHORT
                        ).show()

                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(
                                context,
                                "Не заполнена анкета",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }

                }
            }



        }

    }
}