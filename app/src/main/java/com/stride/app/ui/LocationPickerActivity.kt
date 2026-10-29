package com.stride.app.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R
import com.stride.app.util.DateUtils
import com.stride.app.util.MapDefaults

/** Lets the user choose the location of a habit on the map. */
class LocationPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        findViewById<TextView>(R.id.coordinatesText).text =
            DateUtils.formatCoordinates(MapDefaults.LATITUDE, MapDefaults.LONGITUDE)

        findViewById<View>(R.id.cancelButton).setOnClickListener { finish() }
        findViewById<View>(R.id.saveLocationButton).setOnClickListener {
            Toast.makeText(this, R.string.toast_location_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
