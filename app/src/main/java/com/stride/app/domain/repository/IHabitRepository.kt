package com.stride.app.domain.repository

import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitLog
import java.time.LocalDate

interface IHabitRepository {
    fun getHabits(): List<Habit>
    fun getHabitById(id: Int): Habit?
    fun addHabit(habit: Habit)
    fun updateHabit(habit: Habit)
    fun deleteHabit(id: Int)
    fun getLogs(): List<HabitLog>
    fun getLogsByHabit(habitId: Int): List<HabitLog>
    fun setCompleted(habitId: Int, date: LocalDate, completed: Boolean)

}
