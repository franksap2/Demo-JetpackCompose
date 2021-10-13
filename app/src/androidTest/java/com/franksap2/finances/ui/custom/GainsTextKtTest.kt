package com.franksap2.finances.ui.custom

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.franksap2.finances.ui.stockdetail.ui.GainsText
import org.junit.Rule
import org.junit.Test

class GainsTextKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun useAppContext() {

        composeRule.setContent {
            GainsText(
                previousSessionClosedPrice = 100f,
                currentPrice = 120f
            )
        }

        composeRule
            .onNodeWithText("20,00 %")
            .assertExists()

        composeRule
            .onNodeWithText("20,00 $")
            .assertExists()

    }

}