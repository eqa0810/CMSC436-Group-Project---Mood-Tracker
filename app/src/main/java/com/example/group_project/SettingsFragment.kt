package com.example.group_project

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.group_project.repository.UserPreferencesRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var userPrefsRepository: UserPreferencesRepository
    private lateinit var nicknameEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        userPrefsRepository = UserPreferencesRepository(requireContext())
        nicknameEditText = view.findViewById(R.id.edittext_nickname)
        
        loadUserName()
        setupListeners()
    }

    private fun loadUserName() {
        nicknameEditText.setText(userPrefsRepository.userName)
    }

    private fun setupListeners() {
        nicknameEditText.addTextChangedListener {
            userPrefsRepository.userName = it.toString()
        }
    }
}