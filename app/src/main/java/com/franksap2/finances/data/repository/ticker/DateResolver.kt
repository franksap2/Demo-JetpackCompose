package com.franksap2.finances.data.repository.ticker

import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import com.franksap2.finances.utils.Contents
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

class DateResolver @Inject constructor(private val timeZone: TimeZone) {

    private val minMarketDate
        get() = Calendar.getInstance(timeZone).apply {
            applyDefaultHour()
        }

    private val defaultCalendarInstance = Calendar.getInstance(timeZone)

    private val currentDate get() = Calendar.getInstance(timeZone)

    fun toQueryCalendar(selector: TimeSelector): Pair<Calendar, String> = selector.run {

        val calendar = Calendar.getInstance(timeZone)

        return when (this) {
            TimeSelector.DAY -> calendar.applyDayQueryValidDate() to Contents.FIVE_MIN_RESOLUTION

            TimeSelector.WEEK -> calendar.apply {
                add(Calendar.WEEK_OF_MONTH, -1)
                applyDefaultHour()
            } to Contents.HALF_HOUR_RESOLUTION

            TimeSelector.MONTH -> calendar.apply {
                add(Calendar.MONTH, -1)
                applyDefaultHour()
            } to Contents.HOUR_RESOLUTION

            TimeSelector.THREE_MONTHS -> calendar.apply {
                add(Calendar.MONTH, -3)
                applyDefaultHour()
            } to Contents.DAY_RESOLUTION

            TimeSelector.YEAR -> calendar.apply {
                add(Calendar.YEAR, -1)
                applyDefaultHour()
            } to Contents.DAY_RESOLUTION

            TimeSelector.FIVE_YEARS -> calendar.apply {
                add(Calendar.YEAR, -5)
                applyDefaultHour()
            } to Contents.WEEK_RESOLUTION
        }
    }

    fun remainTime(currentDate: Calendar, lastCandleTime: Long): Float {
        val minMarketTime = Calendar.getInstance(timeZone).apply { timeInMillis = currentDate.timeInMillis }

        currentDate.applyDefaultCloseAfterMarketHour()
        minMarketTime.applyDefaultPreMarketHour()

        return (lastCandleTime - minMarketTime.timeInMillis.toFloat()) / 39600000F
    }

    fun isMarketOpen(from: Calendar): Boolean {
        val closeMarket = Calendar.getInstance(timeZone).apply {
            timeInMillis = from.timeInMillis
            applyDefaultCloseMarketHour()
        }
        return currentDate.before(closeMarket)
    }


    fun isAfterMarket(timeStamp: Long): Boolean {
        val calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = timeStamp
        }

        val afterMarket = Calendar.getInstance(timeZone).apply {
            timeInMillis = timeStamp
            applyDefaultCloseMarketHour()
        }

        return calendar.after(afterMarket)
    }

    fun isPreMarket(timeStamp: Long): Boolean {
        val calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = timeStamp
        }

        val openMarket = Calendar.getInstance(timeZone).apply {
            timeInMillis = timeStamp
            applyDefaultHour()
        }

        return calendar.before(openMarket)
    }

    fun getDayFromTimeStamp(time: Long) = defaultCalendarInstance.apply { timeInMillis = time }.get(Calendar.DAY_OF_YEAR)

    private fun Calendar.applyDayQueryValidDate(): Calendar {

        if (before(minMarketDate)) {
            add(Calendar.DAY_OF_WEEK, -1)
        }

        when (get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY -> add(Calendar.DAY_OF_WEEK, -1)
            Calendar.SUNDAY -> add(Calendar.DAY_OF_WEEK, -2)
        }
        applyDefaultPreMarketHour()
        return this
    }

    private fun Calendar.applyDefaultHour() {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
    }

    private fun Calendar.applyDefaultPreMarketHour() {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    private fun Calendar.applyDefaultCloseMarketHour(): Calendar {
        set(Calendar.HOUR_OF_DAY, 16)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        return this
    }

    private fun Calendar.applyDefaultCloseAfterMarketHour(): Calendar {
        set(Calendar.HOUR_OF_DAY, 20)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        return this
    }

    fun getToDate(fromTimeInMillis: Long, timeSelector: TimeSelector): Calendar {
        return if (timeSelector == TimeSelector.DAY) {
            currentDate.apply {
                timeInMillis = fromTimeInMillis
                applyDefaultCloseAfterMarketHour()
            }
        } else {
            currentDate
        }
    }
}