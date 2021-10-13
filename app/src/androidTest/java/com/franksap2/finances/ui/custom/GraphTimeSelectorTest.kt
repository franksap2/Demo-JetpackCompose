package com.franksap2.finances.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import com.franksap2.finances.ui.stockdetail.ui.GraphTimeSelector
import com.franksap2.finances.utils.assertColor

import org.junit.Test

import org.junit.Rule


class GraphTimeSelectorTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun graphTimeSelector_whenOneDaySelected() {

        composeRule.setContent {
            GraphTimeSelector(
                onItemSelected = {}
            )
        }

        composeRule
            .onNodeWithText("1D")
            .assertColor(Color(0xFF6200EE))
            .assertExists()

        composeRule
            .onNodeWithText("1W")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("1M")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("3M")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("1Y")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("5Y")
            .assertExists()
            .assertColor(Color.Transparent)

    }

    @Test
    fun graphTimeSelector_whenFiveYearsSelected() {

        composeRule.setContent {
            GraphTimeSelector(
                defaultSelection = TimeSelector.FIVE_YEARS,
                onItemSelected = { }
            )
        }

        composeRule
            .onNodeWithText("1D")
            .assertColor(Color.Transparent)
            .assertExists()

        composeRule
            .onNodeWithText("1W")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("1M")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("3M")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("1Y")
            .assertExists()
            .assertColor(Color.Transparent)

        composeRule
            .onNodeWithText("5Y")
            .assertExists()
            .assertColor(Color(0xFF6200EE))

    }
}