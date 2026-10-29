package com.stride.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R

/** Map of the places where habits are completed. The real map is added in a later task. */
class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        BottomNav.setup(this, NavTab.MAP)
        findViewById<View>(R.id.pinInfoCard).setOnClickListener {
            startActivity(Intent(this, HabitDetailActivity::class.java))
        }
    }
}
