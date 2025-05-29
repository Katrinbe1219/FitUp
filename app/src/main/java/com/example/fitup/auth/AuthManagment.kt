package com.example.fitup.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitup.dataStore.OnBoardingStatesClass
import com.example.fitup.dataStore.OnBoardingViewModel.OnBoardingState
import com.example.fitup.hilt.DatabaseManager
import com.example.fitup.hilt.LocalViewModel
import com.example.fitup.items.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


sealed interface AuthResponse {
    data object Success : AuthResponse
    data class  Error ( val message: String? ) : AuthResponse
}

sealed interface AuthState {
    data object  Idle: AuthState
    data object Success : AuthState
    data class  Error ( val message: String? ) : AuthState
}

//AuthManager handles sing-in, sign-out, registration
// wraps firebaseAuth APIs
// does not hold UI state - just logic and emits results
class AuthManager @Inject constructor(
//   @ApplicationContext val context: Context,
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
){

    private val _onboardingState = mutableStateOf<OnBoardingState>(OnBoardingState.Incomplete)
    val onBoardingState: State<OnBoardingState> = _onboardingState



    fun setOnBoardComplete(value: OnBoardingState){
        _onboardingState.value = value
    }

    fun getCurrentAuth (): FirebaseAuth {
        return auth
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }



    fun signUpWithEmail_(emailVal : String, passwordVal: String)  = flow{
        try{
            auth.createUserWithEmailAndPassword(emailVal, passwordVal).await()
            emit(Result.success(Unit))

        }
        catch (e: Exception) {
            emit(Result.failure(e))
        }

    }.flowOn(Dispatchers.IO)

    fun signInWithEmail_(emailVal: String, passwordVal:String) = flow{
        try{
            auth.signInWithEmailAndPassword(emailVal, passwordVal).await()
            emit(Result.success(Unit))

        }
        catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun signOut(){
        auth.signOut()
    }


}

// for accessing hilt
// calls funs in AUthManager
// exposes ui-friendly state - loading, error, success
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val dbManager: DatabaseManager,
    private val localModel: LocalViewModel
) : ViewModel()
{

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState = _authState

    // ui after adding or not adding user, singIn or notSignIn
    fun signUpWithEmail(email:String, password: String): Flow<String> = flow {

            authManager.signUpWithEmail_(email, password).collect{ result ->
                withContext(Dispatchers.Main){
                    _authState.value =
                        if (result.isSuccess) AuthState.Success
                        else AuthState.Error(result.exceptionOrNull()?.message)
                }

                if (result.isSuccess){

                   val creatingUser =  dbManager.createUser(authManager.getCurrentUser()!!.uid )

                    if (creatingUser == DatabaseManager.QueryStatus.Incomplete){
                        emit("problems with adding to Database")
                    }
                    else if (creatingUser == DatabaseManager.QueryStatus.Completed){
                        emit("done")
                    }
                }
                else {
                    emit("${result.exceptionOrNull()?.message}")

                }
            }



    }.flowOn(Dispatchers.IO)

    fun signOut(){
        authManager.signOut()
    }
    fun getAuth ():FirebaseAuth {
        return authManager.getCurrentAuth()
    }

    // true, fals eor exception - is not signedIN
    fun signInWithEmail( email: String, password: String): Flow<Boolean> = flow {

            var resu: Boolean = false
        authManager.signInWithEmail_(email, password).collect{result ->
                withContext(Dispatchers.Main){
                    _authState.value =
                        if (result.isSuccess) AuthState.Success
                        else AuthState.Error(result.exceptionOrNull()?.message)
                }




                if (result.isSuccess){
                    withContext(Dispatchers.Main) {
                        resu = true
                    }
                }

            }


            if (resu) {
                val curOnBoardState =
                    withContext(Dispatchers.IO) {
                        dbManager.checkOnBoardingState(authManager.getCurrentUser()!!.uid)
                    }

                withContext(Dispatchers.Main){
                    localModel.saveOnBoardingState(curOnBoardState)
                }
                emit (curOnBoardState == OnBoardingStatesClass.Completed)
            }

        }



}