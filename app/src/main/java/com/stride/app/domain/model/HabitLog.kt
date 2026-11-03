package com.stride.app.domain.model

import java.time.LocalDate

data class HabitLog(
    val id: Int,
    val habitId: Int,
    val date: LocalDate,
    val completed: Boolean

)
