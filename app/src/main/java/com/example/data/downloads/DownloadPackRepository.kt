package com.example.data.downloads

import android.content.Context
import com.example.core.database.AppDatabase
import com.example.core.database.DownloadPackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DownloadPackRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val downloadDao = db.downloadDao()

    suspend fun ensurePacksInitialized() = withContext(Dispatchers.IO) {
        val defaultPacks = listOf(
            DownloadPackEntity(
                packId = "pack_trans_ur",
                title = "Urdu Translation (Fateh Muhammad Jalandhari)",
                type = "translation",
                sizeMb = 7.9,
                isInstalled = true,
                version = "1.2",
                license = "Public Domain / Tanzil"
            ),
            DownloadPackEntity(
                packId = "pack_trans_en",
                title = "English Translation (Saheeh International)",
                type = "translation",
                sizeMb = 8.2,
                isInstalled = true,
                version = "1.1",
                license = "Licensed / Tanzil"
            ),
            DownloadPackEntity(
                packId = "pack_audio_mishary",
                title = "Reciter: Mishary Rashid Alafasy (128kbps)",
                type = "audio",
                sizeMb = 120.0,
                isInstalled = false,
                version = "2.0",
                license = "EveryAyah CC License"
            ),
            DownloadPackEntity(
                packId = "pack_audio_abdulbasit",
                title = "Reciter: Abdul Basit Mujawwad (High Fidelity)",
                type = "audio",
                sizeMb = 145.0,
                isInstalled = false,
                version = "1.0",
                license = "EveryAyah CC License"
            ),
            DownloadPackEntity(
                packId = "pack_lessons_audio",
                title = "Qaida & Tajweed Audio Examples Pack",
                type = "lesson",
                sizeMb = 24.5,
                isInstalled = true,
                version = "1.0",
                license = "Quran Companion Curriculum"
            ),
            DownloadPackEntity(
                packId = "pack_ai_model",
                title = "On-Device Recitation AI Model (ONNX)",
                type = "model",
                sizeMb = 42.0,
                isInstalled = true,
                version = "1.0",
                license = "Open Quran Speech Model"
            )
        )
        downloadDao.insertPacks(defaultPacks)
    }

    fun getAllPacks(): Flow<List<DownloadPackEntity>> = downloadDao.getAllPacks()

    suspend fun simulateDownload(packId: String) = withContext(Dispatchers.IO) {
        delay(1200) // Simulating checksum verification & download
        downloadDao.updatePackInstallation(packId, true)
    }

    suspend fun deletePack(packId: String) = withContext(Dispatchers.IO) {
        downloadDao.updatePackInstallation(packId, false)
    }
}
