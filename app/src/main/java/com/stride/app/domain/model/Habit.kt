package com.stride.app.domain.model

import java.time.LocalDate

/**
 * A habit created by the user.
 *
 * @property icon either an emoji typed by the user or the key of a built-in icon.
 * @property color hexadecimal color, example "#007AFF".
 */
data class Habit(
    val id: Long = 0L,
    val name: String,
    val icon: String,
    val color: String,
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val createdAt: LocalDate = LocalDate.now(),
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val hasLocation: Boolean
        get() = latitude != null && longitude != null
}
