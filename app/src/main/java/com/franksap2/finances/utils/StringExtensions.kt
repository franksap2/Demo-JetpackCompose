package com.franksap2.finances.utils


import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Float.formatToCurrency(): String {
    return String.format("%.2f $", this)
}

fun Float.formatToPercent(): String {
    return String.format("%.2f %%", this)
}

fun Float.formatToPercentOneDecimal(): String {
    return String.format("%.1f %%", this)
}

fun Float.formatToPercentWithoutDecimals(): String {
    return String.format("%d %%", this.toInt())
}


fun Float?.formatToDecimals(): String {
    return String.format(Locale.US, "%.2f", this ?: 0f)
}

fun String.encode(): String = URLEncoder.encode(this, "utf-8")

fun Char?.toSafeFloat() = this.toString().toFloatOrNull() ?: 0f

fun Long.formatToChartDay(timeSelector: TimeSelector): String {
    val format = when (timeSelector) {
        TimeSelector.DAY -> "h:mm"
        TimeSelector.MONTH,
        TimeSelector.WEEK -> "MMM d, h:mm"
        else -> "d MMM, yyyy"
    }

    val simpleFormatter = SimpleDateFormat(format, Locale.US)
    simpleFormatter.timeZone = TimeZone.getTimeZone("GMT-4")
    return simpleFormatter.format(Date(this))
}