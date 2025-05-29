package com.example.fitup.body

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitup.Components.ExerciseCard
import com.example.fitup.items.Exercsice



@Composable
fun choseExecricesPage(nav: NavController){
    var all_excercies by remember { mutableStateOf(listOf<Exercsice>()) }
    var textChosenPart by remember { mutableStateOf("") }
    var flagSort by remember { mutableStateOf(false) }

    val filterOPtions = listOf("легко", "тяжко","трудно", "смерть")
    val (selectedFilter, onFilterSelected) = remember{ mutableStateOf("") }

    var exercies_ by remember { mutableStateOf(listOf<Exercsice>()) }


    fun filterExercices(option: String){
        exercies_ = all_excercies.filter { it.dificulty.returnFilter() == option  }
    }


    fun updateExecrices(newList: List<Exercsice>, bodyPart: String){
        all_excercies = newList
        textChosenPart = bodyPart
        flagSort = newList.size!=0

        if (selectedFilter ==""){
            exercies_ = newList
        }
        else{
            filterExercices(selectedFilter)
        }
    }



    Scaffold(
        bottomBar =  {
            Surface(
                modifier = Modifier.fillMaxWidth()
                    .height(75.dp),
                shape = RoundedCornerShape(30.dp),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.primary
            ) {
                Row (modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically){
                    // home
                    IconButton(onClick = {
                        nav.navigate("mainPage")
                    },
                    ) {
                        Icon(
                            Icons.Default.Home, contentDescription = "",
                            modifier = Modifier.size(52.dp))
                    }

                    //get excerise - splitting_body
                    IconButton(onClick = {
                        nav.navigate("list_excersices")
                    }) {
                        Icon(
                            Icons.Default.Build, contentDescription = null,
                            modifier = Modifier.size(52.dp))
                    }

                    //body power
                    IconButton(onClick = {
                        nav.navigate("editing_view")
                    }) {
                        Icon(
                            Icons.Default.Edit, contentDescription = null,
                            modifier = Modifier.size(52.dp))
                    }
                }
            }
        }
    ) { innerPadding ->

            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
//
                // body part
                SplitABodyAnimation(
                    updateListExercices = { newList, bodyPart ->
                        updateExecrices(newList, bodyPart)
                    }
                )


                // текст "list of exers
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.Start){

                    Text(text = "Список\nупражнений на",
                        color = MaterialTheme.colorScheme.secondary,
                        style = TextStyle(fontSize = 20.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily)
                    )
                    HorizontalDivider(thickness = 10.dp, color = Color.Transparent)

                    Text(text = textChosenPart,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(fontSize = 24.sp,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            textAlign = TextAlign.Start),
                        modifier = Modifier.fillMaxWidth())
                }

                HorizontalDivider(thickness = 10.dp, color = Color.Transparent)

                //chips to filter exes
                AnimatedVisibility(visible = flagSort) {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 0.dp)
                        .selectableGroup(),
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        filterOPtions.forEach { filter ->
                            FilterChip(

                                onClick = {onFilterSelected(filter)
                                          filterExercices(filter)},
                                label = { Text(filter,
                                    style = TextStyle(
                                        fontSize = 10.sp
                                    )) },
                                selected = (filter == selectedFilter)
//


                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 10.dp, color = Color.Transparent)
                // list of exs
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                            items(exercies_){ exec ->

                                ExerciseCard(exec)
                            }
                }


            }

    }
}