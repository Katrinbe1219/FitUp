package com.example.fitup.editing

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitup.Components.scrollAchievemnts
import com.example.fitup.R
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.items.Achievement
import com.example.fitup.items.DifficultyAchievemnt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EditAchievements(
    dbViewModel: DatabaseViewModel = hiltViewModel()
){

    val new_name = remember { mutableStateOf("") }
    val new_description = remember { mutableStateOf("") }
    val selectedDifficultyText = remember { mutableStateOf("") }
    val selectedDifficulty = remember { mutableStateOf<DifficultyAchievemnt?>(null) }

    val pointsOptions = listOf ("150", "100")
    val (selectedPoint, onPointSelected) = remember { mutableStateOf(pointsOptions[0]) }

    val count_steps = remember{ mutableStateOf("") }

    // for difficulty
    var expanded by remember { mutableStateOf(false) }

    var achiementsListNew  = remember { mutableStateListOf<Achievement>() }
    var achiementsListOld = remember { mutableStateListOf<Achievement>() }
    var isClicked by remember { mutableStateOf(false) }


    fun changeSelectedDifficulty (new_value: String){
        selectedDifficultyText.value = new_value
        if (new_value == "легкое") selectedDifficulty.value = DifficultyAchievemnt.EASY
        else if (new_value == "трудное") selectedDifficulty.value = DifficultyAchievemnt.HARD
        else if (new_value == "смерть") selectedDifficulty.value = DifficultyAchievemnt.SMERT

    }

    val eliminateChild:  (  Achievement) -> Unit = { achieve ->

        when (achieve.status){

            "current" -> {
                val real_list = achiementsListNew.toMutableList()
                val element = real_list.filter { it.name == achieve.name }.first()
                achiementsListNew.removeAll { it.name == achieve.name }
                achiementsListOld.add(element)

                achieve.status = "old"
            }
            "old" -> {
                val real_list = achiementsListOld.toMutableList()
                val element = real_list.filter { it.name == achieve.name }.first()
                achiementsListOld.removeAll { it.name == achieve.name }
                achiementsListNew.add(element)

                achieve.status = "current"
            }
        }

    }

    LaunchedEffect(Unit) {

        withContext(Dispatchers.IO){
            var achievementsCurrent = mutableListOf<Achievement>()
            var achievementsOld = mutableListOf<Achievement>()

            dbViewModel.getUserAchievemnts().collect{task ->
                withContext(Dispatchers.Main) {

                    achievementsCurrent = task["current"]!!
                    achievementsOld = task["old"]!!
                }

            }


            withContext(Dispatchers.Main){
                achiementsListOld.apply {
                    clear()
                    addAll(achievementsOld)
                }

                achiementsListNew.apply {
                    clear()
                    addAll(achievementsCurrent)
                }

            }

        }


    }


    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(top = 50.dp),
        contentAlignment = Alignment.Center)
    {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // adding new
            item {
                Text(
                    text = "Добавить новое",
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
                HorizontalDivider(color = Color.Transparent, thickness = 10.dp)
                // name
                Box (
                    modifier =Modifier.width(250.dp).height(60.dp)
                )
                {
                    Image(
                        painter = painterResource(R.drawable.wide_back),
                        contentDescription = "bacground for input",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    TextField(value=  new_name.value,
                        onValueChange = { new_name.value = it },
                        singleLine = true,
                        label = { Text("Название")},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }

                HorizontalDivider(color = Color.Transparent, thickness = 10.dp)
                // description
                Box (
                    modifier =Modifier.size(250.dp)
                )
                {
                    Image(
                        painter = painterResource(R.drawable.wide_back),
                        contentDescription = "bacground for input",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    TextField(value=  new_description.value,
                        onValueChange = { new_description.value = it },
                        singleLine = false,
                        label = { Text("Описание")},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
                HorizontalDivider(color = Color.Transparent, thickness = 10.dp)
                //difficulty - dropDown menu
                Box(
                    modifier =  Modifier.width(250.dp).height(60.dp)
                ){
                    OutlinedButton (
                        content = { Text(text = if (selectedDifficultyText.value == "") "Сложность" else selectedDifficultyText.value,
                            style = TextStyle(fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily),
                            color = MaterialTheme.colorScheme.onBackground
                        ) },
                        onClick = {expanded = !expanded},

                        modifier = Modifier
                            .fillMaxWidth().padding(12.dp).height(65.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)

                    )


                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                            .padding(10.dp).width(250.dp).height(100.dp)
                    ) {
                        for ( i in 1..3 ){
                            DropdownMenuItem(
                                onClick = {
                                    when(i) {
                                        1 -> {changeSelectedDifficulty("легкое")}
                                        2 ->{changeSelectedDifficulty("трудное")}
                                        3 ->{changeSelectedDifficulty("смерть")}
                                    }

                                },
                                text = {
                                    when(i) {
                                        1 -> {Text("легкое")}
                                        2 ->{Text("трудное")}
                                        3 ->{Text("смерть")}
                                    }

                                }
                            )
                        }
                    }
                }


                HorizontalDivider(color = Color.Transparent, thickness = 10.dp)
                // count_steps - text field
                Box (
                    modifier =Modifier.width(250.dp).height(60.dp)
                )
                {
                    Image(
                        painter = painterResource(R.drawable.wide_back),
                        contentDescription = "bacground for input",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    TextField(value=  count_steps.value,
                        onValueChange = { count_steps.value = it },
                        singleLine = true,
                        label = { Text("Кол-во шагов")},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
                HorizontalDivider(color = Color.Transparent, thickness = 10.dp)
                // all_coins options - radio buttons
                Row (
                    modifier = Modifier.selectableGroup().width(250.dp).height(60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    pointsOptions.forEach { point ->
                        Row (
                            modifier = Modifier.selectable(
                                selected =  ( point == selectedPoint),
                                onClick = { onPointSelected(point)},
                                role = Role.RadioButton
                            )
                        ){
                            RadioButton(
                                selected =  (point == selectedPoint),
                                onClick = null
                            )

                            Text(
                                text = point
                            )
                        }
                    }
                }

                Button (
                    onClick = {
                        isClicked = true

                    }
                ){
                    Text(text = "Создать")

                }


            }


            // now


                // current
                item {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        scrollAchievemnts(achiementsListNew, "Новых",  eliminateChild = eliminateChild)
                    }

                }

                // old
                item {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        scrollAchievemnts(achiementsListOld, "Старых",  eliminateChild = eliminateChild)
                    }

                }

        }
    }


    LaunchedEffect(isClicked) {
        if (isClicked) {
            if (count_steps.value.isNotBlank() && new_name.value.isNotBlank() && new_description.value.isNotBlank() // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                && selectedDifficulty.value != null && count_steps.value.isNotBlank() && count_steps.value.isDigitsOnly()){

                dbViewModel.createAchiement(new_name.value, new_description.value, selectedDifficulty.value!!, selectedPoint, count_steps.value ).collect { task ->
                    isClicked = false

                    if (task) {
                        withContext(Dispatchers.Main){
                            val new_achievement = Achievement(
                                name = new_name.value,
                                description = new_description.value,
                                cur_step = 0,
                                earned_coins = 0,
                                status = "current",
                                all_coins = selectedPoint.toInt(),
                                all_steps = count_steps.value.toInt(),
                                dificulty = selectedDifficulty.value

                            )

                            achiementsListNew.add(new_achievement)
                        }


                    }
                }

            }

        }
    }
}

