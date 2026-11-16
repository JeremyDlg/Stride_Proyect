package com.stride.app

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stride.app.domain.model.Habit
import com.stride.app.util.Util
import com.stride.app.viewmodel.HabitViewModel

interface OnHabitActionListener {
    fun onHabitClick(habit: Habit)
    fun onCheckClick(habit: Habit)
}

class HabitAdapter(
    private var habits: List<Habit>,
    private val listener: OnHabitActionListener
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    fun setHabits(newHabits: List<Habit>) {
        habits = newHabits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val habitName: TextView = itemView.findViewById(R.id.habitName)
        private val habitStreak: TextView = itemView.findViewById(R.id.habitStreak)
        private val habitIcon: ImageView = itemView.findViewById(R.id.habitIcon)
        private val habitCheck: View = itemView.findViewById(R.id.habitCheck)

        fun bind(habit: Habit) {

            habitName.text = habit.name
            habitStreak.text = HabitViewModel.getCurrentStreak(habit.id).toString() +
                " " + itemView.context.getString(R.string.TextDays)
            habitIcon.setImageResource(Util.getIconResource(habit.icon))
            habitIcon.imageTintList = ColorStateList.valueOf(Util.getColorValue(habit.color))

            if (HabitViewModel.isCompletedToday(habit.id)) {
                habitCheck.setBackgroundResource(R.drawable.bg_check_on)
            } else {
                habitCheck.setBackgroundResource(R.drawable.bg_check_off)
            }

            itemView.setOnClickListener(View.OnClickListener { view ->
                listener.onHabitClick(habit)
            })
            habitCheck.setOnClickListener(View.OnClickListener { view ->
                listener.onCheckClick(habit)
            })
        }
    }
}
