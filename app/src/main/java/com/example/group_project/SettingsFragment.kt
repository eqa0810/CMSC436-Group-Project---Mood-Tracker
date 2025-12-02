package com.example.group_project

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.group_project.repository.UserPreferencesRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var model: UserPreferencesRepository
    private lateinit var settingsModel: SettingsModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UserPreferencesRepository(requireContext())
        settingsModel = SettingsModel(requireContext())

        val nicknameEdit = view.findViewById<EditText>(R.id.edittext_nickname)
        val emailEdit = view.findViewById<EditText>(R.id.edittext_email)
        val reminderSwitch = view.findViewById<Switch>(R.id.switch_reminder)
        val reminderTime = view.findViewById<TextView>(R.id.text_reminder_time)
        val pickTimeButton = view.findViewById<Button>(R.id.button_pick_reminder_time)
        val darkModeSwitch = view.findViewById<Switch>(R.id.switch_dark_mode)

        // Utilize model
        nicknameEdit.setText(model.userName)
        emailEdit.setText(model.userEmail)

        darkModeSwitch.isChecked = settingsModel.isDarkModeEnabled()

        // Assume most commented out code is reminder related, will delete/keep as needed

        /*
        reminderSwitch.isChecked = model.isReminderEnabled()


        val date = Date(0)
        date.hours = model.getReminderHour()
        date.minutes = model.getReminderMinute()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val initialTime = sdf.format(date)



        reminderTime.text = "Reminder time: $initialTime"
        */


        // controller stuff

        // Save nickname anytime it changes
        nicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                model.userName = (nicknameEdit.text.toString())
            }
        }

        emailEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) model.userEmail = emailEdit.text.toString()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            settingsModel.saveDarkModeEnabled(isChecked)

            // Apply theme change immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        /*
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.saveReminderEnabled(isChecked)
        }
        pickTimeButton.setOnClickListener {
            val currHour = model.getReminderHour()
            val currMinute = model.getReminderMinute()

            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    model.saveReminderTime(selectedHour, selectedMinute)
                    date.hours = selectedHour
                    date.minutes = selectedMinute

                    val formattedTime = sdf.format(date)
                    reminderTime.text = "Reminder time: $formattedTime"
                },
                currHour,
                currMinute,
                true
            ).show()
        }
        */
    }



}