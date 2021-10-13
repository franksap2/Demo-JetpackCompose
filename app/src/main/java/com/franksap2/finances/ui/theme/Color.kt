package com.franksap2.finances.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Red500 = Color(0xFFFF5001)
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffbc0000)

val Green200 = Color(0xFF38bc5b)
val Green500 = Color(0xFF38bc5b)
val Green700 = Color(0xFF008b2e)

internal val PositiveColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green200,
    secondaryVariant = Green700,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

internal val PositiveColorDarkPalette = darkColors(
    primary = Green500,
    onPrimary = Color.White,
    onSecondary = Color.White,
    primaryVariant = Green700,
    secondary = Green200,
    secondaryVariant = Green700,
)

internal val NegativeColorPalette = lightColors(
    primary = Red500,
    primaryVariant = Red900,
    secondary = Red500,
    secondaryVariant = Red900,
    error = Red800,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

internal val NegativeColorDarkPalette = darkColors(
    primary = Red500,
    primaryVariant = Red900,
    secondary = Red500,
    secondaryVariant = Red900,
    error = Red800,
    onPrimary = Color.White,
    onSecondary = Color.White,
)