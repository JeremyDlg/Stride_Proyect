package com.stride.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.Util

class HabitFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        val btnCancel = findViewById<TextView>(R.id.cancelButton)
        val btnSave = findViewById<TextView>(R.id.saveButton)
        val placeRow = findViewById<View>(R.id.placeRow)

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        placeRow.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, LocationPickerActivity::class.java)
        })

        btnSave.setOnClickListener(View.OnClickListener { view ->
            Toast.makeText(this, getString(R.string.TextHabitSaved),
                Toast.LENGTH_LONG).show()
            finish()
        })

    }
}
