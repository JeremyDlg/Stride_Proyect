package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import com.stride.app.data.memory.MemoryDataRepository
import com.stride.app.domain.model.CalendarDay
import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitFilter
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.domain.repository.IHabitRepository
import com.stride.app.util.Util
import java.time.LocalDate


class HabitViewModel : ViewModel() {
    companion object {
        private var repository: IHabitRepository

        init {
            repository = MemoryDataRepository()
        }

        fun getHabits(): List<Habit> {
            return repository.getHabits()
        }

        fun getHabitById(id: Int): Habit? {
            return repository.getHabitById(id)
        }

        fun saveHabit(habit: Habit) {
            repository.addHabit(habit)
        }

        fun updateHabit(habit: Habit) {
            repository.updateHabit(habit)
        }

        fun removeHabit(id: Int) {
            repository.deleteHabit(id)
        }

        fun getFilteredHabits(filter: HabitFilter, search: String): List<Habit> {
            val result = mutableListOf<Habit>()
            for (habit in repository.getHabits()) {
                var matches = false
                when (filter) {
                    HabitFilter.ALL -> matches = true
                    HabitFilter.PENDING -> matches = !isCompletedToday(habit.id)
                    HabitFilter.DONE -> matches = isCompletedToday(habit.id)
                    HabitFilter.DAILY -> matches = habit.frequency == HabitFrequency.DAILY
                    HabitFilter.WEEKLY -> matches = habit.frequency == HabitFrequency.WEEKLY
                }
                if (matches && habit.name.uppercase().contains(search.trim().uppercase())) {
                    result.add(habit)
                }
            }
            return result
        }

        fun isCompletedToday(habitId: Int): Boolean {
            return getCompletedDates(habitId).contains(LocalDate.now())
        }

        fun toggleToday(habitId: Int) {
            val done = isCompletedToday(habitId)
            repository.setCompleted(habitId, LocalDate.now(), !done)
        }

        fun countCompletedToday(): Int {
            var count = 0
            for (habit in repository.getHabits()) {
                if (isCompletedToday(habit.id)) {
                    count++
                }
            }
            return count
        }

        fun getCurrentStreak(habitId: Int): Int {
            return Util.getCurrentStreak(getCompletedDates(habitId), LocalDate.now())
        }

        fun getBestStreak(habitId: Int): Int {
            return Util.getBestStreak(getCompletedDates(habitId))
        }

        fun getMonthPercent(habitId: Int, month: LocalDate): Int {
            val habit = repository.getHabitById(habitId)

            if (habit == null) {
                return 0
            }
            return Util.getMonthPercent(getCompletedDates(habitId), month,
                habit.createdAt, LocalDate.now())

        }

        fun getCalendar(habitId: Int, month: LocalDate): List<CalendarDay> {
            val habit = repository.getHabitById(habitId)
            var startDate = LocalDate.now()

            if (habit != null) {
                startDate = habit.createdAt
            }

            return Util.buildMonthGrid(month, getCompletedDates(habitId),
                LocalDate.now(), startDate)

        }

        fun getHabitsWithLocation(): List<Habit> {
            val result = mutableListOf<Habit>()

            for (habit in repository.getHabits()) {
                if (habit.hasLocation()) {
                    result.add(habit)
                }
            }

            return result
        }

        fun getTimesCompleted(habitId: Int): Int {
            return getCompletedDates(habitId).size
        }

        private fun getCompletedDates(habitId: Int): List<LocalDate> {
            val dates = mutableListOf<LocalDate>()

            for (log in repository.getLogsByHabit(habitId)) {

                if (log.completed) {
                    dates.add(log.date)
                }
            }
            return dates

        }
    }
}
