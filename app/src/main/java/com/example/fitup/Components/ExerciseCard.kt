package com.example.fitup.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitup.items.Exercsice

@Composable
fun ExerciseCard(exInfo: Exercsice) {

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        colors = CardDefaults.cardColors(
            containerColor = exInfo.dificulty.returnColor(),
            contentColor = MaterialTheme.colorScheme.onSurface
        )

    ) {
        Column (
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center

        ){
            Text(text = exInfo.name,
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(fontSize = 18.sp,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    textAlign = TextAlign.Start),
            )
            HorizontalDivider(thickness = 2.dp, color = Color.Transparent)

            Text(text = "противопоказания: " + exInfo.medicalGrounds.conclude(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(fontSize = 10.sp,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    textAlign = TextAlign.Start),
            )
        }

    }

}