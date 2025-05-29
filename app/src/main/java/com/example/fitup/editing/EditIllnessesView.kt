package com.example.fitup.editing

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitup.R
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.items.Illness

@Composable
fun EditIlnesses(
    dbViewModel: DatabaseViewModel = hiltViewModel(),
    nav: NavController,
    context: Context
){

    var expanded by remember { mutableStateOf(false) }
    var illnesList by  remember { mutableStateOf(mutableListOf<Illness>()) }
    var illnessChosenList  = remember { mutableListOf<Illness>()} // without remember LE will not ssee difference
    var selectedIndexes = remember { mutableStateListOf<Int>() }
    var isLoading  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dbViewModel.getIllnesses().collect { task ->

            if ( !task.isEmpty() ){
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
            .padding(10.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier.padding(16.dp)
        ){

            OutlinedButton(
                content = {
                    Text("Выберите болезни",
                        style = TextStyle(fontSize = 16.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            color = MaterialTheme.colorScheme.onBackground)
                    )
                },
                onClick = {expanded = !expanded},
                modifier = Modifier.fillMaxWidth().padding(10.dp).height(65.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)

            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                    .padding(10.dp).width(250.dp).height(100.dp)
            ) {
                illnesList.forEach{ illness ->
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
                        if (illness.id in selectedIndexes) { Icon(Icons.Default.Check, contentDescription = "")
                        }

                    }
                }
            }


        }

        Button(
            onClick = {
                isLoading = true
            }
        ) {
            Row (modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
//

                Text("Подтверждаю",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                    ))
            }
        }
    }

    LaunchedEffect(isLoading) {
        if (isLoading){
             dbViewModel.changeIllnesses(illnessChosenList).collect{task ->
                 if (task) {
                     nav.navigate("mainPage")
                 }
                 else {
                     Toast.makeText(
                         context,
                         "попробуйте позже",
                         Toast.LENGTH_SHORT
                     ).show()
                 }
             }

            isLoading  = false
        }
    }
}