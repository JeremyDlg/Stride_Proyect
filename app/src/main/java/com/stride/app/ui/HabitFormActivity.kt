package com.stride.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R

/** Create / edit habit form. */
class HabitFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        findViewById<View>(R.id.cancelButton).setOnClickListener { finish() }
        findViewById<View>(R.id.placeRow).setOnClickListener {
            startActivity(Intent(this, LocationPickerActivity::class.java))
        }
        findViewById<View>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, R.string.toast_habit_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
