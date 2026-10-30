package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Quran Companion", appName)
  }

  @Test
  fun `configureAppCheck runs safely without crashing`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Must execute cleanly without throwing IllegalStateException
    com.example.core.ai.configureAppCheck(context, null)
  }
}

