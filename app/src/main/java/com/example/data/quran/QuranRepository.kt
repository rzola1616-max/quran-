package com.example.data.quran

import android.content.Context
import com.example.core.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class QuranRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val quranDao = db.quranDao()
    private val userProgressDao = db.userProgressDao()

    suspend fun ensureDataPopulated() = withContext(Dispatchers.IO) {
        if (quranDao.getSurahCount() == 0) {
            val surahs = VerifiedQuranData.getAll114Surahs()
            quranDao.insertSurahs(surahs)
        }
        if (quranDao.getAyahCount() < 5000) {
            val ayahs = VerifiedQuranData.getFullOfflineAyahs()
            quranDao.insertAyahs(ayahs)
            userProgressDao.setLastRead(
                LastReadEntity(
                    id = 1,
                    surahNumber = 1,
                    ayahNumber = 1,
                    surahName = "Al-Fatihah"
                )
            )
        }
    }

    fun getAllSurahs(): Flow<List<SurahEntity>> = quranDao.getAllSurahs()

    suspend fun getSurahByNumber(surahNumber: Int): SurahEntity? = withContext(Dispatchers.IO) {
        quranDao.getSurahByNumber(surahNumber)
    }

    fun getAyahsForSurah(surahNumber: Int): Flow<List<AyahEntity>> =
        quranDao.getAyahsForSurah(surahNumber)

    fun getAyahsForJuz(juzNumber: Int): Flow<List<AyahEntity>> =
        quranDao.getAyahsForJuz(juzNumber)

    fun searchAyahs(query: String): Flow<List<AyahEntity>> =
        quranDao.searchAyahs(query)

    suspend fun getAyahById(ayahId: Int): AyahEntity? = withContext(Dispatchers.IO) {
        quranDao.getAyahById(ayahId)
    }

    // Bookmarks
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = userProgressDao.getAllBookmarks()

    fun isBookmarked(ayahId: Int): Flow<Boolean> = userProgressDao.isBookmarked(ayahId)

    suspend fun toggleBookmark(ayah: AyahEntity, surahName: String) = withContext(Dispatchers.IO) {
        userProgressDao.deleteBookmarkByAyahId(ayah.ayahId)
        userProgressDao.insertBookmark(
            BookmarkEntity(
                ayahId = ayah.ayahId,
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumber,
                surahName = surahName
            )
        )
    }

    suspend fun removeBookmark(ayahId: Int) = withContext(Dispatchers.IO) {
        userProgressDao.deleteBookmarkByAyahId(ayahId)
    }

    // Notes
    fun getNotesForAyah(ayahId: Int): Flow<List<NoteEntity>> = userProgressDao.getNotesForAyah(ayahId)

    fun getAllNotes(): Flow<List<NoteEntity>> = userProgressDao.getAllNotes()

    suspend fun addNote(ayahId: Int, surahNumber: Int, ayahNumber: Int, surahName: String, text: String) =
        withContext(Dispatchers.IO) {
            userProgressDao.insertNote(
                NoteEntity(
                    ayahId = ayahId,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    surahName = surahName,
                    noteText = text
                )
            )
        }

    suspend fun deleteNote(noteId: Int) = withContext(Dispatchers.IO) {
        userProgressDao.deleteNote(noteId)
    }

    // Last Read
    fun getLastRead(): Flow<LastReadEntity?> = userProgressDao.getLastRead()

    suspend fun updateLastRead(surahNumber: Int, ayahNumber: Int, surahName: String) =
        withContext(Dispatchers.IO) {
            userProgressDao.setLastRead(
                LastReadEntity(
                    id = 1,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    surahName = surahName,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

    // Backup & Restore
    suspend fun exportUserDataToJson(): String = withContext(Dispatchers.IO) {
        val root = JSONObject()
        root.put("version", "1.0")
        root.put("exportedAt", System.currentTimeMillis())
        root.toString(2)
    }
}
