package com.example.group_project.repository

import com.example.group_project.MoodEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MoodRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val moodsCollection = db.collection("moods")

    fun saveMood(moodEntry: MoodEntry, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        moodsCollection.add(moodEntry)
            .addOnSuccessListener { docRef ->
                moodEntry.id = docRef.id
                onSuccess(docRef.id)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getMoodsForUser(userId: String, onSuccess: (List<MoodEntry>) -> Unit, onFailure: (Exception) -> Unit) {
        moodsCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val moods = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MoodEntry::class.java)?.apply {
                        id = doc.id
                    }
                }
                onSuccess(moods)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getMoodsByDateRange(
        userId: String,
        startTimestamp: Long,
        endTimestamp: Long,
        onSuccess: (List<MoodEntry>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        moodsCollection
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            .whereLessThanOrEqualTo("timestamp", endTimestamp)
            .get()
            .addOnSuccessListener { snapshot ->
                val moods = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MoodEntry::class.java)?.apply {
                        id = doc.id
                    }
                }
                onSuccess(moods)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getNewestMoodPerUserForDay(
        startTimestamp: Long,
        endTimestamp: Long,
        onSuccess: (List<Double>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        moodsCollection
            .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            .whereLessThanOrEqualTo("timestamp", endTimestamp)
            .get()
            .addOnSuccessListener { snapshot ->

                val moodEntries = snapshot.documents.mapNotNull { document ->
                    val moodEntry = document.toObject(MoodEntry::class.java)
                    moodEntry?.apply { id = document.id }
                }

                // Group by userId
                val groupUsers = moodEntries.groupBy { moodEntry -> moodEntry.userId }

                // Take newest entry per user
                val newestUserEntries = groupUsers.mapNotNull { (_, moodsForUser) ->
                    val newestMood = moodsForUser.maxByOrNull { mood -> mood.timestamp }
                    newestMood?.getMoodScore()?.toDouble()
                }

                onSuccess(newestUserEntries)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }







}
