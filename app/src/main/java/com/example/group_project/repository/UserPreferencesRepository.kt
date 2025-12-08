package com.example.group_project.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, "") ?: ""
        set(value) = prefs.edit { putString(KEY_USER_NAME, value) }

    var userEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, "") ?: ""
        set(value) = prefs.edit { putString(KEY_USER_EMAIL, value) }

    var userId: String
        get() {
            var id = prefs.getString(KEY_USER_ID, null)
            if (id == null) {
                id = generateUserId()
                prefs.edit { putString(KEY_USER_ID, id) }
            }
            return id
        }
        private set(value) = prefs.edit { putString(KEY_USER_ID, value) }

    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE_ENABLED, false)
        set(value) = prefs.edit { putBoolean(KEY_DARK_MODE_ENABLED, value) }

    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    companion object {
        private const val PREFS_NAME = "mood_tracker_prefs"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"
    }
}