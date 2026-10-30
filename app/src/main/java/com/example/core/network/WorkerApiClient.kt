package com.example.core.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "WorkerApiClient"

data class WorkerHealthResponse(
    val status: String,
    val apiVersion: String,
    val latencyMs: Long,
    val service: String? = null
)

data class WorkerAssistantResult(
    val answer: String,
    val references: List<String>,
    val isAiGenerated: Boolean,
    val disclaimer: String,
    val isFromCloudflareWorker: Boolean
)

class WorkerApiClient(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(8, TimeUnit.SECONDS)
        .build()

    // Default Cloudflare Worker URL (can be customized by user in Settings)
    var customWorkerUrl: String? = null

    fun getEffectiveBaseUrl(): String {
        return customWorkerUrl?.trimEnd('/')?.ifBlank { null }
            ?: "https://quran-companion.pages.dev"
    }

    suspend fun checkHealth(baseUrl: String = getEffectiveBaseUrl()): Result<WorkerHealthResponse> =
        withContext(Dispatchers.IO) {
            val start = System.currentTimeMillis()
            try {
                val url = "${baseUrl.trimEnd('/')}/v1/health"
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    val latency = System.currentTimeMillis() - start
                    if (response.isSuccessful) {
                        val body = response.body?.string().orEmpty()
                        val json = JSONObject(body)
                        Result.success(
                            WorkerHealthResponse(
                                status = json.optString("status", "ok"),
                                apiVersion = json.optString("apiVersion", "1"),
                                latencyMs = latency,
                                service = json.optString("service", "Cloudflare Worker")
                            )
                        )
                    } else {
                        Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Health check failed: ${e.message}")
                Result.failure(e)
            }
        }

    suspend fun askAssistant(
        question: String,
        language: String = "ur",
        referenceIds: List<String> = emptyList(),
        baseUrl: String = getEffectiveBaseUrl()
    ): Result<WorkerAssistantResult> = withContext(Dispatchers.IO) {
        try {
            val url = "${baseUrl.trimEnd('/')}/v1/assistant/ask"
            val payload = JSONObject().apply {
                put("question", question)
                put("language", language)
                put("referenceIds", JSONArray(referenceIds))
            }

            val body = payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val respStr = response.body?.string().orEmpty()
                    val json = JSONObject(respStr)
                    val answer = json.optString("answer", "")
                    val refs = mutableListOf<String>()
                    val refsArray = json.optJSONArray("references")
                    if (refsArray != null) {
                        for (i in 0 until refsArray.length()) {
                            refs.add(refsArray.getString(i))
                        }
                    }

                    Result.success(
                        WorkerAssistantResult(
                            answer = answer,
                            references = refs,
                            isAiGenerated = json.optBoolean("isAiGenerated", true),
                            disclaimer = json.optString(
                                "disclaimer",
                                "This explanation supports learning and is not a substitute for qualified scholarship."
                            ),
                            isFromCloudflareWorker = true
                        )
                    )
                } else {
                    Result.failure(Exception("Server returned status ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Worker askAssistant call failed: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun submitReport(
        requestId: String,
        category: String,
        userNote: String,
        consentedExcerpt: String,
        baseUrl: String = getEffectiveBaseUrl()
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val url = "${baseUrl.trimEnd('/')}/v1/reports"
            val payload = JSONObject().apply {
                put("requestId", requestId)
                put("category", category)
                put("userNote", userNote)
                put("consentedExcerpt", consentedExcerpt)
                put("appVersion", 1)
            }

            val body = payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                Result.success(response.isSuccessful)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
