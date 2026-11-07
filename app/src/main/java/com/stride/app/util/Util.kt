package com.stride.app.util

import android.content.Context
import android.content.Intent
import com.stride.app.domain.model.CalendarDay
import com.stride.app.domain.model.DayState
import java.time.LocalDate
import java.util.Locale



const val maxNameLength = 40
const val defaultLatitude = 9.9281
const val defaultLongitude = -84.0907

class Util {
    companion object {
        fun openActivity(context: Context, objClass: Class<*>,
                         extraName: String = "", value: String? = null) {
            val intent = Intent(context, objClass).apply { putExtra(extraName, value) }
            context.startActivity(intent)
        }

        fun isValidName(name: String): Boolean {
            val text = name.trim()
            return text.isNotEmpty() && text.length <= maxNameLength
        }

        fun isValidEmail(email: String): Boolean {
            val text = email.trim()
            val atIndex = text.indexOf("@")
            val dotIndex = text.lastIndexOf(".")
            return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < text.length - 1
        }

        fun formatCoordinates(latitude: Double, longitude: Double): String {
            return String.format(Locale.US, "%.4f, %.4f", latitude, longitude)
        }

        fun getCurrentStreak(dates: List<LocalDate>, today: LocalDate): Int {
            var day = today
            if (!dates.contains(day)) {
                day = day.minusDays(1)
            }
            var count = 0
            while (dates.contains(day)) {
                count++
                day = day.minusDays(1)
            }
            return count
        }

        fun getBestStreak(dates: List<LocalDate>): Int {
            val sorted = dates.sorted()
            var best = 0
            var current = 0
            for (i in sorted.indices) {
                if (i > 0 && sorted[i - 1].plusDays(1) == sorted[i]) {
                    current++
                } else {
                    current = 1
                }
                if (current > best) {
                    best = current
                }
            }
            return best
        }

        fun getMonthPercent(dates: List<LocalDate>, month: LocalDate,
                            createdAt: LocalDate, today: LocalDate): Int {

            var start = month.withDayOfMonth(1)

            if (createdAt.isAfter(start)) {
                start = createdAt
            }

            var end = month.withDayOfMonth(month.lengthOfMonth())

            if (today.isBefore(end)) {
                end = today
            }

            if (end.isBefore(start)) {
                return 0
            }

            var totalDays = 0
            var day = start

            while (!day.isAfter(end)) {
                totalDays++
                day = day.plusDays(1)
            }

            var done = 0

            for (date in dates) {
                if (!date.isBefore(start) && !date.isAfter(end)) {
                    done++
                }
            }
            return done * 100 / totalDays
        }

        fun buildMonthGrid(month: LocalDate, dates: List<LocalDate>,
                           today: LocalDate, startDate: LocalDate): List<CalendarDay> {

            val cells = mutableListOf<CalendarDay>()
            val emptyCells = month.withDayOfMonth(1).dayOfWeek.value - 1

            for (i in 1..emptyCells) {
                cells.add(CalendarDay(null, DayState.EMPTY, false))
            }

            for (day in 1..month.lengthOfMonth()) {

                val date = month.withDayOfMonth(day)
                var state = DayState.MISSED

                if (dates.contains(date)) {
                    state = DayState.DONE
                } else if (date.isBefore(startDate) || !date.isBefore(today)) {
                    state = DayState.PENDING
                }
                cells.add(CalendarDay(date, state, date == today))
            }
            return cells
        }
    }
}
