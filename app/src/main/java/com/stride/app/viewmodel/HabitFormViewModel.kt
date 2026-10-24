package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.domain.repository.HabitRepository
import com.stride.app.util.DateUtils
import com.stride.app.util.DefaultColors
import com.stride.app.util.DefaultIcons
import com.stride.app.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HabitFormState(
    val name: String = "",
    val icon: String = DefaultIcons.DROP,
    val color: String = DefaultColors.BLUE,
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val nameError: Boolean = false,
    val isEditing: Boolean = false,
    val isSaved: Boolean = false
)

/** Create / edit habit screen. */
class HabitFormViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _state = MutableStateFlow(HabitFormState())
    val state: StateFlow<HabitFormState> = _state.asStateFlow()

    private var editingHabit: Habit? = null

    /** Loads an existing habit to edit it. Not called when creating a new one. */
    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            val habit = repository.getHabit(habitId) ?: return@launch
            editingHabit = habit
            _state.value = HabitFormState(
                name = habit.name,
                icon = habit.icon,
                color = habit.color,
                frequency = habit.frequency,
                placeName = habit.placeName,
                latitude = habit.latitude,
                longitude = habit.longitude,
                isEditing = true
            )
        }
    }

    fun onNameChanged(name: String) = _state.update { it.copy(name = name, nameError = false) }

    /** [icon] can be a built-in icon key or any emoji typed by the user. */
    fun onIconSelected(icon: String) = _state.update { it.copy(icon = icon) }

    /** Ignores values that are not a valid #RRGGBB color. */
    fun onColorSelected(color: String) {
        if (Validators.isValidHexColor(color)) _state.update { it.copy(color = color) }
    }

    fun onFrequencySelected(frequency: HabitFrequency) =
        _state.update { it.copy(frequency = frequency) }

    fun onLocationSelected(placeName: String, latitude: Double, longitude: Double) =
        _state.update {
            it.copy(
                placeName = placeName.ifBlank { null },
                latitude = latitude,
                longitude = longitude
            )
        }

    fun onLocationCleared() =
        _state.update { it.copy(placeName = null, latitude = null, longitude = null) }

    fun save() {
        val current = _state.value
        if (!Validators.isValidName(current.name)) {
            _state.update { it.copy(nameError = true) }
            return
        }
        viewModelScope.launch {
            val base = editingHabit
            val habit = Habit(
                id = base?.id ?: 0L,
                name = current.name.trim(),
                icon = current.icon,
                color = current.color,
                frequency = current.frequency,
                createdAt = base?.createdAt ?: DateUtils.today(),
                placeName = current.placeName,
                latitude = current.latitude,
                longitude = current.longitude
            )
            if (base == null) repository.addHabit(habit) else repository.updateHabit(habit)
            _state.update { it.copy(isSaved = true) }
        }
    }
}
