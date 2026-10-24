package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stride.app.domain.model.UserProfile
import com.stride.app.domain.repository.ProfileRepository
import com.stride.app.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ProfileError { INVALID_NAME, INVALID_EMAIL }

/** Handles the local login screen and the profile screen. */
class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    /** Null means there is no profile yet, so the login screen must be shown. */
    val profile: StateFlow<UserProfile?> = repository.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _error = MutableStateFlow<ProfileError?>(null)
    val error: StateFlow<ProfileError?> = _error.asStateFlow()

    /** Validates and saves the profile. Returns true when it was saved. */
    fun saveProfile(name: String, email: String): Boolean {
        val validation = when {
            !Validators.isValidName(name) -> ProfileError.INVALID_NAME
            !Validators.isValidEmail(email) -> ProfileError.INVALID_EMAIL
            else -> null
        }
        _error.value = validation
        if (validation != null) return false

        viewModelScope.launch {
            val photo = repository.getProfile()?.photoUri
            repository.saveProfile(UserProfile(name.trim(), email.trim(), photo))
        }
        return true
    }

    fun updatePhoto(photoUri: String?) {
        viewModelScope.launch {
            repository.getProfile()?.let { repository.saveProfile(it.copy(photoUri = photoUri)) }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun logout() {
        viewModelScope.launch { repository.clearProfile() }
    }
}
