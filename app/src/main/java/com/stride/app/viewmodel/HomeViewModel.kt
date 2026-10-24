package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stride.app.domain.model.HabitFilter
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.domain.model.HabitWithStatus
import com.stride.app.domain.repository.HabitRepository
import com.stride.app.util.DateUtils
import com.stride.app.util.HabitStatsCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val items: List<HabitWithStatus> = emptyList(),
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val filter: HabitFilter = HabitFilter.ALL,
    val query: String = ""
)

/** Home screen: today's habits with search and filters. */
class HomeViewModel(private val repository: HabitRepository) : ViewModel() {

    private val filter = MutableStateFlow(HabitFilter.ALL)
    private val query = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        repository.observeHabits(),
        repository.observeLogs(),
        filter,
        query
    ) { habits, logs, selectedFilter, searchText ->
        val today = DateUtils.today()
        val all = habits.map { habit ->
            val completedDates = logs
                .filter { it.habitId == habit.id && it.completed }
                .map { it.date }
                .toSet()
            HabitWithStatus(
                habit = habit,
                isCompletedToday = today in completedDates,
                currentStreak = HabitStatsCalculator.currentStreak(completedDates, today)
            )
        }
        val visible = all.filter {
            matchesFilter(it, selectedFilter) &&
                it.habit.name.contains(searchText.trim(), ignoreCase = true)
        }
        HomeUiState(
            items = visible,
            completedToday = all.count { it.isCompletedToday },
            totalHabits = all.size,
            filter = selectedFilter,
            query = searchText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onFilterSelected(newFilter: HabitFilter) = filter.update { newFilter }

    fun onQueryChanged(text: String) = query.update { text }

    /** Marks the habit as done today, or undoes it if it was already done. */
    fun onToggleHabit(habitId: Long) {
        viewModelScope.launch {
            val current = uiState.value.items
                .firstOrNull { it.habit.id == habitId }?.isCompletedToday ?: false
            repository.setCompleted(habitId, DateUtils.today(), !current)
        }
    }

    private fun matchesFilter(item: HabitWithStatus, filter: HabitFilter): Boolean =
        when (filter) {
            HabitFilter.ALL -> true
            HabitFilter.PENDING -> !item.isCompletedToday
            HabitFilter.DONE -> item.isCompletedToday
            HabitFilter.DAILY -> item.habit.frequency == HabitFrequency.DAILY
            HabitFilter.WEEKLY -> item.habit.frequency == HabitFrequency.WEEKLY
        }
}
