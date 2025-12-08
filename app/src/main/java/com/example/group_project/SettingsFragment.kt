package com.example.group_project

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.group_project.repository.UserPreferencesRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var prefsRepository: UserPreferencesRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize single repository for all preferences
        prefsRepository = UserPreferencesRepository(requireContext())

        val nicknameEdit = view.findViewById<EditText>(R.id.edittext_nickname)
        val emailEdit = view.findViewById<EditText>(R.id.edittext_email)
        val darkModeSwitch = view.findViewById<Switch>(R.id.switch_dark_mode)

        // Load all preferences from single repository
        nicknameEdit.setText(prefsRepository.userName)
        emailEdit.setText(prefsRepository.userEmail)
        darkModeSwitch.isChecked = prefsRepository.isDarkModeEnabled

        // Nickname change listener
        nicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                prefsRepository.userName = nicknameEdit.text.toString()
            }
        }

        // Email change listener
        emailEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                prefsRepository.userEmail = emailEdit.text.toString()
            }
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefsRepository.isDarkModeEnabled = isChecked
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}