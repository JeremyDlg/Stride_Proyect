package com.stride.app.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

/** Streak and completion calculations based on the dates a habit was completed. */
object HabitStatsCalculator {

    /**
     * Consecutive completed days ending today. If today is not completed yet,
     * the streak is counted from yesterday so it does not reset during the day.
     */
    fun currentStreak(completedDates: Set<LocalDate>, today: LocalDate): Int {
        var day = if (today in completedDates) today else today.minusDays(1)
        var count = 0
        while (day in completedDates) {
            count++
            day = day.minusDays(1)
        }
        return count
    }

    /** Longest run of consecutive completed days. */
    fun bestStreak(completedDates: Set<LocalDate>): Int {
        var best = 0
        var current = 0
        var previous: LocalDate? = null
        for (date in completedDates.sorted()) {
            current = if (previous != null && previous.plusDays(1) == date) current + 1 else 1
            if (current > best) best = current
            previous = date
        }
        return best
    }

    /**
     * Percentage (0-100) of days completed in [month], counting only from the day
     * the habit was created until today or the end of the month.
     */
    fun monthCompletionPercent(
        completedDates: Set<LocalDate>,
        month: YearMonth,
        createdAt: LocalDate,
        today: LocalDate
    ): Int {
        val start = maxOf(month.atDay(1), createdAt)
        val end = minOf(month.atEndOfMonth(), today)
        if (end.isBefore(start)) return 0
        val totalDays = ChronoUnit.DAYS.between(start, end) + 1
        val done = completedDates.count { !it.isBefore(start) && !it.isAfter(end) }
        return (done * 100 / totalDays).toInt()
    }
}
