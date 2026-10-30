package com.example.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey val surahNumber: Int,
    val nameArabic: String,
    val nameTransliteration: String,
    val nameTranslationEn: String,
    val nameTranslationUr: String,
    val ayahCount: Int,
    val revelationType: String, // Meccan or Medinan
    val startJuz: Int
)

@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey val ayahId: Int, // surah * 1000 + ayah
    val surahNumber: Int,
    val ayahNumber: Int,
    val textUthmani: String,
    val textIndoPak: String,
    val translationEn: String,
    val translationUr: String,
    val juz: Int,
    val page: Int,
    val ruku: Int,
    val sajdah: Boolean = false
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ayahId: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ayahId: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val noteText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "last_read")
data class LastReadEntity(
    @PrimaryKey val id: Int = 1,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val lastAttemptTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "hifz_items")
data class HifzItemEntity(
    @PrimaryKey val ayahId: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val category: String, // "new_lesson", "sabqi", "manzil"
    val difficulty: String = "okay", // "easy", "okay", "hard"
    val mistakeCount: Int = 0,
    val mistakeNote: String = "",
    val lastReviewedTimestamp: Long = System.currentTimeMillis(),
    val nextReviewDueTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "download_packs")
data class DownloadPackEntity(
    @PrimaryKey val packId: String,
    val title: String,
    val type: String, // "translation", "audio", "lesson", "model"
    val sizeMb: Double,
    val isInstalled: Boolean,
    val version: String,
    val license: String
)

@Entity(tableName = "ai_feedback_reports")
data class AdminReportEntity(
    @PrimaryKey val id: String,
    val requestId: String,
    val category: String, // "incorrect", "harmful", "unsourced", "unclear"
    val question: String,
    val consentedExcerpt: String,
    val userNote: String,
    val status: String = "new", // "new", "triaged", "resolved"
    val resolutionNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "admin_sources")
data class AdminSourceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // quran_text, translation, audio, lessons, model
    val publisher: String,
    val licenseName: String,
    val licenseUrl: String,
    val licenseStatus: String = "verified", // pending, verified, rejected
    val notes: String = ""
)

@Entity(tableName = "admin_lesson_reviews")
data class AdminLessonReviewEntity(
    @PrimaryKey val lessonId: String,
    val titleEn: String,
    val titleUr: String,
    val reviewStatus: String = "approved", // draft, in_review, changes_requested, approved
    val reviewNotes: String = "",
    val reviewedBy: String = "Teacher Reviewer"
)

@Entity(tableName = "audit_log")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val adminRole: String,
    val action: String,
    val entityType: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
