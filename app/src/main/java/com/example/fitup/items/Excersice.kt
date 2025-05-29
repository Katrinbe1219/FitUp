package com.example.fitup.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



enum class Dificulty(){
    BEGGINER,
    MEDIUM,
    ADVANCED,
    SPORTSMAN;

    @Composable
    fun returnColor():Color{
        return when(this) {
            BEGGINER -> MaterialTheme.colorScheme.primary
            MEDIUM -> MaterialTheme.colorScheme.secondary
            ADVANCED -> MaterialTheme.colorScheme.surfaceVariant
            SPORTSMAN ->MaterialTheme.colorScheme.surface
        }
    }


    fun returnFilter():String{
        return when(this) {
            BEGGINER -> "легко"
            MEDIUM -> "тяжко"
            ADVANCED -> "трудно"
            SPORTSMAN -> "смерть"
        }
    }
}

enum class MedicalGrounds{
    PROTRUSION, SPRAIN, NONE;

    fun conclude():String{
        return when(this){
            PROTRUSION -> "протрузия"
            SPRAIN -> "растяжение"
            NONE -> "отсутствуют"
        }
    }
}
data class Exercsice(
    val name: String,
    val dificulty: Dificulty,
    val medicalGrounds: MedicalGrounds,
    )