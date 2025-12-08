package com.example.group_project

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.group_project.repository.UserPreferencesRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var model: UserPreferencesRepository
    private lateinit var settingsModel: SettingsModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UserPreferencesRepository(requireContext())
        settingsModel = SettingsModel(requireContext())

        val nicknameEdit = view.findViewById<EditText>(R.id.edittext_nickname)
        val emailEdit = view.findViewById<EditText>(R.id.edittext_email)
        val darkModeSwitch = view.findViewById<Switch>(R.id.switch_dark_mode)

        nicknameEdit.setText(model.userName)
        emailEdit.setText(model.userEmail)
        darkModeSwitch.isChecked = settingsModel.isDarkModeEnabled()

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
    }
}