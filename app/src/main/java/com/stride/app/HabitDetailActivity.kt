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

class HabitDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        val btnBack = findViewById<TextView>(R.id.backButton)
        val btnEdit = findViewById<TextView>(R.id.editButton)
        val btnDelete = findViewById<View>(R.id.deleteButton)

        btnBack.setOnClickListener(View.OnClickListener { view ->
            finish()
        })
        btnEdit.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, HabitFormActivity::class.java)
        })
        btnDelete.setOnClickListener(View.OnClickListener { view ->
            showDeleteDialog()
        })
    }

    private fun showDeleteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_habit, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialogView.findViewById<TextView>(R.id.cancelButton)
        val btnConfirm = dialogView.findViewById<TextView>(R.id.confirmButton)

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
        })
        btnConfirm.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
            Toast.makeText(this, getString(R.string.TextHabitDeleted),
                Toast.LENGTH_LONG).show()
            finish()
        })
        dialog.show()
    }
}
