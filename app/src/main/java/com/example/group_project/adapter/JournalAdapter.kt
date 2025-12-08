package com.example.group_project.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.group_project.MoodEntry
import com.example.group_project.R
import com.google.android.material.color.MaterialColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JournalAdapter(private val entries: List<MoodEntry>) : 
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    class JournalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.journal_date)
        val moodScore: TextView = view.findViewById(R.id.journal_mood_score)
        val moodLabel: TextView = view.findViewById(R.id.journal_mood_label)
        val journalText: TextView = view.findViewById(R.id.journal_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_journal_entry, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val entry = entries[position]
        val context = holder.itemView.context
        
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = Date(entry.timestamp)
        
        holder.dateText.text = "${dateFormat.format(date)} at ${timeFormat.format(date)}"
        
        val score = entry.getMoodScore()
        holder.moodScore.text = "$score/10"
        holder.moodScore.setTextColor(getMoodColor(score))
        
        holder.moodLabel.text = getMoodLabel(score)
        
        val text = entry.journalText
        if (text.isNullOrEmpty()) {
            holder.journalText.text = "No journal entry for this mood."
            // Use theme-aware color for empty state
            holder.journalText.setTextColor(
                MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY)
            )
        } else {
            holder.journalText.text = text
            // Use theme-aware color for text
            holder.journalText.setTextColor(
                MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, Color.BLACK)
            )
        }
    }

    override fun getItemCount() = entries.size

    private fun getMoodColor(score: Int): Int {
        return when {
            score <= 3 -> Color.parseColor("#E57373")
            score <= 7 -> Color.parseColor("#FFB74D")
            else -> Color.parseColor("#81C784")
        }
    }

    private fun getMoodLabel(score: Int): String {
        return when {
            score <= 2 -> "Feeling very low"
            score <= 4 -> "Feeling low"
            score <= 6 -> "Feeling okay"
            score <= 8 -> "Feeling good"
            else -> "Feeling excellent"
        }
    }
}
