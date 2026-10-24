package com.stride.app.data.memory

import com.stride.app.domain.model.Habit
import com.stride.app.domain.model.HabitFrequency
import com.stride.app.domain.model.HabitLog
import com.stride.app.domain.model.UserProfile
import com.stride.app.domain.repository.HabitRepository
import com.stride.app.domain.repository.ProfileRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Data is lost when the process dies. It starts with some sample habits.
 */
class MemoryDataRepository : HabitRepository, ProfileRepository {

    private val habits = MutableStateFlow<List<Habit>>(emptyList())
    private val logs = MutableStateFlow<List<HabitLog>>(emptyList())
    private val profile = MutableStateFlow<UserProfile?>(null)

    private var nextHabitId = 1L
    private var nextLogId = 1L

    init {
        seedSampleData()
    }

    // ---------- HabitRepository ----------

    override fun observeHabits(): Flow<List<Habit>> = habits

    override fun observeLogs(): Flow<List<HabitLog>> = logs

    override fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>> =
        logs.map { list -> list.filter { it.habitId == habitId } }

    override suspend fun getHabit(id: Long): Habit? = habits.value.firstOrNull { it.id == id }

    override suspend fun addHabit(habit: Habit): Long {
        val id = nextHabitId++
        habits.update { it + habit.copy(id = id) }
        return id
    }

    override suspend fun updateHabit(habit: Habit) {
        habits.update { list -> list.map { if (it.id == habit.id) habit else it } }
    }

    override suspend fun deleteHabit(id: Long) {
        habits.update { list -> list.filterNot { it.id == id } }
        logs.update { list -> list.filterNot { it.habitId == id } }
    }

    override suspend fun setCompleted(habitId: Long, date: LocalDate, completed: Boolean) {
        logs.update { current ->
            val existing = current.firstOrNull { it.habitId == habitId && it.date == date }
            if (existing != null) {
                current.map { if (it.id == existing.id) it.copy(completed = completed) else it }
            } else {
                current + HabitLog(nextLogId++, habitId, date, completed)
            }
        }
    }

    // ---------- ProfileRepository ----------

    override fun observeProfile(): Flow<UserProfile?> = profile

    override suspend fun getProfile(): UserProfile? = profile.value

    override suspend fun saveProfile(profile: UserProfile) {
        this.profile.value = profile
    }

    override suspend fun clearProfile() {
        profile.value = null
    }

    // ---------- Sample data ----------

    private fun seedSampleData() {
        val today = LocalDate.now()
        val start = today.minusDays(30)

        val water = seedHabit("Drink water", "drop", "#007AFF", HabitFrequency.DAILY, start)
        val exercise = seedHabit(
            "Exercise", "activity", "#30D158", HabitFrequency.DAILY, start,
            "Central Gym", 9.9340, -84.0910
        )
        val reading = seedHabit(
            "Read 20 min", "book", "#FBBF24", HabitFrequency.DAILY, start,
            "La Sabana Park", 9.9366, -84.1035
        )
        val meditate = seedHabit("Meditate", "sun", "#00C7FF", HabitFrequency.WEEKLY, start)

        seedLogs(water, (0..11).toList())
        seedLogs(exercise, listOf(0, 1, 2, 3, 4, 6, 8))
        seedLogs(reading, listOf(1, 2, 3))
        seedLogs(meditate, listOf(2, 4))
    }

    private fun seedHabit(
        name: String,
        icon: String,
        color: String,
        frequency: HabitFrequency,
        createdAt: LocalDate,
        placeName: String? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Long {
        val id = nextHabitId++
        habits.value = habits.value + Habit(
            id, name, icon, color, frequency, createdAt, placeName, latitude, longitude
        )
        return id
    }

    private fun seedLogs(habitId: Long, daysAgo: List<Int>) {
        val today = LocalDate.now()
        logs.value = logs.value + daysAgo.map {
            HabitLog(nextLogId++, habitId, today.minusDays(it.toLong()), true)
        }
    }

    companion object {
        /** Single shared instance for the whole app. */
        val instance: MemoryDataRepository by lazy { MemoryDataRepository() }
    }
}
