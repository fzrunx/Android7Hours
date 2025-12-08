package com.sesac.auth.utils

import android.util.Patterns

object ValidationUtils {
    /**
     * Checks if the email has a valid format.
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Checks if the password meets the complexity requirements.
     * At least 10 characters, one letter, one number, and one special character.
     */
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{10,}\$".toRegex()
        return password.matches(passwordPattern)
    }
}
