package com.franksap2.finances.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext


@Composable
fun FinancesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isNegative: Boolean,
    content: @Composable () -> Unit
) {

    val colors = when {
        isNegative && !darkTheme -> NegativeColorPalette
        isNegative && darkTheme -> NegativeColorDarkPalette
        darkTheme -> PositiveColorDarkPalette
        else -> PositiveColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content
    )
}