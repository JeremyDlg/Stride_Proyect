package com.stride.app.domain.repository

import com.stride.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/** Local user profile (name, email and photo). */
interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile?>
    suspend fun getProfile(): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
    suspend fun clearProfile()
}
