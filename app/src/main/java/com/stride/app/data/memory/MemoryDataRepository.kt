package com.stride.app.data.memory

import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitLog
import com.stride.app.domain.model.UserProfile
import com.stride.app.domain.repository.IHabitRepository
import com.stride.app.domain.repository.IProfileRepository
import java.time.LocalDate

class MemoryDataRepository : IHabitRepository, IProfileRepository {
    private val habits = mutableListOf<Habit>()
    private val logs = mutableListOf<HabitLog>()
    private var profile: UserProfile? = null
    private var nextHabitId = 1
    private var nextLogId = 1



    override fun getHabits(): List<Habit> {
        return habits
    }



    override fun getHabitById(id: Int): Habit? {
        for (habit in habits) {
            if (habit.id == id) {
                return habit
            }
        }
        return null
    }



    override fun addHabit(habit: Habit) {
        habit.id = nextHabitId
        habits.add(habit)
        nextHabitId++
    }



    override fun updateHabit(habit: Habit) {
        for (i in habits.indices) {
            if (habits[i].id == habit.id) {
                habits[i] = habit
            }
        }
    }



    override fun deleteHabit(id: Int) {
        habits.removeIf { it.id == id }
        logs.removeIf { it.habitId == id }
    }



    override fun getLogs(): List<HabitLog> {
        return logs
    }



    override fun getLogsByHabit(habitId: Int): List<HabitLog> {
        return logs.filter { it.habitId == habitId }
    }



    override fun setCompleted(habitId: Int, date: LocalDate, completed: Boolean) {
        logs.removeIf { it.habitId == habitId && it.date == date }
        logs.add(HabitLog(nextLogId, habitId, date, completed))
        nextLogId++
    }



    override fun getProfile(): UserProfile? {
        return profile
    }



    override fun saveProfile(profile: UserProfile) {
        this.profile = profile
    }



    override fun deleteProfile() {
        profile = null
    }


}
