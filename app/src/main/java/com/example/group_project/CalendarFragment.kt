package com.example.group_project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.group_project.repository.MoodRepository
import com.example.group_project.repository.UserPreferencesRepository
import java.util.Calendar

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var moodRepository: MoodRepository
    private lateinit var userId: String
    private lateinit var selectedDateText: TextView
    private lateinit var selectedScoreText: TextView
    private lateinit var selectedNoteText: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var model: UserPreferencesRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moodRepository = MoodRepository()
        model = UserPreferencesRepository(requireContext())
        userId = model.userId

        calendarView = view.findViewById(R.id.calendar_view)
        selectedDateText = view.findViewById(R.id.text_selected_date)
        selectedScoreText = view.findViewById(R.id.text_selected_score)
        selectedNoteText = view.findViewById(R.id.text_selected_note)

        loadInitialDate()

        // Listen for date selection
        calendarView.setOnDateChangeListener { _, year, month, day ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, day, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }

            //Currently using timestamp for correct date, can change to actual date later
            val startTimestamp = calendar.timeInMillis
            val endTimestamp = startTimestamp + 24 * 60 * 60 * 1000 - 1

            selectedDateText.text = "${month + 1}/${day}/$year"

            // Query Firebase for moods on this date
            moodRepository.getMoodsByDateRange(userId, startTimestamp, endTimestamp,
                onSuccess = { moods ->
                    Log.d("CalendarFragment", "Moods fetched: ${moods.size}")
                    if (moods.isNotEmpty()) {
                        val mood = moods.last()
                        selectedScoreText.text = "Mood: ${mood.getMoodScore()}/10"
                        selectedNoteText.text = mood.journalText ?: "No note for this day"
                    } else {
                        selectedScoreText.text = "Mood: --/10"
                        selectedNoteText.text = "No entry for this day."
                    }
                },
                onFailure = { exception ->
                    Log.d("CalendarFragment", "failed")
                    selectedScoreText.text = "Mood: --/10"
                    selectedNoteText.text = "Failed to load entry."
                }
            )
        }
    }

    private fun loadInitialDate() {
        val initialMillis = calendarView.date

        val cal = Calendar.getInstance().apply {
            timeInMillis = initialMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val startTimestamp = cal.timeInMillis
        val endTimestamp = startTimestamp + 24 * 60 * 60 * 1000 - 1

        selectedDateText.text = "${month + 1}/${day}/$year"

        moodRepository.getMoodsByDateRange(
            userId,
            startTimestamp,
            endTimestamp,
            onSuccess = { moods ->
                if (moods.isNotEmpty()) {
                    val mood = moods.last()
                    selectedScoreText.text = "Mood: ${mood.getMoodScore()}/10"
                    selectedNoteText.text = mood.journalText ?: "No note for this day"
                } else {
                    selectedScoreText.text = "Mood: --/10"
                    selectedNoteText.text = "No entry for this day."
                }
            },
            onFailure = {
                selectedScoreText.text = "Mood: --/10"
                selectedNoteText.text = "Failed to load entry."
            }
        )
    }

}

