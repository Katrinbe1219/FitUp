package com.example.fitup.editing

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditingView( nav: NavController ) {

    // options to edit
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
    ){ innerPadding ->
        Column  (
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background).padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ElevatedCard(
                onClick = {
                    nav.navigate("edit_nickname")
                },
                modifier = Modifier.padding(14.dp).height(100.dp).width(200.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Имя персонажа",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            ElevatedCard(
                onClick = {
                    nav.navigate("edit_illnesses")
                },
                modifier = Modifier.padding(14.dp).height(100.dp).width(200.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),

                ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Болезни",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 15.sp
                        )
                    )
                }

            }
            ElevatedCard(
                onClick = {
                    nav.navigate("edit_achiements")
                },
                modifier = Modifier.padding(14.dp).height(100.dp).width(200.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Достижения",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 15.sp
                        )
                    )
                }
            }
            ElevatedCard(
                onClick = {
                    nav.navigate("edit_energy")
                },
                modifier = Modifier.padding(14.dp).height(100.dp).width(200.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Загруженность",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 15.sp
                        )
                    )
                }
            }

        }
    }

}