package com.example.fitup.hilt

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SplittingBodyViewModel  @Inject constructor(

) : ViewModel() {
    private val _states = MutableStateFlow<Map<String, Boolean>>( emptyMap())
    val states = _states.asStateFlow()



    //1) добавляем под контроль все картинки
    fun addImagesToControl (images: Map<String, Uri>){
        _states.update {
            images.keys.associateWith {  false } +
                    ("left_leg" to false)

        }

    }


    // 2) ищменяем состояние опредедленной картинки
    fun updateState (image: String, ready: Boolean) {
        _states.update { prev ->
            prev.toMutableMap().apply {
                this[image] = ready
            }
        }
    }

    //3) наблюдаем за всеми картинками
    // auto updates when _states is chaneged
    val allImagesReady: StateFlow<Boolean> =
        _states.map{ it.values.isNotEmpty() &&  it.values.all {ready -> ready}} // retrun true if all values are true
            .stateIn(
                scope = viewModelScope, // binds the Flow`s lifecycle to the VM, auto cancels when VM clears
                started = SharingStarted.WhileSubscribed(2000), // starts collecting when the first subcriber appers, and stops after last subscribers deisapperas 5 sec
                initialValue = false // the value emutted before the first actual value arrives
            )

}