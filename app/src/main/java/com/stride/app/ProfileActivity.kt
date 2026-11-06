package com.stride.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.Util

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val imgPhoto = findViewById<View>(R.id.profilePhoto)
        val btnLogout = findViewById<TextView>(R.id.logoutButton)
        val navHome = findViewById<View>(R.id.navHome)
        val navMap = findViewById<View>(R.id.navMap)
        val navProfile = findViewById<View>(R.id.navProfile)

        navProfile.setBackgroundResource(R.drawable.bg_nav_item_selected)

        imgPhoto.setOnClickListener(View.OnClickListener { view ->
            showPhotoDialog()
        })

        btnLogout.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, LoginActivity::class.java)
            finish()
        })

        navHome.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        navMap.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, MapActivity::class.java)
            finish()
        })

    }

    private fun showPhotoDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_photo, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val rowTake = dialogView.findViewById<View>(R.id.rowTakePhoto)
        val rowGallery = dialogView.findViewById<View>(R.id.rowChooseGallery)
        val rowRemove = dialogView.findViewById<View>(R.id.rowRemovePhoto)

        rowTake.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
        })

        rowGallery.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
        })

        rowRemove.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
            Toast.makeText(this, getString(R.string.TextPhotoRemoved),
                Toast.LENGTH_LONG).show()
        })

        dialog.show()
    }
}
