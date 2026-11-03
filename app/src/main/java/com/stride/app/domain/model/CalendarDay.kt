package com.stride.app.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate?,
    val state: DayState,
    val isToday: Boolean

)
