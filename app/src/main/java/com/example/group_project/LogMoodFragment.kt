package com.example.group_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.group_project.repository.MoodRepository
import com.example.group_project.repository.UserPreferencesRepository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogMoodFragment : Fragment(R.layout.fragment_log_mood) {

    private var adView: AdView? = null
    private lateinit var moodRepository: MoodRepository
    private lateinit var userPrefsRepository: UserPreferencesRepository
    
    private lateinit var greetingText: TextView
    private lateinit var dateText: TextView
    private lateinit var moodLabel: TextView
    private lateinit var moodSeekBar: SeekBar
    private lateinit var journalEditText: EditText
    private lateinit var saveMoodButton: Button
    
    private var currentMoodScore = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        moodRepository = MoodRepository()
        userPrefsRepository = UserPreferencesRepository(requireContext())
        
        initializeViews(view)
        setupAds(view)
        setupListeners()
        updateGreeting()
        updateDate()
    }

    private fun initializeViews(view: View) {
        greetingText = view.findViewById(R.id.text_greeting)
        dateText = view.findViewById(R.id.text_today_date)
        moodLabel = view.findViewById(R.id.text_mood_label)
        moodSeekBar = view.findViewById(R.id.seekbar_mood)
        journalEditText = view.findViewById(R.id.edittext_journal)
        saveMoodButton = view.findViewById(R.id.button_save_mood)
    }

    private fun setupAds(view: View) {
        val adContainer = view.findViewById<LinearLayout>(R.id.ad_container_log)
        val banner = AdView(requireContext())
        banner.setAdSize(AdSize.BANNER)
        banner.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        adContainer.addView(banner)
        banner.loadAd(AdRequest.Builder().build())
        adView = banner
    }

    private fun setupListeners() {
        moodSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentMoodScore = progress + 1
                moodLabel.text = "Mood: $currentMoodScore / 10"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveMoodButton.setOnClickListener {
            saveMood()
        }
    }

    private fun updateGreeting() {
        val userName = userPrefsRepository.userName
        greetingText.text = if (userName.isNotEmpty()) {
            "Hello $userName, how are you feeling today?"
        } else {
            "How are you feeling today?"
        }
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateText.text = "Today – ${dateFormat.format(Date())}"
    }

    private fun saveMood() {
        val journalText = journalEditText.text.toString()
        val userId = userPrefsRepository.userId
        
        val moodEntry = MoodEntry(
            date = Date(),
            moodScore = currentMoodScore,
            journalText = journalText.ifEmpty { null },
            userId = userId
        )

        saveMoodButton.isEnabled = false
        
        moodRepository.saveMood(
            moodEntry = moodEntry,
            onSuccess = {
                Toast.makeText(requireContext(), "Mood saved successfully!", Toast.LENGTH_SHORT).show()
                journalEditText.text.clear()
                saveMoodButton.isEnabled = true
            },
            onFailure = { error ->
                Toast.makeText(requireContext(), "Failed to save mood: ${error.message}", Toast.LENGTH_LONG).show()
                saveMoodButton.isEnabled = true
            }
        )
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
        updateGreeting()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        adView?.destroy()
        super.onDestroyView()
    }
}
