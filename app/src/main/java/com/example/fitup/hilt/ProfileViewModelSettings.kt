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
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _states = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val states = _states.asStateFlow()

    //1) adding elements to control
    fun addImagesToControl (images: Map<String, Uri>){
        _states.update {
            images.keys.associateWith { false } + ("left_leg" to false)
        }
    }

    //2) updateState
    fun updateState ( image: String, ready: Boolean) {
        _states.update { prev ->
            prev.toMutableMap().apply {
                this [image] = ready
            }
        }
    }

    //3) monitor when all images are downloaded and placed
    val allImagesReady: StateFlow<Boolean> =
        _states.map { it.values.isNotEmpty() && it.values.all { ready -> ready } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
}