package com.example.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "AudioPlayerService"

enum class QariReciter(val displayName: String, val subfolder: String) {
    ALAFASY("Mishary Rashid Alafasy", "Alafasy_128kbps"),
    ABDUL_BASIT("Abdul Basit Mujawwad", "Abdul_Basit_Mujawwad_128kbps"),
    AL_GHAMDI("Saad Al-Ghamdi", "Ghamadi_40kbps"),
    AL_HUSARY("Mahmoud Khalil Al-Husary", "Husary_128kbps")
}

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val currentSurah: Int = 1,
    val currentAyah: Int = 1,
    val speed: Float = 1.0f,
    val isRepeatEnabled: Boolean = false,
    val selectedReciter: QariReciter = QariReciter.ALAFASY,
    val errorMessage: String? = null
)

class AudioPlayerService(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val _state = MutableStateFlow(AudioPlaybackState())
    val state: StateFlow<AudioPlaybackState> = _state.asStateFlow()

    fun playAyah(surah: Int, ayah: Int, onComplete: () -> Unit = {}) {
        stop()

        val surahFormatted = String.format("%03d", surah)
        val ayahFormatted = String.format("%03d", ayah)
        val reciter = _state.value.selectedReciter
        val audioUrl = "https://everyayah.com/data/${reciter.subfolder}/${surahFormatted}${ayahFormatted}.mp3"

        _state.value = _state.value.copy(
            isPlaying = true,
            currentSurah = surah,
            currentAyah = ayah,
            errorMessage = null
        )

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                setOnPreparedListener { mp ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            mp.playbackParams = PlaybackParams().apply { speed = _state.value.speed }
                        } catch (e: Exception) {
                            Log.w(TAG, "Speed adjustment not supported: ${e.message}")
                        }
                    }
                    mp.start()
                    _state.value = _state.value.copy(isPlaying = true)
                }
                setOnCompletionListener {
                    if (_state.value.isRepeatEnabled) {
                        it.seekTo(0)
                        it.start()
                    } else {
                        _state.value = _state.value.copy(isPlaying = false)
                        onComplete()
                    }
                }
                setOnErrorListener { _, what, extra ->
                    Log.w(TAG, "Media player playback error: what=$what, extra=$extra (airplane mode or network offline)")
                    _state.value = _state.value.copy(
                        isPlaying = false,
                        errorMessage = "Audio playback requires internet connection or offline downloaded pack."
                    )
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start audio playback", e)
            _state.value = _state.value.copy(
                isPlaying = false,
                errorMessage = "Audio playback could not be initialized."
            )
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _state.value = _state.value.copy(isPlaying = false)
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            it.start()
            _state.value = _state.value.copy(isPlaying = true)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing media player: ${e.message}")
        } finally {
            mediaPlayer = null
            _state.value = _state.value.copy(isPlaying = false)
        }
    }

    fun toggleRepeat() {
        _state.value = _state.value.copy(isRepeatEnabled = !_state.value.isRepeatEnabled)
    }

    fun setSpeed(speed: Float) {
        _state.value = _state.value.copy(speed = speed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.playbackParams = PlaybackParams().apply { this.speed = speed }
            } catch (e: Exception) {
                Log.w(TAG, "Could not set playback speed: ${e.message}")
            }
        }
    }

    fun setReciter(reciter: QariReciter) {
        _state.value = _state.value.copy(selectedReciter = reciter)
        if (_state.value.isPlaying) {
            playAyah(_state.value.currentSurah, _state.value.currentAyah)
        }
    }
}
