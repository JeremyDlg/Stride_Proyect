package com.stride.app.domain.model

import java.time.LocalDate

/** Visual state of one cell in the monthly calendar. */
enum class DayState {

    EMPTY,

    DONE,

    MISSED,

    PENDING
}

/** One cell of the 7-column calendar grid. [date] is null for [DayState.EMPTY] cells. */
data class CalendarDay(
    val date: LocalDate?,
    val state: DayState,
    val isToday: Boolean = false
)
