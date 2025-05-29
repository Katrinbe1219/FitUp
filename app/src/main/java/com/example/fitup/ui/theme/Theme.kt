package com.example.fitup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fitup.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val MyLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000), // Черный
    secondary = Color(0xFFFFC300), // Желтый
    tertiary = Color(0xFFFFA500), // Оранжевый
    background = Color(0xFFFFFFFF), // Белый
    surface = Color(0xFFFFEA00), // Светло-желтый
    onPrimary = Color(0xFFFFFFFF), // Белый
    onSecondary = Color(0xFF000000), // Черный
    onBackground = Color(0xFF000000), // Черный
    onSurface = Color(0xFF000000), // Черный

)

// Dark Color Scheme
private val MyDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700), // Золотисто-желтый   - легко
    secondary = Color(0xFFFFC300), // Желтый
    tertiary = Color(0xFFFFA500), // Оранжевый      -              // easy
    background = Color(0xFF1A1A1A), // Темно-серый
    surface =Color(0xFF3A2025), // Черный // смерть                     //  bad мышцам конец
    onPrimary = Color(0xFF000000), // Черный
    onSecondary = Color(0xFF000000), // Черный
    onBackground =Color(0xFFF0DCD7), // молочный
    onSurface = Color(0xFFFFFFFF), // Белый
    surfaceVariant = Color(0xFF93351D)    // тяжело                      //  good аларм

)

private val MyFontFamily = FontFamily(
    Font(resId = R.font.widock)
)

private val MyCustomTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = MyFontFamily,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyLarge = TextStyle(
        fontFamily = MyFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),

)

@Composable
fun FitUpTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme()
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme()
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme_ = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> MyDarkColorScheme
        else -> MyLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme_,
        typography = MyCustomTypography,
        content = content
    )
}