package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stride.app.data.memory.MemoryDataRepository

/**
 * Creates the ViewModels with the shared repository.
 * Usage: `private val viewModel: HomeViewModel by viewModels { ViewModelFactory() }`
 */
class ViewModelFactory(
    private val repository: MemoryDataRepository = MemoryDataRepository.instance
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository)
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
        modelClass.isAssignableFrom(HabitFormViewModel::class.java) -> HabitFormViewModel(repository)
        modelClass.isAssignableFrom(HabitDetailViewModel::class.java) -> HabitDetailViewModel(repository)
        modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(repository)
        modelClass.isAssignableFrom(LocationPickerViewModel::class.java) -> LocationPickerViewModel()
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    } as T
}
