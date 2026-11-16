package com.stride.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitFilter
import com.stride.app.util.Util
import com.stride.app.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeActivity : AppCompatActivity(), OnHabitActionListener {

    private lateinit var habitAdapter: HabitAdapter
    private lateinit var searchInput: EditText
    private lateinit var emptyText: TextView
    private lateinit var progressText: TextView
    private lateinit var chipAll: TextView
    private lateinit var chipPending: TextView
    private lateinit var chipDone: TextView
    private lateinit var chipDaily: TextView
    private lateinit var chipWeekly: TextView

    private var filter = HabitFilter.ALL

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnAddHabit = findViewById<ImageView>(R.id.addHabitButton)
        val navHome = findViewById<View>(R.id.navHome)
        val navMap = findViewById<View>(R.id.navMap)
        val navProfile = findViewById<View>(R.id.navProfile)
        val dateText = findViewById<TextView>(R.id.dateText)
        val chipSearch = findViewById<TextView>(R.id.chipSearch)
        val habitList = findViewById<RecyclerView>(R.id.habitList)

        searchInput = findViewById(R.id.searchInput)
        emptyText = findViewById(R.id.emptyText)
        progressText = findViewById(R.id.progressText)
        chipAll = findViewById(R.id.chipAll)
        chipPending = findViewById(R.id.chipPending)
        chipDone = findViewById(R.id.chipDone)
        chipDaily = findViewById(R.id.chipDaily)
        chipWeekly = findViewById(R.id.chipWeekly)

        navHome.setBackgroundResource(R.drawable.bg_nav_item_selected)

        dateText.text = LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)
        )

        habitAdapter = HabitAdapter(mutableListOf(), this)
        habitList.layoutManager = LinearLayoutManager(this)
        habitList.adapter = habitAdapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                applyFilter()
            }
        })

        btnAddHabit.setOnClickListener(View.OnClickListener { view ->
            openForm(0)
        })

        chipSearch.setOnClickListener(View.OnClickListener { view ->
            toggleSearch()
        })

        chipAll.setOnClickListener(View.OnClickListener { view ->
            selectFilter(HabitFilter.ALL, chipAll)
        })
        chipPending.setOnClickListener(View.OnClickListener { view ->
            selectFilter(HabitFilter.PENDING, chipPending)
        })
        chipDone.setOnClickListener(View.OnClickListener { view ->
            selectFilter(HabitFilter.DONE, chipDone)
        })
        chipDaily.setOnClickListener(View.OnClickListener { view ->
            selectFilter(HabitFilter.DAILY, chipDaily)
        })
        chipWeekly.setOnClickListener(View.OnClickListener { view ->
            selectFilter(HabitFilter.WEEKLY, chipWeekly)
        })

        navMap.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, MapActivity::class.java)
        })

        navProfile.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, ProfileActivity::class.java)
        })
    }

    override fun onResume() {
        super.onResume()
        applyFilter()
    }

    override fun onHabitClick(habit: Habit) {
        val intent = Intent(this, HabitDetailActivity::class.java)
        intent.putExtra("habitId", habit.id)
        startActivity(intent)
    }

    override fun onCheckClick(habit: Habit) {
        HabitViewModel.toggleToday(habit.id)
        applyFilter()
    }

    private fun openForm(habitId: Int) {
        val intent = Intent(this, HabitFormActivity::class.java)
        intent.putExtra("habitId", habitId)
        startActivity(intent)
    }

    private fun selectFilter(newFilter: HabitFilter, selectedChip: TextView) {

        filter = newFilter

        val chips = listOf(chipAll, chipPending, chipDone, chipDaily, chipWeekly)

        for (chip in chips) {
            chip.setBackgroundResource(R.drawable.bg_chip)
            chip.setTextColor(getColor(R.color.stride_text))
        }

        selectedChip.setBackgroundResource(R.drawable.bg_chip_selected)
        selectedChip.setTextColor(getColor(R.color.stride_accent_light))

        applyFilter()
    }

    private fun applyFilter() {

        val habits = HabitViewModel.getFilteredHabits(filter, searchInput.text.toString())
        habitAdapter.setHabits(habits)

        if (habits.isEmpty()) {
            emptyText.visibility = View.VISIBLE

            if (HabitViewModel.getHabits().isEmpty()) {
                emptyText.setText(R.string.TextHomeEmptyNoHabits)
            } else {
                emptyText.setText(R.string.TextHomeEmpty)
            }
        } else {
            emptyText.visibility = View.GONE
        }

        progressText.text = HabitViewModel.countCompletedToday().toString() + "/" +
            HabitViewModel.getHabits().size
    }

    private fun toggleSearch() {

        if (searchInput.visibility == View.VISIBLE) {
            searchInput.setText("")
            searchInput.visibility = View.GONE
        } else {
            searchInput.visibility = View.VISIBLE
            searchInput.requestFocus()
        }
    }
}
