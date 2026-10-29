package com.stride.app.domain.model

import java.time.LocalDate

/** Record of a habit for a specific day. */
data class HabitLog(
    val id: Long = 0L,
    val habitId: Long,
    val date: LocalDate,
    val completed: Boolean
)
