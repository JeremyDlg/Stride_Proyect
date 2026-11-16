package com.stride.app

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.util.Util
import com.stride.app.viewmodel.HabitViewModel
import java.time.LocalDate

class HabitFormActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var formTitle: TextView
    private lateinit var hexLabel: TextView
    private lateinit var saveButton: TextView
    private lateinit var iconOptions: List<View>
    private lateinit var iconImages: List<ImageView>
    private lateinit var colorOptions: List<View>
    private lateinit var frequencyDaily: TextView
    private lateinit var frequencyWeekly: TextView

    private val iconKeys = listOf("drop", "activity", "book", "sun", "leaf")
    private val colorValues = listOf("#007AFF", "#00C7FF", "#FBBF24", "#30D158")

    private var habitId = 0
    private var selectedIcon = "drop"
    private var selectedColor = "#007AFF"
    private var selectedFrequency = HabitFrequency.DAILY

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        habitId = intent.getIntExtra("habitId", 0)

        txtName = findViewById(R.id.habitNameInput)
        formTitle = findViewById(R.id.formTitle)
        hexLabel = findViewById(R.id.hexLabel)
        saveButton = findViewById(R.id.saveButton)
        frequencyDaily = findViewById(R.id.frequencyDaily)
        frequencyWeekly = findViewById(R.id.frequencyWeekly)

        val btnCancel = findViewById<TextView>(R.id.cancelButton)
        val placeRow = findViewById<View>(R.id.placeRow)

        iconOptions = listOf(
            findViewById(R.id.iconDrop),
            findViewById(R.id.iconActivity),
            findViewById(R.id.iconBook),
            findViewById(R.id.iconSun),
            findViewById(R.id.iconLeaf)
        )

        iconImages = listOf(
            findViewById(R.id.iconDropImage),
            findViewById(R.id.iconActivityImage),
            findViewById(R.id.iconBookImage),
            findViewById(R.id.iconSunImage),
            findViewById(R.id.iconLeafImage)
        )

        colorOptions = listOf(
            findViewById(R.id.colorBlueRing),
            findViewById(R.id.colorCyanRing),
            findViewById(R.id.colorGoldRing),
            findViewById(R.id.colorGreenRing)
        )

        for (i in iconOptions.indices) {
            iconOptions[i].setOnClickListener(View.OnClickListener { view ->
                selectedIcon = iconKeys[i]
                updateIconSelection()
            })
        }

        for (i in colorOptions.indices) {
            colorOptions[i].setOnClickListener(View.OnClickListener { view ->
                selectedColor = colorValues[i]
                updateColorSelection()
            })
        }

        frequencyDaily.setOnClickListener(View.OnClickListener { view ->
            selectedFrequency = HabitFrequency.DAILY
            updateFrequency()
        })

        frequencyWeekly.setOnClickListener(View.OnClickListener { view ->
            selectedFrequency = HabitFrequency.WEEKLY
            updateFrequency()
        })

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        placeRow.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, LocationPickerActivity::class.java)
        })

        saveButton.setOnClickListener(View.OnClickListener { view ->
            saveHabit()
        })

        if (habitId != 0) {
            loadHabit()
        }

        updateIconSelection()
        updateColorSelection()
        updateFrequency()
    }

    private fun loadHabit() {

        val habit = HabitViewModel.getHabitById(habitId)

        if (habit == null) {
            finish()
            return
        }

        formTitle.setText(R.string.TextFormEditTitle)
        saveButton.setText(R.string.TextSaveChanges)
        txtName.setText(habit.name)
        selectedIcon = habit.icon
        selectedColor = habit.color
        selectedFrequency = habit.frequency
    }

    private fun saveHabit() {

        val name = txtName.text.toString().trim()

        if (!Util.isValidName(name)) {
            Toast.makeText(this, getString(R.string.TextNameRequiredError),
                Toast.LENGTH_LONG).show()
            return
        }

        if (habitId == 0) {

            val habit = Habit(0, name, selectedIcon, selectedColor,
                selectedFrequency, LocalDate.now())

            HabitViewModel.saveHabit(habit)

            Toast.makeText(this, getString(R.string.TextHabitSaved),
                Toast.LENGTH_LONG).show()
            finish()

        } else {
            showUpdateDialog(name)
        }
    }

    private fun showUpdateDialog(name: String) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_update, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val btnCancel = dialogView.findViewById<TextView>(R.id.cancelButton)
        val btnConfirm = dialogView.findViewById<TextView>(R.id.confirmButton)

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
        })

        btnConfirm.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
            updateHabit(name)
            Toast.makeText(this, getString(R.string.TextHabitUpdated),
                Toast.LENGTH_LONG).show()
            finish()
        })

        dialog.show()
    }

    private fun updateHabit(name: String) {

        val habit = HabitViewModel.getHabitById(habitId)

        if (habit == null) {
            finish()
            return
        }

        val updated = Habit(habit.id, name, selectedIcon, selectedColor,
            selectedFrequency, habit.createdAt, habit.placeName,
            habit.latitude, habit.longitude)

        HabitViewModel.updateHabit(updated)
    }

    private fun updateIconSelection() {

        for (i in iconOptions.indices) {

            if (iconKeys[i] == selectedIcon) {
                iconOptions[i].setBackgroundResource(R.drawable.bg_icon_option_selected)
                iconImages[i].imageTintList =
                    ColorStateList.valueOf(getColor(R.color.stride_accent_light))
            } else {
                iconOptions[i].setBackgroundResource(R.drawable.bg_icon_option)
                iconImages[i].imageTintList =
                    ColorStateList.valueOf(getColor(R.color.stride_muted))
            }
        }
    }

    private fun updateColorSelection() {

        for (i in colorOptions.indices) {

            if (colorValues[i] == selectedColor) {
                colorOptions[i].setBackgroundResource(R.drawable.bg_dot_ring)
            } else {
                colorOptions[i].setBackgroundResource(0)
            }
        }

        hexLabel.text = selectedColor
    }

    private fun updateFrequency() {

        if (selectedFrequency == HabitFrequency.DAILY) {
            frequencyDaily.setBackgroundResource(R.drawable.bg_segment_selected)
            frequencyDaily.setTextColor(getColor(R.color.stride_on_accent))
            frequencyWeekly.setBackgroundResource(0)
            frequencyWeekly.setTextColor(getColor(R.color.stride_muted))
        } else {
            frequencyWeekly.setBackgroundResource(R.drawable.bg_segment_selected)
            frequencyWeekly.setTextColor(getColor(R.color.stride_on_accent))
            frequencyDaily.setBackgroundResource(0)
            frequencyDaily.setTextColor(getColor(R.color.stride_muted))
        }
    }
}
