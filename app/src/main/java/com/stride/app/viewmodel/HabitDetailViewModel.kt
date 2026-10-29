package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stride.app.domain.model.CalendarDay
import com.stride.app.domain.model.Habit
import com.stride.app.domain.repository.HabitRepository
import com.stride.app.util.DateUtils
import com.stride.app.util.HabitStatsCalculator
import java.time.YearMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HabitDetailUiState(
    val habit: Habit? = null,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val monthCompletionPercent: Int = 0,
    val month: YearMonth = YearMonth.now(),
    val days: List<CalendarDay> = emptyList(),
    val isDeleted: Boolean = false
)

/** Habit detail: streak, monthly completion and the 7-column calendar. */
@OptIn(ExperimentalCoroutinesApi::class)
class HabitDetailViewModel(private val repository: HabitRepository) : ViewModel() {

    private val habitId = MutableStateFlow<Long?>(null)
    private val month = MutableStateFlow(YearMonth.now())
    private val deleted = MutableStateFlow(false)

    private val habitFlow = habitId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else repository.observeHabits().map { list -> list.firstOrNull { it.id == id } }
    }

    private val logsFlow = habitId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.observeLogsForHabit(id)
    }

    val uiState: StateFlow<HabitDetailUiState> = combine(
        habitFlow, logsFlow, month, deleted
    ) { habit, logs, selectedMonth, isDeleted ->
        val today = DateUtils.today()
        val completedDates = logs.filter { it.completed }.map { it.date }.toSet()
        val trackingStart = habit?.createdAt ?: today
        HabitDetailUiState(
            habit = habit,
            currentStreak = HabitStatsCalculator.currentStreak(completedDates, today),
            bestStreak = HabitStatsCalculator.bestStreak(completedDates),
            monthCompletionPercent = HabitStatsCalculator.monthCompletionPercent(
                completedDates, selectedMonth, trackingStart, today
            ),
            month = selectedMonth,
            days = DateUtils.buildMonthGrid(selectedMonth, completedDates, today, trackingStart),
            isDeleted = isDeleted
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HabitDetailUiState())

    fun loadHabit(id: Long) {
        habitId.value = id
    }

    fun onPreviousMonth() = month.update { it.minusMonths(1) }

    /** Months in the future are not allowed. */
    fun onNextMonth() = month.update { if (it < YearMonth.now()) it.plusMonths(1) else it }

    fun deleteHabit() {
        val id = habitId.value ?: return
        viewModelScope.launch {
            repository.deleteHabit(id)
            deleted.value = true
        }
    }
}
