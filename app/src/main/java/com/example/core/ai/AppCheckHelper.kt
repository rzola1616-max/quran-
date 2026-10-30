package com.example.core.ai

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

private const val TAG = "AppCheckInit"
private const val DEBUG_TOKEN_EXTRA = "FIREBASE_APPCHECK_DEBUG_TOKEN"
private const val DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store."
private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
private const val RECAPTCHA_SITE_KEY_RES = "recaptcha_site_key"

fun configureAppCheck(context: Context, intent: Intent?) {
    try {
        var app: FirebaseApp? = try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                FirebaseApp.getInstance()
            } else {
                FirebaseApp.initializeApp(context)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Standard Firebase initialization note: ${e.message}")
            null
        }

        if (app == null) {
            try {
                // Initialize fallback FirebaseApp so Firebase services and AppCheck can function gracefully offline
                val options = FirebaseOptions.Builder()
                    .setApplicationId(context.packageName)
                    .setApiKey("AIzaSyQuranAppFallbackKeyForOffline")
                    .setProjectId(context.packageName.replace('.', '-'))
                    .build()
                app = FirebaseApp.initializeApp(context, options)
            } catch (e: Exception) {
                Log.w(TAG, "Fallback Firebase initialization note: ${e.message}")
            }
        }

        if (app == null) {
            Log.w(TAG, "FirebaseApp could not be initialized; continuing safely without App Check.")
            return
        }

        if (BuildConfig.DEBUG) {
            val debugToken = intent?.getStringExtra(DEBUG_TOKEN_EXTRA)
            if (!debugToken.isNullOrEmpty()) {
                val persistenceKey = app.persistenceKey
                val prefs = context.getSharedPreferences(
                    "$DEBUG_STORE_PREFIX$persistenceKey",
                    Context.MODE_PRIVATE
                )
                prefs.edit()
                    .putString(DEBUG_SECRET_KEY, debugToken)
                    .apply()
                Log.d(TAG, "App Check debug secret configured successfully.")
            } else {
                Log.w(TAG, "No debug token found in Intent extra.")
            }

            try {
                FirebaseAppCheck.getInstance(app).installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "DebugAppCheckProviderFactory installed.")
            } catch (e: Exception) {
                Log.w(TAG, "DebugAppCheckProviderFactory note: ${e.message}")
            }
        } else {
            val resId = context.resources.getIdentifier(RECAPTCHA_SITE_KEY_RES, "string", context.packageName)
            if (resId != 0) {
                val siteKey = context.getString(resId)
                if (siteKey.isNotBlank()) {
                    try {
                        FirebaseAppCheck.getInstance(app).installAppCheckProviderFactory(
                            RecaptchaAppCheckProviderFactory.getInstance(siteKey)
                        )
                        Log.d(TAG, "RecaptchaAppCheckProviderFactory installed.")
                    } catch (e: Exception) {
                        Log.w(TAG, "RecaptchaAppCheckProviderFactory note: ${e.message}")
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.w(TAG, "App Check initialization handled safely: ${e.message}")
    }
}

