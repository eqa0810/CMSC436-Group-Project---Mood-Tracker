package com.example.group_project

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.group_project.adapter.JournalAdapter
import com.example.group_project.repository.MoodRepository
import com.example.group_project.repository.UserPreferencesRepository

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private lateinit var moodRepository: MoodRepository
    private lateinit var userPrefsRepository: UserPreferencesRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moodRepository = MoodRepository()
        userPrefsRepository = UserPreferencesRepository(requireContext())

        recyclerView = view.findViewById(R.id.journal_recycler)
        emptyText = view.findViewById(R.id.empty_journal_text)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadJournalEntries()
    }

    private fun loadJournalEntries() {
        val userId = userPrefsRepository.userId

        moodRepository.getMoodsForUser(
            userId = userId,
            onSuccess = { moods ->
                if (moods.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyText.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyText.visibility = View.GONE
                    recyclerView.adapter = JournalAdapter(moods)
                }
            },
            onFailure = {
                recyclerView.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
                emptyText.text = "Failed to load journal entries."
            }
        )
    }

    override fun onResume() {
        super.onResume()
        loadJournalEntries()
    }
}
