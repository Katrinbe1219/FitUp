package com.example.fitup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fitup.auth.AuthViewModel
import com.example.fitup.auth.fillInfo
import com.example.fitup.auth.firstPage
import com.example.fitup.auth.logIn
import com.example.fitup.auth.regPage
import com.example.fitup.body.choseExecricesPage
import com.example.fitup.dataStore.OnBoardingStatesClass
import com.example.fitup.dataStore.OnBoardingViewModel
import com.example.fitup.dataStore.OnBoardingViewModelFactory
import com.example.fitup.dataStore.UserLocalData
import com.example.fitup.editing.EditAchievements
import com.example.fitup.editing.EditBodyEnergy
import com.example.fitup.editing.EditIlnesses
import com.example.fitup.editing.EditNickname
import com.example.fitup.editing.EditingView
import com.example.fitup.hilt.DatabaseViewModel
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.ui.theme.MyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {




    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val authViewModel: AuthViewModel by viewModels()
            val localViewModel: LocalViewModel by viewModels()

            var auth = authViewModel.getAuth()
            val cUser = auth.currentUser


            val context = LocalContext.current
            localViewModel.checkedOnBoarding()
            val onBoardingState by localViewModel.onBOardingState.collectAsState()



            MyTheme  {
                if (onBoardingState == OnBoardingStatesClass.NotChecked) {
                    Column (
                        modifier = Modifier.padding(10.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ){

                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.tertiary,

                            )
                    }
                }
                else {
                    val navCon =rememberNavController()


                    NavHost(navCon,
                        startDestination = if (cUser ==null
                        ) {"auth"}
                        else {"profile"}


                    ){

                        navigation(startDestination = "first_page", route="auth"){

                            composable(route="first_page"){
                                firstPage(navCon)
                            }
                            composable(route = "logIn"){
                                logIn(navCon,  context)
                            }
                            composable(route = "register"){
                                regPage(navCon, context)
                            }


                        }

                        navigation(startDestination =
                                    when(onBoardingState) {
                                        OnBoardingStatesClass.Incomplete -> {
                                            "fill_info"
                                        }
                                        OnBoardingStatesClass.Completed -> {
                                            "mainPage"
                                        }
                                        OnBoardingStatesClass.NotChecked -> {
                                            "loading"
                                        }

                                    },
                                route="profile")
                        {
                            composable(route = "mainPage"){
                                mainPage( navCon)
                            }
                            composable(route = "list_excersices"){
                                choseExecricesPage(navCon)
                            }

                            composable(route="loading"){
                                Loading()
                            }
                            composable(route = "fill_info"){
                                fillInfo(navCon)
                            }


                            composable(route = "editing_view"){
                                EditingView(navCon)
                            }
                            composable(route = "edit_nickname"){
                                EditNickname(nav = navCon, context = context)
                            }
                            composable(route = "edit_illnesses"){
                                EditIlnesses(nav = navCon,context =  context)
                            }

                            composable(route = "edit_energy"){
                                EditBodyEnergy()
                            }
                            composable(route = "edit_achiements"){
                                EditAchievements()
                            }
                        }

                    }
                }

            }
        }
    }
}

