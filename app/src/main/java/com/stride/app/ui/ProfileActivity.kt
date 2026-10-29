package com.stride.app.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stride.app.R

/** Profile and settings. */
class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        BottomNav.setup(this, NavTab.PROFILE)

        findViewById<View>(R.id.profilePhoto).setOnClickListener { showPhotoSheet() }
        findViewById<View>(R.id.editProfileRow).setOnClickListener { showComingSoon() }
        findViewById<View>(R.id.languageRow).setOnClickListener { showComingSoon() }

        findViewById<View>(R.id.logoutButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun showPhotoSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_photo, null)

        view.findViewById<View>(R.id.rowTakePhoto).setOnClickListener {
            dialog.dismiss()
            showComingSoon()
        }
        view.findViewById<View>(R.id.rowChooseGallery).setOnClickListener {
            dialog.dismiss()
            showComingSoon()
        }
        view.findViewById<View>(R.id.rowRemovePhoto).setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, R.string.toast_photo_removed, Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.setOnShowListener {
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundColor(Color.TRANSPARENT)
        }
        dialog.show()
    }

    private fun showComingSoon() {
        Toast.makeText(this, R.string.toast_feature_coming_soon, Toast.LENGTH_SHORT).show()
    }
}