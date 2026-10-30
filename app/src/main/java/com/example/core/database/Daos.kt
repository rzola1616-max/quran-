package com.example.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {
    @Query("SELECT * FROM surahs ORDER BY surahNumber ASC")
    fun getAllSurahs(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surahs WHERE surahNumber = :surahNumber LIMIT 1")
    suspend fun getSurahByNumber(surahNumber: Int): SurahEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<SurahEntity>)

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getAyahsForSurah(surahNumber: Int): Flow<List<AyahEntity>>

    @Query("SELECT * FROM ayahs WHERE ayahId = :ayahId LIMIT 1")
    suspend fun getAyahById(ayahId: Int): AyahEntity?

    @Query("SELECT * FROM ayahs WHERE juz = :juzNumber ORDER BY surahNumber ASC, ayahNumber ASC")
    fun getAyahsForJuz(juzNumber: Int): Flow<List<AyahEntity>>

    @Query("SELECT * FROM ayahs WHERE textUthmani LIKE '%' || :query || '%' OR translationEn LIKE '%' || :query || '%' OR translationUr LIKE '%' || :query || '%' LIMIT 100")
    fun searchAyahs(query: String): Flow<List<AyahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyahs(ayahs: List<AyahEntity>)

    @Query("SELECT COUNT(*) FROM surahs")
    suspend fun getSurahCount(): Int

    @Query("SELECT COUNT(*) FROM ayahs")
    suspend fun getAyahCount(): Int
}

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE ayahId = :ayahId")
    suspend fun deleteBookmarkByAyahId(ayahId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE ayahId = :ayahId)")
    fun isBookmarked(ayahId: Int): Flow<Boolean>

    // Notes
    @Query("SELECT * FROM notes WHERE ayahId = :ayahId ORDER BY createdAt DESC")
    fun getNotesForAyah(ayahId: Int): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM last_read WHERE id = 1 LIMIT 1")
    fun getLastRead(): Flow<LastReadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLastRead(lastRead: LastReadEntity)

    @Query("SELECT * FROM lesson_progress")
    fun getAllLessonProgress(): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT * FROM hifz_items ORDER BY lastReviewedTimestamp ASC")
    fun getAllHifzItems(): Flow<List<HifzItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHifzItem(item: HifzItemEntity)

    @Query("DELETE FROM hifz_items WHERE ayahId = :ayahId")
    suspend fun deleteHifzItem(ayahId: Int)

    @Query("DELETE FROM bookmarks")
    suspend fun clearBookmarks()

    @Query("DELETE FROM notes")
    suspend fun clearNotes()

    @Query("DELETE FROM hifz_items")
    suspend fun clearHifz()

    @Query("DELETE FROM lesson_progress")
    suspend fun clearLessonProgress()
}

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_packs")
    fun getAllPacks(): Flow<List<DownloadPackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPacks(packs: List<DownloadPackEntity>)

    @Query("UPDATE download_packs SET isInstalled = :installed WHERE packId = :packId")
    suspend fun updatePackInstallation(packId: String, installed: Boolean)
}

@Dao
interface AdminDao {
    // Reports
    @Query("SELECT * FROM ai_feedback_reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<AdminReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: AdminReportEntity)

    @Query("UPDATE ai_feedback_reports SET status = :status, resolutionNote = :resolutionNote WHERE id = :id")
    suspend fun resolveReport(id: String, status: String, resolutionNote: String)

    // Sources
    @Query("SELECT * FROM admin_sources")
    fun getAllSources(): Flow<List<AdminSourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<AdminSourceEntity>)

    @Query("UPDATE admin_sources SET licenseStatus = :status WHERE id = :id")
    suspend fun updateSourceStatus(id: String, status: String)

    // Lesson Reviews
    @Query("SELECT * FROM admin_lesson_reviews")
    fun getAllLessonReviews(): Flow<List<AdminLessonReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonReviews(reviews: List<AdminLessonReviewEntity>)

    @Query("UPDATE admin_lesson_reviews SET reviewStatus = :status, reviewNotes = :notes WHERE lessonId = :lessonId")
    suspend fun updateLessonReview(lessonId: String, status: String, notes: String)

    // Audit Log
    @Query("SELECT * FROM audit_log ORDER BY timestamp DESC")
    fun getAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}
