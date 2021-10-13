package com.franksap2.finances.utils

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.franksap2.finances.ui.stocklist.StockList
import com.franksap2.finances.ui.theme.FinancesTheme

fun Fragment.createComposeView(
    content: @Composable () -> Unit
) = ComposeView(requireContext()).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent(content)
}