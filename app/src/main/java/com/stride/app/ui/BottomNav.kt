package com.stride.app.ui

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.stride.app.R

enum class NavTab { HOME, MAP, PROFILE }

/** Wires the bottom navigation bar shared by Home, Map and Profile. */
object BottomNav {

    fun setup(activity: Activity, selected: NavTab) {
        val home = activity.findViewById<android.view.View>(R.id.navHome)
        val map = activity.findViewById<android.view.View>(R.id.navMap)
        val profile = activity.findViewById<android.view.View>(R.id.navProfile)

        val accent = ContextCompat.getColor(activity, R.color.stride_accent_light)
        val muted = ContextCompat.getColor(activity, R.color.stride_muted)
        activity.findViewById<ImageView>(R.id.navHomeIcon)
            .setColorFilter(if (selected == NavTab.HOME) accent else muted)
        activity.findViewById<ImageView>(R.id.navMapIcon)
            .setColorFilter(if (selected == NavTab.MAP) accent else muted)

        when (selected) {
            NavTab.HOME -> home.setBackgroundResource(R.drawable.bg_nav_item_selected)
            NavTab.MAP -> map.setBackgroundResource(R.drawable.bg_nav_item_selected)
            NavTab.PROFILE -> profile.setBackgroundResource(R.drawable.bg_nav_item_selected)
        }

        home.setOnClickListener { if (selected != NavTab.HOME) goHome(activity) }
        map.setOnClickListener { if (selected != NavTab.MAP) open(activity, MapActivity::class.java, selected) }
        profile.setOnClickListener {
            if (selected != NavTab.PROFILE) open(activity, ProfileActivity::class.java, selected)
        }
    }

    private fun goHome(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }

    private fun open(activity: Activity, target: Class<*>, from: NavTab) {
        activity.startActivity(Intent(activity, target))
        // Keep Home in the back stack, but do not stack Map and Profile on top of each other.
        if (from != NavTab.HOME) activity.finish()
    }
}
