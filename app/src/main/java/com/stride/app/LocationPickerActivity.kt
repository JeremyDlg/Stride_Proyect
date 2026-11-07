package com.stride.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.defaultLatitude
import com.stride.app.util.defaultLongitude
import com.stride.app.util.Util

class LocationPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        val btnCancel = findViewById<TextView>(R.id.cancelButton)
        val btnSave = findViewById<TextView>(R.id.saveLocationButton)
        val tvCoordinates = findViewById<TextView>(R.id.coordinatesText)

        tvCoordinates.text = Util.formatCoordinates(defaultLatitude, defaultLongitude)

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        btnSave.setOnClickListener(View.OnClickListener { view ->

            Toast.makeText(this, getString(R.string.TextLocationSaved),
                Toast.LENGTH_LONG).show()
            finish()

        })
    }
}
