package com.example.group_project

import java.util.Date
import android.graphics.Color

class MoodEntry {
    var id: String? = null
    private var date: Date? = null
    private var moodScore = 0 // 1-10 scale
    var journalText: String? = null
    var userId: String? = null
    var timestamp: Long = 0

    constructor()

    //creating new entries
    constructor(date: Date, moodScore: Int, journalText: String?, userId: String?) {
        this.date = date
        this.moodScore = moodScore
        this.journalText = journalText
        this.userId = userId
        this.timestamp = date.getTime()
    }

    //GETTERS AND SETTERS

    fun getDate(): Date? {
        return date
    }

    fun setDate(date: Date) {
        this.date = date
        this.timestamp = date.getTime()
    }

    fun getMoodScore(): Int {
        return moodScore
    }

    fun setMoodScore(moodScore: Int) {
        //safety check
        if (moodScore < 1) {
            this.moodScore = 1
        } else if (moodScore > 10) {
            this.moodScore = 10
        } else { //otherwise set it to what the user put
            this.moodScore = moodScore
        }
    }

    val moodColor: Int
        get() {
            if (moodScore <= 3) {
                return Color.RED
            } else if (moodScore <= 6) {
                return Color.YELLOW
            } else {
                return Color.GREEN
            }
        }

    val moodDescription: String
        get() {
            if (moodScore <= 2) {
                return "Very Low"
            } else if (moodScore <= 4) {
                return "Low"
            } else if (moodScore <= 6) {
                return "Neutral"
            } else if (moodScore <= 8) {
                return "Good"
            } else {
                return "Excellent"
            }
        }

    override fun toString(): String {
        return "MoodEntry{" +
                "date=" + date +
                ", moodScore=" + moodScore +
                ", journalText='" + journalText + '\'' +
                '}'
    }
}