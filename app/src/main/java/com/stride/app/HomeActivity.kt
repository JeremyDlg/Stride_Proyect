package com.stride.app

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.Util

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnAddHabit = findViewById<ImageView>(R.id.addHabitButton)
        val navHome = findViewById<View>(R.id.navHome)
        val navMap = findViewById<View>(R.id.navMap)
        val navProfile = findViewById<View>(R.id.navProfile)

        navHome.setBackgroundResource(R.drawable.bg_nav_item_selected)

        btnAddHabit.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, HabitFormActivity::class.java)
        })

        navMap.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, MapActivity::class.java)
        })

        navProfile.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, ProfileActivity::class.java)
        })
    }
}
