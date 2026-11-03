package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import com.stride.app.data.memory.MemoryDataRepository
import com.stride.app.domain.model.UserProfile
import com.stride.app.domain.repository.IProfileRepository


class ProfileViewModel : ViewModel() {
    companion object {
        private var repository: IProfileRepository

        init {
            repository = MemoryDataRepository()
        }

        fun getProfile(): UserProfile? {
            return repository.getProfile()
        }

        fun saveProfile(name: String, email: String, photoUri: String) {
            repository.saveProfile(UserProfile(name.trim(), email.trim(), photoUri))
        }

        fun logout() {
            repository.deleteProfile()
        }
    }
}
