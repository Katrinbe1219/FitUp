package com.example.fitup.hilt

import android.net.Uri
import android.util.Log
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
class EnergyViewModel  @Inject constructor(

) : ViewModel() {
    private val _states = MutableStateFlow<Map<String, Boolean>>( emptyMap())
    val states = _states.asStateFlow()


    data class UiState(
        val states: Map<String, Boolean> = emptyMap(),
        val changes:Map<String, String> = emptyMap()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val changes: StateFlow< Map<String, String> > = _uiState.map { it.changes }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())



    //1) добавляем под контроль все картинки


    fun addImagesToControl(images: Map<String, Uri>) {
        _uiState.update { prev ->

            val newStates = images.keys.associateWith { false } + ("left_leg" to false)
            prev.copy(states = newStates)
        }
    }

    // 2) ищменяем состояние опредедленной картинки

    fun updateState(image: String, ready: Boolean) {
        _uiState.update { prev ->
            val updatedStates = prev.states.toMutableMap().apply {
                this[image] = ready
            }
            prev.copy(states = updatedStates) // changes stays the same
        }
    }

    //3) наблюдаем за всеми картинками


    val allImagesReady: StateFlow<Boolean> = _uiState
        .map { it.states.values.isNotEmpty() && it.states.values.all { ready -> ready } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )





    fun updateStates(listImages: MutableMap<String, String>, ready: Boolean) {

        _uiState.update { prev ->
            val updatedStates = prev.states.toMutableMap().apply {
                for (i in listImages){
                    this[i.key] = ready
                }
            }

            val updatedChanges = prev.changes.toMutableMap().apply {
                for (i in listImages){
                    this[i.key] = i.value
                }
            }
            prev.copy(states = updatedStates, changes = updatedChanges) // changes stays the same
        }
    }
}