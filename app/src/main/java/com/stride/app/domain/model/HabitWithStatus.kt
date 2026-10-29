package com.stride.app.domain.model

/** A habit plus the information the home list needs to draw one row. */
data class HabitWithStatus(
    val habit: Habit,
    val isCompletedToday: Boolean,
    val currentStreak: Int
)
