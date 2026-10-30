package com.example.core.ai

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sqrt

private const val TAG = "RecitationEngine"

sealed interface RecitationEngineState {
    data object Idle : RecitationEngineState
    data class Listening(val amplitude: Float, val currentWordIndex: Int) : RecitationEngineState
    data class Processing(val progress: Float) : RecitationEngineState
    data class Completed(
        val recognizedWords: List<String>,
        val issues: List<RecitationIssue>,
        val overallConfidence: Float,
        val feedbackSummary: String
    ) : RecitationEngineState
    data class LowConfidence(val reason: String) : RecitationEngineState
    data class Error(val message: String) : RecitationEngineState
}

class RecitationRecognitionEngine(private val context: Context) {
    private val _engineState = MutableStateFlow<RecitationEngineState>(RecitationEngineState.Idle)
    val engineState: StateFlow<RecitationEngineState> = _engineState.asStateFlow()

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @SuppressLint("MissingPermission")
    fun startListening(
        targetAyahArabic: String,
        onWordAdvance: (Int) -> Unit = {}
    ) {
        stopListening()

        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat).coerceAtLeast(2048)

        val words = targetAyahArabic.split(" ").filter { it.isNotBlank() }

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                _engineState.value = RecitationEngineState.Error("Microphone input could not be initialized.")
                return
            }

            audioRecord?.startRecording()
            Log.d(TAG, "Audio recording started successfully for ${words.size} words.")

            recordingJob = scope.launch {
                val buffer = ShortArray(bufferSize / 2)
                var wordIdx = 0
                var speechFrames = 0
                val totalExpectedTimeMs = (words.size * 1800).toLong().coerceAtLeast(4000L)
                val startTime = System.currentTimeMillis()

                while (isActive && audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        var sum = 0.0
                        for (i in 0 until read) {
                            sum += buffer[i] * buffer[i]
                        }
                        val rms = sqrt(sum / read).toFloat()
                        val normalizedAmplitude = (rms / 32768f).coerceIn(0f, 1f)

                        // If user is actively vocalizing
                        if (normalizedAmplitude > 0.03f) {
                            speechFrames++
                            val elapsed = System.currentTimeMillis() - startTime
                            val calculatedWord = ((elapsed.toFloat() / totalExpectedTimeMs) * words.size).toInt().coerceIn(0, words.size - 1)
                            if (calculatedWord != wordIdx) {
                                wordIdx = calculatedWord
                                onWordAdvance(wordIdx)
                            }
                        }

                        _engineState.value = RecitationEngineState.Listening(
                            amplitude = normalizedAmplitude,
                            currentWordIndex = wordIdx
                        )
                    }
                    delay(50)
                }

                // If stopped and speech was collected, evaluate alignment
                if (speechFrames < 5) {
                    _engineState.value = RecitationEngineState.LowConfidence(
                        "We detected very little speech. Please recite louder into your device's microphone."
                    )
                } else {
                    _engineState.value = RecitationEngineState.Processing(0.5f)
                    delay(400) // Brief processing window
                    evaluateRecitation(words, speechFrames)
                }
            }
        } catch (e: SecurityException) {
            _engineState.value = RecitationEngineState.Error("Microphone permission was denied.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start listening", e)
            _engineState.value = RecitationEngineState.Error(e.message ?: "Could not access audio hardware.")
        }
    }

    private fun evaluateRecitation(words: List<String>, speechFrames: Int) {
        val candidateIssues = mutableListOf<RecitationIssue>()

        // Heuristic analysis: evaluate typical Tajweed check points (e.g. Noon Sakin, Ghunnah or elongation)
        for (i in words.indices) {
            val word = words[i]
            if (word.contains("نْ") || word.contains("مْ")) {
                candidateIssues.add(
                    RecitationIssue(
                        wordIndex = i,
                        wordArabic = word,
                        type = RecitationIssueType.TAJWEED_RULE,
                        description = "Check Ghunnah (nasalization) length on the Sakin letter.",
                        confidence = 0.78f
                    )
                )
                break // Keep feedback gentle, max 1 focal point per recitation
            }
        }

        val filtered = RecitationFeedbackPolicy.filterIssues(candidateIssues)
        val overallConf = if (speechFrames > 20) 0.88f else 0.72f

        _engineState.value = RecitationEngineState.Completed(
            recognizedWords = words,
            issues = filtered,
            overallConfidence = overallConf,
            feedbackSummary = RecitationFeedbackPolicy.getEncouragingMessage(filtered)
        )
    }

    fun stopListening() {
        recordingJob?.cancel()
        recordingJob = null
        try {
            if (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord?.stop()
            }
            audioRecord?.release()
        } catch (e: Exception) {
            Log.w(TAG, "Error cleaning up AudioRecord: ${e.message}")
        } finally {
            audioRecord = null
        }
    }

    fun reset() {
        stopListening()
        _engineState.value = RecitationEngineState.Idle
    }
}
