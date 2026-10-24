package com.stride.app.util

import com.stride.app.domain.model.CalendarDay
import com.stride.app.domain.model.DayState
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

object DateUtils {

    fun today(): LocalDate = LocalDate.now()

    /**
     * Builds the cells of a 7-column calendar (Monday first) for [month].
     * Empty cells are added before day 1 so the weekdays line up.
     *
     * @param trackingStart days before this date are not marked (the habit did not exist yet).
     */
    fun buildMonthGrid(
        month: YearMonth,
        completedDates: Set<LocalDate>,
        today: LocalDate,
        trackingStart: LocalDate
    ): List<CalendarDay> {
        val cells = mutableListOf<CalendarDay>()
        val offset = month.atDay(1).dayOfWeek.value - 1 // Monday = 1
        repeat(offset) { cells.add(CalendarDay(null, DayState.EMPTY)) }

        for (day in 1..month.lengthOfMonth()) {
            val date = month.atDay(day)
            val state = when {
                date in completedDates -> DayState.DONE
                date.isBefore(trackingStart) || !date.isBefore(today) -> DayState.PENDING
                else -> DayState.MISSED
            }
            cells.add(CalendarDay(date, state, isToday = date == today))
        }
        return cells
    }

    fun formatCoordinates(latitude: Double, longitude: Double): String =
        String.format(Locale.US, "%.4f, %.4f", latitude, longitude)
}
