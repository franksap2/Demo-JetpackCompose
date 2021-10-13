package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import com.franksap2.finances.ui.theme.Shapes
import com.franksap2.finances.utils.backgroundColor


private val timeSelectorData = listOf(
    TimeSelector.DAY to R.string.one_day,
    TimeSelector.WEEK to R.string.week,
    TimeSelector.MONTH to R.string.month,
    TimeSelector.THREE_MONTHS to R.string.three_months,
    TimeSelector.YEAR to R.string.one_year,
    TimeSelector.FIVE_YEARS to R.string.five_years
)

@Composable
fun GraphTimeSelector(
    modifier: Modifier = Modifier,
    defaultSelection: TimeSelector = TimeSelector.DAY,
    onItemSelected: (TimeSelector) -> Unit
) {

    var currentSelection by rememberSaveable { mutableStateOf(defaultSelection.ordinal) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)

    ) {

        timeSelectorData.forEachIndexed { position, item ->

            val backgroundColor: Color
            val textColor: Color

            if (currentSelection == position) {
                backgroundColor = MaterialTheme.colors.primary
                textColor = MaterialTheme.colors.onPrimary
            } else {
                backgroundColor = colorResource(id = android.R.color.transparent)
                textColor = MaterialTheme.colors.onBackground
            }

            val backgroundTargetColor = animateColorAsState(backgroundColor, tween(400))
            val textTargetColor = animateColorAsState(textColor, tween(400))

            Text(
                text = stringResource(id = item.second),
                color = textTargetColor.value,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(
                        remember { MutableInteractionSource() },
                        null
                    ) {
                        if (currentSelection != position) {
                            currentSelection = position
                            onItemSelected(item.first)
                        }
                    }
                    .weight(1f)
                    .background(
                        backgroundTargetColor.value,
                        shape = Shapes.small
                    )
                    .padding(5.dp)
                    .semantics {
                        this.backgroundColor = backgroundTargetColor.value
                    }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    GraphTimeSelector { }
}