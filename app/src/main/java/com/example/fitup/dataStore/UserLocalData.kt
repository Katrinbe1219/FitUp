package com.example.fitup.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

// can not save info to DataStore in main THread, since async functions are here
class UserLocalData private constructor ( private val context: Context) {

    //creating what i want to keep there
    // companion object helps keeping this only once initialized
    companion object{

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userName")
        // preferenesDataStore creates a Data store with filename userName.preferences_pb
        // by - the keyword makes it lazy-delegated property

        //DataStore<Preferences> sprecializeed for key-value pairs
        private val  ON_BOARD_COMPLETE = stringPreferencesKey("no")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_PERSO_LEVEL = stringPreferencesKey("perso_level")
        private val USER_HAS_ILLNESS = booleanPreferencesKey("user_has_illness")


        private val USER_ENERGY_ARM = stringPreferencesKey("perso_energy_arm")
        private val USER_ENERGY_LEG = stringPreferencesKey("perso_energy_leg")
        private val USER_ENERGY_BODY = stringPreferencesKey("perso_energy_body")

        //make this singleton and use with hilt
        @Volatile private var instance: UserLocalData? = null

        fun getInstance (context: Context): UserLocalData {
            return instance ?: synchronized(this){
                instance ?: UserLocalData(context.applicationContext).also { instance = it }
            }
        }
    }


    suspend fun getPersoLevel(): String? {
        return context.dataStore.data.map{ preferences ->
            preferences[USER_PERSO_LEVEL] ?: "small"
        }.first()
    }

    suspend fun getUserName() : String {
        return context.dataStore.data.map{ preferences ->
            preferences[USER_NAME] ?: "User"} .first()
    }

    suspend fun getUserHasIllness () : Boolean {
        return context.dataStore.data.map { preference ->
            preference[USER_HAS_ILLNESS] ?: false
        }.first()
    }

    suspend fun  getOnBoardComplete() : String {
        return context.dataStore.data.map{ prefereence ->
            prefereence[ON_BOARD_COMPLETE] ?: ""
        }.first()
    }

    suspend fun getArmEnergy(): String {
        return context.dataStore.data.map{ preferences ->
            preferences[USER_ENERGY_ARM] ?: "null"}.first()
    }


    suspend fun getLegEnergy(): String {
        return context.dataStore.data.map{ preferences ->
            preferences[USER_ENERGY_LEG] ?: "null"
        }.first()
    }

    suspend fun  getBodyEnergy(): String {
        return context.dataStore.data.map{ preferences ->
            preferences[USER_ENERGY_BODY] ?: "null"
        }.first()
    }

    suspend fun saveUserHasIllness (has_illness : Boolean) {
        context.dataStore.edit { preference ->
            preference[USER_HAS_ILLNESS] = has_illness
        }
    }


    suspend fun saveOnBoardingState( state: String) {
        context.dataStore.edit { preference ->
            preference[ON_BOARD_COMPLETE] = state
        }
    }
    suspend fun saveUserName(name: String){
        context.dataStore.edit{preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun  savePhysicalForm( physics: String ){
        context.dataStore.edit { prefernce ->
            prefernce[USER_PERSO_LEVEL] = physics
        }
    }



    suspend fun  saveArmEnergy(level: String){
        context.dataStore.edit{ preferences ->
            preferences[USER_ENERGY_BODY] = level
        }
    }
    suspend fun  saveLegEnergy(level: String){
        context.dataStore.edit{ preferences ->
            preferences[USER_ENERGY_LEG] = level
        }
    }
    suspend fun  saveTorsoEnergy(level: String){
        context.dataStore.edit{ preferences ->
            preferences[USER_ENERGY_BODY] = level
        }
    }

     suspend fun clearDataStore() {
        context.dataStore.edit { preferences ->
            preferences.clear() // Clears all key-value pairs
        }
    }
}


