package com.stride.app.domain.repository

import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitLog
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/** CRUD operations for habits and their daily logs. */
interface HabitRepository {
    fun observeHabits(): Flow<List<Habit>>
    fun observeLogs(): Flow<List<HabitLog>>
    fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>>

    suspend fun getHabit(id: Long): Habit?
    suspend fun addHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(id: Long)

    /** Creates or updates the log of [habitId] for [date]. */
    suspend fun setCompleted(habitId: Long, date: LocalDate, completed: Boolean)
}
