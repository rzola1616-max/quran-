package com.example.feature.hifz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.database.AyahEntity
import com.example.core.database.HifzItemEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HifzScreen(
    hifzItems: List<HifzItemEntity>,
    sampleAyahs: List<AyahEntity>,
    onSaveRevision: (Int, Int, Int, String, String, Boolean, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("new_lesson") } // "new_lesson", "sabqi", "manzil", "weak_sections"
    var selectedAyah by remember { mutableStateOf(sampleAyahs.firstOrNull()) }

    var isTextHidden by remember { mutableStateOf(true) }
    var revealedWordsCount by remember { mutableIntStateOf(0) }
    var recordedDifficulty by remember { mutableStateOf<String?>(null) }
    var showMistakeDialog by remember { mutableStateOf(false) }
    var customMistakeNote by remember { mutableStateOf("") }
    var showRangePicker by remember { mutableStateOf(false) }

    LaunchedEffect(sampleAyahs) {
        if (selectedAyah == null && sampleAyahs.isNotEmpty()) {
            selectedAyah = sampleAyahs.first()
        }
    }

    val currentAyah = selectedAyah ?: AyahEntity(
        ayahId = 112001,
        surahNumber = 112,
        ayahNumber = 1,
        textUthmani = "قُلْ هُوَ اللَّهُ أَحَدٌ",
        textIndoPak = "قُلْ هُوَ اللهُ اَحَدٌ",
        translationEn = "Say, \"He is Allah, [who is] One,",
        translationUr = "آپ فرما دیجیے کہ وہ اللہ ایک ہے۔",
        juz = 30, page = 604, ruku = 1
    )

    val words = remember(currentAyah) {
        currentAyah.textUthmani.split(" ").filter { it.isNotBlank() }
    }

    val displayedRevisionItems = remember(hifzItems, selectedCategory) {
        if (selectedCategory == "weak_sections") {
            hifzItems.filter { it.difficulty == "hard" || it.mistakeCount > 0 || it.mistakeNote.isNotBlank() }
        } else {
            hifzItems.filter { it.category == selectedCategory }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column {
                Text(
                    text = "Hifz & Memorization",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Recall verses from memory, reveal hints, and track your revision discipline.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryGrey
                )
            }
        }

        // Hifz Method Category Chips (New Lesson, Sabqi, Manzil, Review Weak Sections)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == "new_lesson",
                    onClick = {
                        selectedCategory = "new_lesson"
                        isTextHidden = true
                        revealedWordsCount = 0
                    },
                    label = { Text("New Sabaq") }
                )
                FilterChip(
                    selected = selectedCategory == "sabqi",
                    onClick = {
                        selectedCategory = "sabqi"
                        isTextHidden = true
                        revealedWordsCount = 0
                    },
                    label = { Text("Recent Sabqi") }
                )
                FilterChip(
                    selected = selectedCategory == "manzil",
                    onClick = {
                        selectedCategory = "manzil"
                        isTextHidden = true
                        revealedWordsCount = 0
                    },
                    label = { Text("Manzil") }
                )
                FilterChip(
                    selected = selectedCategory == "weak_sections",
                    onClick = {
                        selectedCategory = "weak_sections"
                        isTextHidden = true
                        revealedWordsCount = 0
                    },
                    label = { Text("Weak Sections") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFFEBEE))
                )
            }
        }

        // Ayah Selector & Range Picker
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected: Surah ${currentAyah.surahNumber} : Ayah ${currentAyah.ayahNumber}",
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                OutlinedButton(
                    onClick = { showRangePicker = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Choose Verse")
                }
            }
        }

        // Memorization Recall Canvas
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("hifz_recall_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(8.dp), color = SoftJade) {
                            Text(
                                text = "Surah ${currentAyah.surahNumber} : ${currentAyah.ayahNumber}",
                                fontWeight = FontWeight.Bold,
                                color = DeepEmerald,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = {
                            isTextHidden = !isTextHidden
                            if (!isTextHidden) revealedWordsCount = words.size
                            else revealedWordsCount = 0
                        }) {
                            Icon(
                                imageVector = if (isTextHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Visibility",
                                tint = ForestGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Verse Words with tap-to-reveal or hidden boxes
                    if (isTextHidden && revealedWordsCount == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(ForestGreen.copy(alpha = 0.08f))
                                .clickable {
                                    revealedWordsCount = 1 // reveal first word as hint
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, tint = ForestGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Ayah is hidden. Tap to reveal first word hint.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        // Display revealed words with remaining words masked
                        val displayWords = words.mapIndexed { index, word ->
                            if (!isTextHidden || index < revealedWordsCount) word else "•••••"
                        }.joinToString(" ")

                        Text(
                            text = displayWords,
                            fontSize = 28.sp,
                            lineHeight = 44.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = DeepEmerald,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Hint Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                revealedWordsCount = (revealedWordsCount + 1).coerceAtMost(words.size)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reveal +1 Word")
                        }
                        OutlinedButton(
                            onClick = {
                                isTextHidden = false
                                revealedWordsCount = words.size
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reveal All")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = LightSurfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Self-Evaluation: How well did you recall?
                    Text(
                        text = "How was your recall from memory?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                recordedDifficulty = "easy"
                                onSaveRevision(
                                    currentAyah.ayahId,
                                    currentAyah.surahNumber,
                                    currentAyah.ayahNumber,
                                    selectedCategory,
                                    "easy",
                                    false,
                                    ""
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (recordedDifficulty == "easy") DeepEmerald else ForestGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Easy (7d)", color = Color.White)
                        }

                        Button(
                            onClick = {
                                recordedDifficulty = "okay"
                                onSaveRevision(
                                    currentAyah.ayahId,
                                    currentAyah.surahNumber,
                                    currentAyah.ayahNumber,
                                    selectedCategory,
                                    "okay",
                                    false,
                                    ""
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (recordedDifficulty == "okay") DeepEmerald else MutedGold
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Okay (3d)", color = Color.White)
                        }

                        Button(
                            onClick = {
                                recordedDifficulty = "hard"
                                showMistakeDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (recordedDifficulty == "hard") DeepEmerald else ErrorRed
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Hard (1d)", color = Color.White)
                        }
                    }

                    if (recordedDifficulty != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Saved! Scheduled for next revision interval.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Mistake Notebook & Saved Revision Schedule
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory == "weak_sections") "Weak Sections (${displayedRevisionItems.size})" else "Revision Items (${displayedRevisionItems.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (displayedRevisionItems.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedCategory == "weak_sections") "Alhamdulillah! No weak sections logged yet." else "No verses logged in this category yet. Self-evaluate any verse above to track revision.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryGrey,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(displayedRevisionItems, key = { it.ayahId }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val target = sampleAyahs.find { it.ayahId == item.ayahId }
                            if (target != null) {
                                selectedAyah = target
                                isTextHidden = true
                                revealedWordsCount = 0
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Surah ${item.surahNumber} : Ayah ${item.ayahNumber}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "Difficulty: ${item.difficulty.replaceFirstChar { it.uppercase() }}${if (item.mistakeNote.isNotBlank()) " • Note: ${item.mistakeNote}" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryGrey
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (item.mistakeCount > 0 || item.difficulty == "hard") Color(0xFFFFEBEE) else SoftJade
                        ) {
                            Text(
                                text = if (item.mistakeCount > 0 || item.difficulty == "hard") "Needs Review" else "Strong",
                                color = if (item.mistakeCount > 0 || item.difficulty == "hard") ErrorRed else DeepEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Mistake Note Dialog
    if (showMistakeDialog) {
        AlertDialog(
            onDismissRequest = { showMistakeDialog = false },
            title = { Text("Log to Mistake Notebook") },
            text = {
                Column {
                    Text("Specify what was difficult or where you stumbled:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customMistakeNote,
                        onValueChange = { customMistakeNote = it },
                        placeholder = { Text("e.g. Hesitated on continuation, misread vowel...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveRevision(
                            currentAyah.ayahId,
                            currentAyah.surahNumber,
                            currentAyah.ayahNumber,
                            selectedCategory,
                            "hard",
                            true,
                            customMistakeNote
                        )
                        showMistakeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Save to Notebook", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMistakeDialog = false }) {
                    Text("Skip Note")
                }
            }
        )
    }

    // Range / Verse Picker Dialog
    if (showRangePicker) {
        AlertDialog(
            onDismissRequest = { showRangePicker = false },
            title = { Text("Select Verse for Memorization") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(sampleAyahs) { ayah ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAyah = ayah
                                    isTextHidden = true
                                    revealedWordsCount = 0
                                    showRangePicker = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Surah ${ayah.surahNumber}:${ayah.ayahNumber} — ${ayah.textUthmani.take(24)}...",
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRangePicker = false }) {
                    Text("Close")
                }
            }
        )
    }
}
