package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.app.QuranCompanionApp
import com.example.core.ai.configureAppCheck
import com.example.ui.theme.QuranCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration before UI or Firebase operations
        try {
            configureAppCheck(this, intent)
        } catch (_: Exception) {
            // Safe fallback ensuring app launches seamlessly even in offline or constrained environments
        }

        enableEdgeToEdge()

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            QuranCompanionTheme(darkTheme = isDarkTheme) {
                QuranCompanionApp(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = it }
                )
            }
        }
    }
}
