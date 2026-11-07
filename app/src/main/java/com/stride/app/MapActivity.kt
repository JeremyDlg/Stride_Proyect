package com.stride.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.Util

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val infoCard = findViewById<View>(R.id.pinInfoCard)
        val navHome = findViewById<View>(R.id.navHome)
        val navMap = findViewById<View>(R.id.navMap)
        val navProfile = findViewById<View>(R.id.navProfile)

        navMap.setBackgroundResource(R.drawable.bg_nav_item_selected)

        infoCard.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, HabitDetailActivity::class.java)
        })

        navHome.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        navProfile.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, ProfileActivity::class.java)
            finish()
        })
    }
}
