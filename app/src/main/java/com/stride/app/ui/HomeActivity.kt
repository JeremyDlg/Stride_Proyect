package com.stride.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R

/** Today's habits. The list and the filters are connected to the ViewModel in the next task. */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        BottomNav.setup(this, NavTab.HOME)
        findViewById<View>(R.id.addHabitButton).setOnClickListener {
            startActivity(Intent(this, HabitFormActivity::class.java))
        }
    }
}
