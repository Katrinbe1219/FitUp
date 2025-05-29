package com.example.fitup.hilt

import android.util.Log
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitup.auth.AuthManager
import com.example.fitup.auth.AuthViewModel
import com.example.fitup.dataStore.OnBoardingStatesClass
import com.example.fitup.dataStore.UserLocalData
import com.example.fitup.items.Achievement
import com.example.fitup.items.AddAchievment
import com.example.fitup.items.DifficultyAchievemnt
import com.example.fitup.items.Illness
import com.example.fitup.items.User
import com.example.fitup.items.UserWithIllness
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DatabaseManager @Inject constructor(

   // private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
){
    sealed class QueryStatus{
        object InProgress : QueryStatus()
        object Incomplete : QueryStatus()
        object Completed : QueryStatus()
    }

    sealed class OnBoardingState{
        // паттер - Конечный автомат

        // здесь происзодит аналогия работы enum class,
        // Loading и тд являются конкретным состоянием
        object Loading: OnBoardingState()
        object Incomplete: OnBoardingState()
        object Completed: OnBoardingState()
        //data class Error(val message: String?): OnBoardingState() // еще одно состояние, но которое еще содержит инфу

    }

    suspend fun createUser(uid: String): QueryStatus  = suspendCancellableCoroutine { continuation ->
        val user = User()

        dbReference.child("users").child(uid).setValue(user).addOnCompleteListener { task ->

            if (task.isSuccessful){
                 continuation.resume(QueryStatus.Completed)
            }
            else{
                continuation.resume(QueryStatus.Incomplete)
            }

        }.addOnFailureListener { e ->
            Log.e("FIREBASE", "Failure: ${e.message}")
            continuation.resume(QueryStatus.Incomplete)
        }


    }

    suspend fun  checkOnBoardingState ( uid: String ) = suspendCancellableCoroutine { continuation ->

            dbReference.child("users").child(uid)
                .addListenerForSingleValueEvent(
                    object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val receivedInfo = snapshot.getValue(User::class.java) // put all fields in user class
                            if(receivedInfo?.on_board_complete == true) {
                                continuation.resume(OnBoardingStatesClass.Completed)
                            }
                            else {
                                continuation.resume( OnBoardingStatesClass.Incomplete)
                            }


                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }

                )




    }

    fun getDbReference(): DatabaseReference {
        return dbReference
    }

    suspend  fun fetchIllneses(): MutableMap<Int, String>{
        val result = mutableMapOf<Int, String>()
        try{
           val dataSnapshot =  dbReference.child("ilnesses").get().await()


            if ( dataSnapshot.exists() ) {
                for (illness in dataSnapshot.children ){
                    illness.key?.let {key ->
                        result[key.toInt()] = illness.value.toString() ?: ""
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RealtimeDatabaseLogs", "RealtimeDb gets error  with fetching all illnesess ${e.message}")
        }
        return  result
    }

    suspend fun  fetchUsersIllnesses() : MutableList<String>{
        val illneses_id = mutableListOf<Int>()
        val illneses = mutableListOf<String>()

        try{
            val dataSnapShot = dbReference.child("users_with_illnesss").get().await()
            if (dataSnapShot.exists()) {
                for (illness in dataSnapShot.children){
                    illneses_id.add(illness.value.toString().toInt())
                }
            }

            val ilnessesSnapShot = dbReference.child("ilnesses").get().await()
            if (ilnessesSnapShot.exists()){
                 for ( illness in ilnessesSnapShot.children) {
                    if (illness.key?.toInt() in illneses_id) {
                        illneses.add(illness.value.toString())
                    }
                 }
            }

        } catch (e: Exception){
            Log.e("KATRIN_BE", "RealtimeDb gets error with fetching users illnesses ${e.message}")
        }

        return illneses
    }

    suspend fun saveProfileForm(information: Map<String, Any>, uid: String): Boolean = suspendCancellableCoroutine{ continuation ->
        dbReference.child("users").child(uid).updateChildren(information).addOnCompleteListener{ task ->
            if(task.isSuccessful) continuation.resume(true)
            else {
                Log.d("RealtimeDatabaseLogs","problems with saving Profile Form - ${task.exception?.message}")
                continuation.resume(false)
            }


        }
    }

    suspend fun saveUserWithIllness(uid: String, info: Map<String, MutableList<Int> >):Boolean = suspendCancellableCoroutine{ continuation ->
        try {
            val path = dbReference.child("users_with_illness").child(uid)
            val snapshot = path.get()

            snapshot.addOnCompleteListener { task_first ->
                if (task_first.isSuccessful && snapshot.result.exists()){

                    path.updateChildren(info)
                        .addOnCompleteListener { task ->
                                if(task.isSuccessful) continuation.resume(true)

                        }.addOnFailureListener { e ->
                            Log.d("RealtimeDatabaseLogs", "Problems with saving Form profile - ${e.message}")
                            continuation.resume(false)
                        }
                }
                else if (task_first.isSuccessful && !snapshot.result.exists()){
                    path.setValue(info)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) continuation.resume(true)

                        }.addOnFailureListener { e ->
                            Log.d("RealtimeDatabaseLogs", "Problems with saving Form profile - ${e.message}")
                            continuation.resume(false)
                        }
                }
            }
        } catch (e: Exception){
            Log.e("RealtimeDatabaseLogs", "problems with saveUserWithUlness: ${e.message}")
        }



    }

    suspend fun changeNickname (uid: String, nickname: String) : Boolean = suspendCancellableCoroutine { continuation ->
        dbReference.child("users").child(uid)
            .updateChildren( mapOf ("nickname" to nickname))
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    continuation.resume(true)
                }
                else {
                    Log.e("RealtimeDatabaseLogs", "problem with changingNickanme ${task.exception?.message}")
                    continuation.resume(false)
                }
            }
    }

    suspend fun  deleteAllIllnesses(uid: String): Boolean = suspendCancellableCoroutine {  continuation ->
        try {
            dbReference.child("users_with_illness").child(uid).removeValue()
            continuation.resume(true)

        }catch (e: Exception) {
            continuation.resumeWithException(e)
        }


    }

    suspend fun setUserHasIllness (uid: String, has_illness: Boolean) : Boolean = suspendCancellableCoroutine {  continuation ->
        dbReference.child("users").child(uid).updateChildren(mapOf( "is_illness" to has_illness)).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                continuation.resume(true)
            }else {
                Log.e("RealtimeDatabaseLogs", "${task.exception?.message}")
                continuation.resume(false)
            }
        }
    }

    suspend fun editEnergyLevel(uid: String, info: Map<String, String> ): Boolean = suspendCancellableCoroutine{ continuation ->
        try {
            dbReference.child("users").child(uid).updateChildren(info)
                .addOnSuccessListener { task ->
                    continuation.resume(true)
                }
                .addOnFailureListener { task ->
                    continuation.resume(false)
                }



        }
        catch(e: Exception ) {
            Log.d("RealtimeDatabaseLogs", "problems woth editEnergy ${e.message}")
        }
    }

    suspend fun fetchUsersAchiements(uid: String) : MutableList<Achievement> {
        val result = mutableListOf<Achievement>()
        try {

            val dataSnapshot = dbReference.child("achievements").child(uid).get().await()
            if (dataSnapshot.exists()){

                for (achiev in dataSnapshot.children) {
                    var achiev_info = Achievement()
                    achiev_info.name = achiev.key

                    for (detail in achiev.children) {
                        when (detail.key) {
                            "all_coins" ->{
                                achiev_info.all_coins = detail.value.toString().toInt()
                            }
                            "earned_coins" -> {
                                achiev_info.earned_coins = detail.value.toString().toInt()
                            }
                            "all_steps" -> {
                                achiev_info.all_steps = detail.value.toString().toInt()
                            }
                            "cur_step" -> {
                                achiev_info.cur_step = detail.value.toString().toInt()
                            }
                            "description" -> {
                                achiev_info.description = detail.value.toString()
                            }
                            "dificulty" -> {
                                val value = detail.value
                                when (value){
                                    "easy" -> achiev_info.dificulty = DifficultyAchievemnt.EASY
                                    "bad" -> achiev_info.dificulty = DifficultyAchievemnt.HARD
                                    "smert" -> achiev_info.dificulty = DifficultyAchievemnt.SMERT
                                }
                            }

                            "status" -> {
                                achiev_info.status = detail.value.toString()

                            }

                        }


                    }
                    result.add( achiev_info)
                }
            }
        }
        catch (e: Exception){
            Log.d("RealtimeDatabaseLogs", "problems with fetching Achiements ${e.message}")
        }
        return result

    }

    suspend fun createAchievemnt(uid: String, new_achie: AddAchievment) : Boolean{
        try {
            val path = dbReference.child("achievements").child(uid)
            val snapShot =  path.get().await()
            if (snapShot.exists()) {
                Log.d("KATRIN_BE", "1suuukla")
                path.updateChildren(mapOf(new_achie.name to new_achie))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("RealtimeDatabaseLogs", "the achievement was created")
                        }
                        else {
                            Log.d("RealtimeDatabaseLogs", "the achievement was  not created ${task.exception?.message}")

                        }
                    }
            }else {
                Log.d("KATRIN_BE", "2suuukla")
                dbReference.child("achievements").updateChildren(mapOf(uid to mapOf (new_achie.name to new_achie)))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("RealtimeDatabaseLogs", "the first achievement was created")
                        }
                        else {
                            Log.d("RealtimeDatabaseLogs", "the first achievement was  not created ${task.exception?.message}")

                        }
                    }
            }

        }
        catch(e: Exception) {
            Log.d("RealtimeDatabaseLogs", "the achievement was not created with excep ${e.message}")

            return false
        }

        return true

    }

    suspend fun getBodyPartEnergy (uid: String, body_part: String): String? {
        try {
            val snaphot = dbReference.child("users").child(uid).child(body_part).get().await()
            return snaphot.value.toString()

        }
        catch (e: Exception){
            Log.e("RealtimeDatabaseLogs", "probems with getting body part ${body_part} -  ${e.message}")
           return  null
        }
    }

    suspend fun changeAchievementStatus (uid: String, status: String, achievemnt: String) : Boolean {
        try {
            val path = dbReference.child("achievements").child(uid).child(achievemnt)
            val snapshot = path.get().await()
            if (snapshot.exists()) {
                path.updateChildren(mapOf("status" to status)).addOnCompleteListener { task ->
                    if (!task.isSuccessful)  { Log.d("KATRIN_BE", "there is a problem with changing status of achievemnt ${task.exception?.message}")}
                }
            }

        }
        catch (e: Exception) {
            Log.d("RealtimeDatabaseLogs", "problems with changing status of achievement - ${e.message}")
            return  false
        }
        return true
    }



}

