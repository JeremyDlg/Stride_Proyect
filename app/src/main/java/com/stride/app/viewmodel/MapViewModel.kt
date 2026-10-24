package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stride.app.domain.model.MapPin
import com.stride.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MapUiState(
    val pins: List<MapPin> = emptyList(),
    val selectedPin: MapPin? = null
)

/** Map screen: one pin for every habit that has a location. */
class MapViewModel(repository: HabitRepository) : ViewModel() {

    private val selectedHabitId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<MapUiState> = combine(
        repository.observeHabits(),
        repository.observeLogs(),
        selectedHabitId
    ) { habits, logs, selectedId ->
        val pins = habits.filter { it.hasLocation }.map { habit ->
            MapPin(habit, logs.count { it.habitId == habit.id && it.completed })
        }
        MapUiState(pins, pins.firstOrNull { it.habit.id == selectedId })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MapUiState())

    fun onPinSelected(habitId: Long) {
        selectedHabitId.value = habitId
    }

    fun onSelectionCleared() {
        selectedHabitId.value = null
    }
}
