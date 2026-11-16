package com.stride.app

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.util.Util
import com.stride.app.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HabitDetailActivity : AppCompatActivity() {

    private lateinit var habitName: TextView
    private lateinit var habitSubtitle: TextView
    private lateinit var habitIcon: ImageView
    private lateinit var streakNumber: TextView
    private lateinit var bestStreak: TextView
    private lateinit var monthProgress: ProgressBar
    private lateinit var monthCompletion: TextView
    private lateinit var monthTitle: TextView

    private var habitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        habitId = intent.getIntExtra("habitId", 0)

        val btnBack = findViewById<TextView>(R.id.backButton)
        val btnEdit = findViewById<TextView>(R.id.editButton)
        val btnDelete = findViewById<View>(R.id.deleteButton)

        habitName = findViewById(R.id.habitName)
        habitSubtitle = findViewById(R.id.habitSubtitle)
        habitIcon = findViewById(R.id.habitIcon)
        streakNumber = findViewById(R.id.streakNumber)
        bestStreak = findViewById(R.id.bestStreak)
        monthProgress = findViewById(R.id.monthProgress)
        monthCompletion = findViewById(R.id.monthCompletion)
        monthTitle = findViewById(R.id.monthTitle)

        btnBack.setOnClickListener(View.OnClickListener { view ->
            finish()
        })
        btnEdit.setOnClickListener(View.OnClickListener { view ->
            openForm()
        })
        btnDelete.setOnClickListener(View.OnClickListener { view ->
            showDeleteDialog()
        })
    }

    override fun onResume() {
        super.onResume()
        loadHabit()
    }

    private fun loadHabit() {

        val habit = HabitViewModel.getHabitById(habitId)

        if (habit == null) {
            finish()
            return
        }

        habitName.text = habit.name
        habitIcon.setImageResource(Util.getIconResource(habit.icon))
        habitIcon.imageTintList = ColorStateList.valueOf(Util.getColorValue(habit.color))

        habitSubtitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)

        if (habit.frequency == HabitFrequency.DAILY) {
            habitSubtitle.text = getString(R.string.TextFrequencyDaily)
        } else {
            habitSubtitle.text = getString(R.string.TextFrequencyWeekly)
        }

        streakNumber.text = HabitViewModel.getCurrentStreak(habit.id).toString()
        bestStreak.text = getString(R.string.TextDetailBestStreak) + " " +
            HabitViewModel.getBestStreak(habit.id)

        val percent = HabitViewModel.getMonthPercent(habit.id, LocalDate.now())
        monthProgress.progress = percent
        monthCompletion.text = percent.toString() + "% " +
            getString(R.string.TextDetailMonthCompletion)

        monthTitle.text = LocalDate.now().format(
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
        )
    }

    private fun openForm() {
        val intent = Intent(this, HabitFormActivity::class.java)
        intent.putExtra("habitId", habitId)
        startActivity(intent)
    }

    private fun showDeleteDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_habit, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val btnCancel = dialogView.findViewById<TextView>(R.id.cancelButton)
        val btnConfirm = dialogView.findViewById<TextView>(R.id.confirmButton)

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
        })

        btnConfirm.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
            HabitViewModel.removeHabit(habitId)
            Toast.makeText(this, getString(R.string.TextHabitDeleted),
                Toast.LENGTH_LONG).show()
            finish()
        })

        dialog.show()
    }
}
