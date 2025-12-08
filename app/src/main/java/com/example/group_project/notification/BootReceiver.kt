package com.example.group_project.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.group_project.SettingsModel

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val settingsModel = SettingsModel(context)
            
            if (settingsModel.isReminderEnabled()) {
                val scheduler = ReminderScheduler(context)
                scheduler.scheduleReminder(
                    settingsModel.getReminderHour(),
                    settingsModel.getReminderMinute()
                )
            }
        }
    }
}
