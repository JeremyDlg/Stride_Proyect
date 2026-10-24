package com.stride.app.domain.model

/** A habit with a location, shown as a pin on the map. */
data class MapPin(
    val habit: Habit,
    val timesCompleted: Int
)
