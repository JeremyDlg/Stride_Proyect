package com.stride.app.domain.model

import java.time.LocalDate

data class Habit(
    var id: Int,
    val name: String,
    val icon: String,
    val color: String,
    val frequency: HabitFrequency,
    val createdAt: LocalDate,
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null

) {
    fun hasLocation(): Boolean {
        return latitude != null && longitude != null
    }
}
