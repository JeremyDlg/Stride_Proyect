package com.stride.app.viewmodel

import androidx.lifecycle.ViewModel
import com.stride.app.util.MapDefaults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationPickerState(
    val latitude: Double = MapDefaults.LATITUDE,
    val longitude: Double = MapDefaults.LONGITUDE,
    val placeName: String = ""
)

/** "Choose location" screen: keeps the point selected on the map. */
class LocationPickerViewModel : ViewModel() {

    private val _state = MutableStateFlow(LocationPickerState())
    val state: StateFlow<LocationPickerState> = _state.asStateFlow()

    /** Starts from the habit's current location when editing. */
    fun setInitialLocation(latitude: Double?, longitude: Double?, placeName: String?) {
        if (latitude != null && longitude != null) {
            _state.value = LocationPickerState(latitude, longitude, placeName.orEmpty())
        }
    }

    fun onPinMoved(latitude: Double, longitude: Double) =
        _state.update { it.copy(latitude = latitude, longitude = longitude) }

    fun onPlaceNameChanged(name: String) = _state.update { it.copy(placeName = name) }
}
