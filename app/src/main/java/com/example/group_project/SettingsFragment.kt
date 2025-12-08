package com.example.group_project

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.group_project.notification.ReminderScheduler
import com.example.group_project.repository.UserPreferencesRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var model: UserPreferencesRepository
    private lateinit var settingsModel: SettingsModel
    private lateinit var reminderScheduler: ReminderScheduler
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scheduleReminderIfEnabled()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UserPreferencesRepository(requireContext())
        settingsModel = SettingsModel(requireContext())
        reminderScheduler = ReminderScheduler(requireContext())

        val nicknameEdit = view.findViewById<EditText>(R.id.edittext_nickname)
        val emailEdit = view.findViewById<EditText>(R.id.edittext_email)
        val reminderSwitch = view.findViewById<Switch>(R.id.switch_reminder)
        val reminderTimeText = view.findViewById<TextView>(R.id.text_reminder_time)
        val pickTimeButton = view.findViewById<Button>(R.id.button_pick_reminder_time)
        val darkModeSwitch = view.findViewById<Switch>(R.id.switch_dark_mode)

        nicknameEdit.setText(model.userName)
        emailEdit.setText(model.userEmail)
        darkModeSwitch.isChecked = settingsModel.isDarkModeEnabled()
        reminderSwitch.isChecked = settingsModel.isReminderEnabled()
        
        updateReminderTimeText(reminderTimeText)

        nicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                model.userName = (nicknameEdit.text.toString())
            }
        }

        emailEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) model.userEmail = emailEdit.text.toString()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsModel.saveDarkModeEnabled(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsModel.saveReminderEnabled(isChecked)
            if (isChecked) {
                requestNotificationPermissionAndSchedule()
            } else {
                reminderScheduler.cancelReminder()
            }
        }
        
        pickTimeButton.setOnClickListener {
            val currHour = settingsModel.getReminderHour()
            val currMinute = settingsModel.getReminderMinute()

            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    settingsModel.saveReminderTime(selectedHour, selectedMinute)
                    updateReminderTimeText(reminderTimeText)
                    if (settingsModel.isReminderEnabled()) {
                        reminderScheduler.scheduleReminder(selectedHour, selectedMinute)
                    }
                },
                currHour,
                currMinute,
                true
            ).show()
        }
    }
    
    private fun requestNotificationPermissionAndSchedule() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    scheduleReminderIfEnabled()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            scheduleReminderIfEnabled()
        }
    }
    
    private fun scheduleReminderIfEnabled() {
        if (settingsModel.isReminderEnabled()) {
            reminderScheduler.scheduleReminder(
                settingsModel.getReminderHour(),
                settingsModel.getReminderMinute()
            )
            Toast.makeText(requireContext(), "Reminder set!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateReminderTimeText(textView: TextView) {
        val hour = settingsModel.getReminderHour()
        val minute = settingsModel.getReminderMinute()
        textView.text = "Reminder time: %02d:%02d".format(hour, minute)
    }
}