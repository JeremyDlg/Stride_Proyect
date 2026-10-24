package com.stride.app.util

object Validators {
    const val MAX_NAME_LENGTH = 40

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val HEX_COLOR_REGEX = Regex("^#[0-9A-Fa-f]{6}$")

    fun isValidName(name: String): Boolean {
        val trimmed = name.trim()
        return trimmed.isNotEmpty() && trimmed.length <= MAX_NAME_LENGTH
    }

    fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email.trim())

    fun isValidHexColor(color: String): Boolean = HEX_COLOR_REGEX.matches(color)
}
