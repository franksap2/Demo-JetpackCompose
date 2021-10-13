package com.franksap2.finances.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.isSelected


fun SemanticsNodeInteraction.assertColor(
    color : Color
): SemanticsNodeInteraction = assert(
    SemanticsMatcher.expectValue(backgroundColor, color)
)
