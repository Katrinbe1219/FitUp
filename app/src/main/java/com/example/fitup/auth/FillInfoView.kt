package com.example.fitup.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitup.R
import com.example.fitup.dataStore.OnBoardingStatesClass
import com.example.fitup.dataStore.OnBoardingViewModel
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.items.Illness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun fillInfo(
    nav: NavController,
    //onBoardViewModel: OnBoardingViewModel,
    localViewModel: LocalViewModel = hiltViewModel(),
    dbViewModel: DatabaseViewModel = hiltViewModel(),
    userViewModel: LocalViewModel = hiltViewModel()
){

    var nickname = remember{mutableStateOf("")}
    var expanded by remember { mutableStateOf(false) }
    var illness by remember { mutableStateOf("Выберите болезнь") }
    var illnesList by  remember { mutableStateOf(mutableListOf<Illness>()) }
    var illnessChosenList  = mutableListOf<Illness>()
    var selectedIndexes = remember { mutableStateListOf<Int>() }
    // mutableStateOf(mutableListOf<Int>()) does not track mutations like add() or remove()

    var selectedPhysicalForm by remember { mutableStateOf<String>("Ваша физическая подготовка") }
    var expanded_form by remember { mutableStateOf(false) }
    var isLoading = remember { mutableStateOf(false) }

    fun setPhysicalForm(type: Int){
        when(type) {
            1 -> selectedPhysicalForm = "small"
            2 -> selectedPhysicalForm = "middle"
            3 -> selectedPhysicalForm = "bigg"
        }

    }

    LaunchedEffect(Unit) {
        dbViewModel.getIllnesses().collect{ task ->

            if (!task.isEmpty()){
                val illnesses_ = mutableListOf<Illness>()
                task.forEach { id, name ->
                    illnesses_.add(Illness(id, name))
                }
                illnesList = illnesses_
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "АНКЕТА",
            style = TextStyle(
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        HorizontalDivider(thickness = 10.dp, color = Color.Transparent)

        //nickname
        Box(
            modifier = Modifier.wrapContentSize().padding(10.dp)
        ){
            Image(
                painter = painterResource(R.drawable.backo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            TextField(
                value = nickname.value,
                onValueChange = {nickname.value = it},
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                label = { Text("nickname") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.height(65.dp)

            )
        }



        //physical form
        Box(
            modifier =  Modifier.padding(16.dp)
        ){
            OutlinedButton (
                content = { Text(selectedPhysicalForm,
                    style = TextStyle(fontSize = 16.sp,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily),
                    color = MaterialTheme.colorScheme.onBackground
                ) },
                onClick = {expanded_form = !expanded_form},

                modifier = Modifier
                    .fillMaxWidth().padding(12.dp).height(65.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)

            )


            DropdownMenu(
                expanded = expanded_form,
                onDismissRequest = { expanded_form = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                    .padding(10.dp).width(250.dp).height(100.dp)
            ) {
                for ( i in 1..3 ){
                    DropdownMenuItem(
                        onClick = {setPhysicalForm(i)},
                        text = {
                            when(i) {
                                1 -> {Text(" 1 - новичок, мышщ практически нет")}
                                2 ->{Text("2 - существует небольшой рельеф")}
                                3 ->{Text("3 - качок, пью печеньку с молочоком")}
                            }

                        }
                    )
                }
            }
        }


        //illnesses
        Box(
            modifier = Modifier.padding(16.dp)
        ){

           OutlinedButton (
               content = { Text(illness,
                   style = TextStyle(fontSize = 16.sp,
                       fontFamily = MaterialTheme.typography.bodyLarge.fontFamily),
                   color = MaterialTheme.colorScheme.onBackground
               ) },
                onClick = {expanded = !expanded},

               modifier = Modifier
                   .fillMaxWidth().padding(10.dp).height(65.dp),
               shape = RectangleShape,
               colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)

           )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                    .padding(10.dp).width(250.dp).height(100.dp)
            ) {
                illnesList.forEach { illness ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(20.dp)
                            .clickable{
                            if (illness in illnessChosenList){
                                illnessChosenList.remove(illness)
                                selectedIndexes.remove(illness.id)
                            }
                            else {
                                illnessChosenList.add(illness)
                                selectedIndexes.add(illness.id)
                            }
                                  },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween


                    ){
                        Text(illness.name,
                            style = TextStyle(
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                fontSize = 12.sp
                            )
                        )
                        if (illness.id in selectedIndexes) { Icon(Icons.Default.Check, contentDescription = "")}
                    }
                }
            }
        }

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

    if ( isLoading.value ){
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
        if (isLoading.value ){
            if (nickname.value.isNotEmpty() && selectedPhysicalForm.isNotEmpty() ) {

                dbViewModel.saveProfileForm(selectedPhysicalForm, illnessChosenList, nickname.value).collect{  task ->
                    if (task){

                        userViewModel.saveProfileWithoutIllneses(nickname.value, selectedPhysicalForm)
                        withContext(Dispatchers.Main) {

                            localViewModel.saveOnBoardingState(OnBoardingStatesClass.Completed)
                            nav.navigate("mainPage")
                        }
                    }
                }
            }

            isLoading.value = false

        }
    }


}