package com.example.fitup.hilt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitup.dataStore.OnBoardingStatesClass
import com.example.fitup.dataStore.UserLocalData
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class LocalViewModel @Inject constructor (
    private val userLocalData: UserLocalData,
    private val dbViewModel: DatabaseViewModel
) : ViewModel() {

    private val _onBoardingState = MutableStateFlow<OnBoardingStatesClass>(OnBoardingStatesClass.NotChecked)
    val onBOardingState: StateFlow<OnBoardingStatesClass> = _onBoardingState.asStateFlow()

    fun checkedOnBoarding() {
        viewModelScope.launch {
            _onBoardingState.value = getOnBoardingtState()
        }
    }

   suspend fun getUserName(): String = suspendCancellableCoroutine { continuation ->
       viewModelScope.launch(Dispatchers.IO) {
           val name = userLocalData.getUserName()
           continuation.resume(name)
       }
   }

    suspend fun getBodyLevel(): String? = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch (Dispatchers.IO){
            val level = userLocalData.getPersoLevel()
            continuation.resume(level)
        }
    }

    suspend fun getArmEnergy(): String = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch (Dispatchers.IO){
            val energy_level = userLocalData.getArmEnergy()
            continuation.resume(energy_level)
        }
    }

    suspend fun getLegEnergy(): String = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch (Dispatchers.IO){
            val energy_level = userLocalData.getLegEnergy()
            continuation.resume(energy_level)
        }
    }

    suspend fun getBodyEnergy(): String = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch (Dispatchers.IO){
            val energy_level = userLocalData.getBodyEnergy()
            continuation.resume(energy_level)
        }
    }

    suspend fun getAllEnergies(): MutableMap<String, String?> = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch (Dispatchers.IO){
            val energies = mutableMapOf<String, String?>()

            energies["arm"] = getArmEnergy()
            energies["leg"] = getLegEnergy()
            energies["body"] = getBodyEnergy()
            if (energies.any{(key, value) -> value == "null"}) {

                    var energies_ = mutableMapOf<String, String?>().apply {
                        putAll(energies)
                    }
                    if (energies["arm"] == "null") {
                        energies_["right_arm"] = dbViewModel.getBodyPartEnergy("right_arm")

                    }
                    if (energies["leg"] == "null") {
                        energies_["leg"] = dbViewModel.getBodyPartEnergy("leg")
                    }
                    if (energies["body"] == "null") {
                        energies_["body"] = dbViewModel.getBodyPartEnergy("body")
                    }
                    withContext(Dispatchers.Main){
                        continuation.resume(energies_)
                    }

            }else {
                withContext(Dispatchers.Main){
                    continuation.resume(energies)
                }
            }




        }

    }

    suspend fun  getOnBoardingtState ():  OnBoardingStatesClass =  withContext(Dispatchers.IO){

            var onBoardingState = userLocalData.getOnBoardComplete()
            if (onBoardingState == "") {
                val from_db = dbViewModel.getObBoradingState()
                if (from_db ) {
                    onBoardingState = "done"
                }else {
                    onBoardingState = "false"
                }
            }
            if (onBoardingState == "done") {
                OnBoardingStatesClass.Completed
            }else {
                OnBoardingStatesClass.Incomplete
            }


    }

    suspend fun saveUsername ( name: String ) : Boolean = suspendCancellableCoroutine{ continuation ->
        viewModelScope.launch(Dispatchers.IO) {
            userLocalData.saveUserName(name);
            continuation.resume(true)
        }


    }

    suspend fun savePhysicalForm ( physics : String) : Boolean = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch  (Dispatchers.IO){
            userLocalData.savePhysicalForm(physics)
            continuation.resume(true)
        }



    }

    suspend fun saveProfileWithoutIllneses ( name: String, physics: String ): Boolean = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch {
            savePhysicalForm(physics)
            saveUsername(name)
            continuation.resume(true)
        }



    }

    suspend fun saveOnBoardingState (state: OnBoardingStatesClass ): Boolean = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch(Dispatchers.IO) {
            when(state) {
                OnBoardingStatesClass.Completed -> {
                    userLocalData.saveOnBoardingState("done")

                }
                OnBoardingStatesClass.Incomplete -> {
                    userLocalData.saveOnBoardingState("not done")
                }
                OnBoardingStatesClass.NotChecked -> {
                    userLocalData.saveOnBoardingState("not done")
                }
            }
            continuation.resume(true)

        }
    }


    suspend fun clearAll(){
        userLocalData.clearDataStore()
    }





}