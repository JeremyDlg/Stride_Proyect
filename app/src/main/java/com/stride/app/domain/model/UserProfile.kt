package com.stride.app.domain.model

/** Local profile stored on the device. */
data class UserProfile(
    val name: String,
    val email: String,
    val photoUri: String? = null
)
