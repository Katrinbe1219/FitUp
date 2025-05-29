package com.example.fitup

import android.util.Log
import androidx.collection.intIntMapOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitup.Components.myCharacter
import com.example.fitup.Components.scrollAchievemnts
import com.example.fitup.auth.AuthViewModel
import com.example.fitup.dataStore.UserLocalData
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.hilt.ProfileViewModel
import com.example.fitup.items.Achievement
import com.example.fitup.items.DifficultyAchievemnt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun mainPage( nav: NavController,
              localViewModel: LocalViewModel = hiltViewModel(),
              authViewModel: AuthViewModel = hiltViewModel(),
              profileViewModel: ProfileViewModel = hiltViewModel(),
              dbViewModel: DatabaseViewModel = hiltViewModel()
){

    var userName by remember { mutableStateOf( "") }

    var armEnergy by remember { mutableStateOf( "")  }
    var legEnergy  by remember { mutableStateOf( "") }
    var bodyEnergy by remember { mutableStateOf( "") }

    var isToSignOut by remember { mutableStateOf(false) }

    var achiementsListNew  = remember { mutableStateListOf<Achievement>() }

    LaunchedEffect(Unit) {

        val getting_energy = localViewModel.getAllEnergies()
        userName = localViewModel.getUserName()
        armEnergy = getting_energy["right_arm"]!!
        legEnergy= getting_energy["leg"]!!
        bodyEnergy = getting_energy["body"]!!



        var achievementsCurrent = mutableListOf<Achievement>()

        dbViewModel.getUserAchievemnts().collect{task ->
            withContext(Dispatchers.Main) {
                achievementsCurrent = task["current"]!!
            }

        }


        withContext(Dispatchers.Main){


            achiementsListNew.apply {
                clear()
                addAll(achievementsCurrent)
            }




        }
    }

    LaunchedEffect(isToSignOut) {
        if (isToSignOut){
            authViewModel.signOut()
            localViewModel.clearAll()

            withContext(Dispatchers.Main){
                nav.navigate("logIn")
            }
        }
    }

    val eliminateChild:  (  Achievement) -> Unit = { achieve ->
                achiementsListNew.removeAll { it.name == achieve.name }
                achieve.status = "old"

    }

    val colorsEnergyLevel: MutableMap<String, Color> = mutableMapOf(
        //"easy" to MaterialTheme.colorScheme.primary,
        "easy" to MaterialTheme.colorScheme.tertiary,
        "bad" to MaterialTheme.colorScheme.surfaceVariant,
        "smert" to MaterialTheme.colorScheme.surface,
        "" to Color.Transparent,
        "null" to Color.Transparent
    )
    val allImages by profileViewModel.allImagesReady.collectAsState()


    Box(modifier = Modifier.wrapContentSize()){

            Scaffold (
                contentWindowInsets = WindowInsets(10.dp),
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
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
                Column (
                    modifier = Modifier
                        .consumeWindowInsets(innerPadding)
                        .padding(vertical = 50.dp, horizontal = 25.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
//            verticalArrangement = Arrangement.Center,

                ){

                    Row(modifier = Modifier.fillMaxWidth().height(250.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(25.dp)
                    )
                    {
                        //  your perso
                        myCharacter()

                        // info
                        Column (
                            modifier = Modifier.fillMaxSize().padding(vertical = 25.dp, horizontal = 0.dp),
//                    horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceAround )
                        {


                            Text(text = userName,
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                                )
                            )//userName.value)

                            // харакеристики загруженности частей тела
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Text("руки: ")
                                    (if (armEnergy != null ) colorsEnergyLevel[armEnergy]!! else null)?.let {
                                        Surface (
                                            modifier = Modifier.size(width = 20.dp, height = 20.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            shadowElevation = 10.dp,
                                            color = it,
                                            content = {}

                                        )
                                    }
                                }
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Text("ноги: ")
                                    (if (legEnergy != null ) colorsEnergyLevel[legEnergy]!! else null)?.let {
                                        Surface (
                                            modifier = Modifier.size(width = 20.dp, height = 20.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            shadowElevation = 10.dp,
                                            color = it,
                                            content = {}

                                        )
                                    }
                                }

                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Text("торс: ")
                                    (if (bodyEnergy != null ) colorsEnergyLevel[bodyEnergy]!! else null)?.let {
                                        Surface (
                                            modifier = Modifier.size(width = 20.dp, height = 20.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            shadowElevation = 10.dp,
                                            color = it,
                                            content = {}

                                        )
                                    }
                                }

                            }


                            Button(
                                onClick = {
                                    isToSignOut = true
                                }
                            ) {
                                Text(text="Выйти")
                            }
                        }


                        // collectAsState converts the Flow into A Compose State<String> object
                        // initial = "" provides default empty string while waiting for the first emision

                        // achievements - horizontal pagers




                    }
                    // top right - coins
                    // left top - your perso - name - "power"

                    HorizontalDivider(thickness = 50.dp, color=Color.Transparent)


                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            scrollAchievemnts(achiementsListNew,  "Новых", eliminateChild = eliminateChild)
                        }




                }
            }

            if (!allImages){
                Box(
                    modifier = Modifier.matchParentSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.width(120.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }

            }
        }

}