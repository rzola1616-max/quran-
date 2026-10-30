package com.example.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (language: String, script: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }
    var selectedLanguage by remember { mutableStateOf("ur") }
    var selectedScript by remember { mutableStateOf("uthmani") }
    var selectedGoal by remember { mutableStateOf("quran_reader") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Step indicators
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { idx ->
                        Box(
                            modifier = Modifier
                                .size(if (step == idx) 24.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (step == idx) ForestGreen else SoftJade)
                        )
                    }
                }

                TextButton(
                    onClick = { onFinishOnboarding(selectedLanguage, selectedScript) },
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text("Skip", fontWeight = FontWeight.Bold, color = ForestGreen)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (step) {
                0 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SoftJade,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = DeepEmerald,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Quran Companion",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                        Text(
                            text = "Learn, read, listen, and revise offline-first.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryGrey
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Choose your preferred language:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedLanguage == "ur") SoftJade else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLanguage = "ur" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("اردو (Urdu)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("اردو ترجمہ اور مکمل رہنمائی", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                }
                                RadioButton(selected = selectedLanguage == "ur", onClick = { selectedLanguage = "ur" })
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedLanguage == "en") SoftJade else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLanguage = "en" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("English", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("English translations and guides", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                }
                                RadioButton(selected = selectedLanguage == "en", onClick = { selectedLanguage = "en" })
                            }
                        }
                    }
                }

                1 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select Quran Script",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "You can change this anytime from reader settings.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryGrey
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedScript == "uthmani") SoftJade else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedScript = "uthmani" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Uthmani Script (الرسم العثماني)", fontWeight = FontWeight.Bold)
                                    Text("Standard Madinah Mushaf script", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", fontSize = 18.sp, color = DeepEmerald)
                                }
                                RadioButton(selected = selectedScript == "uthmani", onClick = { selectedScript = "uthmani" })
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedScript == "indopak") SoftJade else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedScript = "indopak" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Indo-Pak Script (رسم خط انڈو پاک)", fontWeight = FontWeight.Bold)
                                    Text("South Asian / Nastaliq font style", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِیْنَ", fontSize = 18.sp, color = DeepEmerald)
                                }
                                RadioButton(selected = selectedScript == "indopak", onClick = { selectedScript = "indopak" })
                            }
                        }
                    }
                }

                2 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SoftJade,
                            modifier = Modifier.size(76.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = DeepEmerald, modifier = Modifier.size(38.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Privacy & Offline-First Guarantee",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "• No user registration or sign-in is required to read or learn.\n• All bookmarks and progress stay on your device.\n• Microphone is only used during active practice sessions and is never stored.",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                            color = Charcoal
                        )
                    }
                }
            }

            // Bottom Continue / Finish button
            Button(
                onClick = {
                    if (step < 2) {
                        step++
                    } else {
                        onFinishOnboarding(selectedLanguage, selectedScript)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("onboarding_next_button")
            ) {
                Text(
                    text = if (step < 2) "Continue" else "Start Learning",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
