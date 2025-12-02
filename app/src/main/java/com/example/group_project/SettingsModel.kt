package com.example.group_project

import android.content.Context
import android.content.SharedPreferences

class SettingsModel(context: Context) {
    // Class does as advertised, serves as the model for the settings class
    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

    fun saveNickname(nickname: String) {
        val editor = prefs.edit()
        editor.putString("nickname", nickname)
        editor.commit()
    }

    fun getNickname(): String {
        val result = prefs.getString("nickname", "") ?: return ""
        return result
    }

    fun saveReminderEnabled(enabled: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("reminder_enabled", enabled).apply()
        editor.commit()
    }

    fun isReminderEnabled(): Boolean {
        return prefs.getBoolean("reminder_enabled", false)
    }

    fun saveReminderTime(hour: Int, minute: Int) {
        val editor = prefs.edit()
        editor.putInt("reminder_hour", hour)
        editor.putInt("reminder_minute", minute)

        editor.commit()
    }

    fun getReminderHour() : Int {
        return prefs.getInt("reminder_hour", 21)
    }
    fun getReminderMinute(): Int {
        return prefs.getInt("reminder_minute", 0)
    }

    fun saveDarkModeEnabled(enabled: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("dark_mode_enabled", enabled)
        editor.commit()
    }

    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean("dark_mode_enabled", false)
    }
}