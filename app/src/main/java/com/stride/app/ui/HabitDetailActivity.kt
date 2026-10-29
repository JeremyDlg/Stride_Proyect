package com.stride.app.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R

/** Habit detail: streak, monthly completion and calendar. */
class HabitDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
        findViewById<View>(R.id.editButton).setOnClickListener {
            startActivity(Intent(this, HabitFormActivity::class.java))
        }
        findViewById<View>(R.id.deleteButton).setOnClickListener { showDeleteDialog() }
    }

    private fun showDeleteDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_delete_habit, null)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<View>(R.id.cancelButton).setOnClickListener { dialog.dismiss() }
        view.findViewById<View>(R.id.confirmButton).setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, R.string.toast_habit_deleted, Toast.LENGTH_SHORT).show()
            finish()
        }
        dialog.show()
    }
}
