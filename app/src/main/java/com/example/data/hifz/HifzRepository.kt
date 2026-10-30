package com.example.data.hifz

import android.content.Context
import com.example.core.database.AppDatabase
import com.example.core.database.HifzItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HifzRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val progressDao = db.userProgressDao()

    fun getAllHifzItems(): Flow<List<HifzItemEntity>> =
        progressDao.getAllHifzItems()

    suspend fun saveHifzRevision(
        ayahId: Int,
        surahNumber: Int,
        ayahNumber: Int,
        category: String,
        difficulty: String,
        hasMistake: Boolean,
        mistakeNote: String = ""
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val intervalDays = when (difficulty) {
            "easy" -> 7
            "okay" -> 3
            else -> 1
        }
        val nextDue = now + (intervalDays * 24 * 60 * 60 * 1000L)

        val item = HifzItemEntity(
            ayahId = ayahId,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            category = category,
            difficulty = difficulty,
            mistakeCount = if (hasMistake) 1 else 0,
            mistakeNote = mistakeNote,
            lastReviewedTimestamp = now,
            nextReviewDueTimestamp = nextDue
        )
        progressDao.insertOrUpdateHifzItem(item)
    }

    suspend fun removeHifzItem(ayahId: Int) = withContext(Dispatchers.IO) {
        progressDao.deleteHifzItem(ayahId)
    }
}