// it checks Hilt models for providers
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val dbManager: DatabaseManager,
    private val userLocalData: UserLocalData,
    private val authManager: AuthManager
): ViewModel() {

    fun getRefernce(): DatabaseReference {
        return dbManager.getDbReference()
    }

    suspend fun getIllnesses(): Flow<MutableMap<Int, String>> = flow {
        var result: MutableMap<Int, String>
        withContext(Dispatchers.IO) {
             result = dbManager.fetchIllneses()

        }
        emit(result)

    }

    suspend fun saveProfileForm(physicalForm: String, illneses: MutableList<Illness>, nickname: String): Flow<Boolean> = flow {

        val userUpdate = mapOf<String, Any>("is_illness" to (illneses.size!=0),
            "nickname" to nickname,
            "perso_level" to physicalForm,
            "on_board_complete" to true,
            "arm_level" to "easy",
            "leg_level" to "easy",
            "body_level" to "easy")

        val uid = authManager.getCurrentAuth().uid!!

        withContext(Dispatchers.IO){
            dbManager.saveProfileForm(userUpdate, uid )

            if (illneses.size == 0) {
                userLocalData.saveUserHasIllness(false)
            }else {
                userLocalData.saveUserHasIllness(true)
                val listOfId = mutableListOf<Int>()
                for (i in illneses) {
                    listOfId.add(i.id)
                }


                val map_ = mapOf("illnesses_id" to  listOfId)
                dbManager.saveUserWithIllness(uid, map_)

            }


        }

        emit(true)




    }

    suspend fun changeIllnesses ( illness: MutableList<Illness> ) : Flow<Boolean> = flow {
        val uid = authManager.getCurrentAuth().uid!!

        if (illness.size > 0) {

            withContext(Dispatchers.IO){

                val listOfId = mutableListOf<Int>()
                for (i in illness) {
                    listOfId.add(i.id)
                }


                val map_ = mapOf("illnesses_id" to  listOfId)
                dbManager.saveUserWithIllness(uid, map_)

            }
        }else {
            withContext(Dispatchers.IO) {
                dbManager.deleteAllIllnesses(uid)
            }

        }

        val user_has_illness_state = userLocalData.getUserHasIllness()

        if (user_has_illness_state != (illness.size != 0)){
            userLocalData.saveUserHasIllness(illness.size != 0 )
            dbManager.setUserHasIllness(uid, illness.size != 0)
        }

        emit(true)

    }

    suspend fun changeNickname (nickname: String ): Flow<Boolean> = flow {
        val uid = authManager.getCurrentAuth().uid!!
        withContext(Dispatchers.IO){

           val changing_ =  dbManager.changeNickname(uid, nickname)
            if (changing_) {
                userLocalData.saveUserName(nickname)
            }
        }

        emit(true)


    }

    fun editEnergyLevel (info: MutableMap<String, String>): Flow<Boolean> = flow {
        val uid = authManager.getCurrentAuth().uid!!

        dbManager.editEnergyLevel(uid, info.toMap())
        info.forEach{ key, value  ->
            when (key) {
                "arm" -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        userLocalData.saveArmEnergy(value)
                    }
                }
                "leg" -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        userLocalData.saveLegEnergy(value)
                    }
                }
                "body" -> {

                    viewModelScope.launch(Dispatchers.IO) {
                        userLocalData.saveTorsoEnergy(value)
                    }
                }
            }
        }

        emit(true)



    }

    suspend fun getBodyPartEnergy ( part_body: String):String?   {
        val uid = authManager.getCurrentAuth().uid!!
        val result = dbManager.getBodyPartEnergy(uid, part_body)
        return result
    }

    fun getUserAchievemnts ():Flow<Map<String, MutableList< Achievement>>> = flow{
        var result = mutableListOf<Achievement>()
        var old_result = mutableListOf<Achievement>()
        var cur_result = mutableListOf<Achievement>()
        val uid = authManager.getCurrentAuth().uid!!

        withContext(Dispatchers.IO){
            result = dbManager.fetchUsersAchiements(uid)
            withContext(Dispatchers.Main) {
                for (res in result ){
                    if (res.status == "current") {
                        cur_result.add(res)
                    }
                    else {
                        old_result.add(res)
                    }

                }
            }


        }
        emit(mapOf("old" to old_result, "current" to cur_result))
    }

    fun createAchiement (name: String, description: String, dificult: DifficultyAchievemnt, all_coins: String, all_steps: String): Flow<Boolean>   = flow {
        val new_achie = AddAchievment(
            name = name,
            description =  description,
            all_coins =  all_coins.toInt(),
            all_steps = all_steps.toInt(),
            cur_step = 0,
            earned_coins = 0,
            status = "current"
        )

        when (dificult) {
            DifficultyAchievemnt.EASY -> new_achie.dificulty = "easy"
            DifficultyAchievemnt.HARD -> new_achie.dificulty = "hard"
            DifficultyAchievemnt.SMERT -> new_achie.dificulty = "smert"

        }

        val uid = authManager.getCurrentAuth().uid!!
        withContext(Dispatchers.IO) {
            dbManager.createAchievemnt(uid, new_achie)
        }
        emit(true)
    }

    fun changeAchievementStatus ( achievemnt: String, status: String): Flow <Boolean> = flow {
        val uid = authManager.getCurrentAuth().uid!!
        var result = false
        withContext(Dispatchers.IO) {
            result = dbManager.changeAchievementStatus(uid, status, achievemnt)

        }
        if (result) emit(true)
        else emit(false)

    }

    suspend fun getObBoradingState(): Boolean{
        val uid = authManager.getCurrentAuth().uid
        if (uid != null) {
            val res = dbManager.checkOnBoardingState(uid)
            if (res == OnBoardingStatesClass.Completed) {
                return true
            }
            else {
                return false
            }
        }
        return false


    }



}


