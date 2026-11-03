package com.stride.app.domain.repository

import com.stride.app.domain.model.UserProfile

interface IProfileRepository {
    fun getProfile(): UserProfile?
    fun saveProfile(profile: UserProfile)
    fun deleteProfile()

}
