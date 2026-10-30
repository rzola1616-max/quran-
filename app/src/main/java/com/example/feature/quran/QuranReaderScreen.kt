package com.example.feature.quran

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.audio.AudioPlaybackState
import com.example.core.audio.QariReciter
import com.example.core.database.AyahEntity
import com.example.core.database.BookmarkEntity
import com.example.core.database.NoteEntity
import com.example.core.database.SurahEntity
import com.example.core.designsystem.AyahCard
import com.example.data.quran.QuranWordByWordData
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surah: SurahEntity?,
    ayahs: List<AyahEntity>,
    initialAyahNumber: Int = 1,
    bookmarks: List<BookmarkEntity>,
    notes: List<NoteEntity>,
    onToggleBookmark: (AyahEntity, String) -> Unit,
    onAddNote: (Int, Int, Int, String, String) -> Unit,
    onPlayAyahAudio: (Int, Int) -> Unit,
    onPauseAudio: () -> Unit,
    onResumeAudio: () -> Unit,
    onToggleRepeat: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onSetReciter: (QariReciter) -> Unit,
    playbackState: AudioPlaybackState,
    onPracticeWithAi: (AyahEntity) -> Unit,
    onAyahVisible: (Int, Int, String) -> Unit,
    onJumpToAyah: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    var showIndoPak by remember { mutableStateOf(false) }
    var activeTranslation by remember { mutableStateOf("ur") } // "ur", "en", "none"
    var fontSizeSp by remember { mutableFloatStateOf(26f) }
    var readingSurfaceTheme by remember { mutableStateOf("warm_ivory") } // warm_ivory, sepia, dark, high_contrast

    var showSettingsSheet by remember { mutableStateOf(false) }
    var showJumpDialog by remember { mutableStateOf(false) }
    var selectedAyahForSheet by remember { mutableStateOf<AyahEntity?>(null) }
    var showNoteInputDialog by remember { mutableStateOf(false) }
    var noteInputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // Scroll to initial Ayah if specified
    LaunchedEffect(initialAyahNumber, ayahs) {
        if (ayahs.isNotEmpty() && initialAyahNumber > 1) {
            val targetIdx = ayahs.indexOfFirst { it.ayahNumber == initialAyahNumber }
            if (targetIdx >= 0) {
                listState.scrollToItem(targetIdx + 1) // +1 for header
            }
        }
    }

    // Report last read on first visible ayah
    LaunchedEffect(listState.firstVisibleItemIndex, ayahs) {
        if (ayahs.isNotEmpty() && surah != null) {
            val currentIdx = (listState.firstVisibleItemIndex - 1).coerceIn(0, ayahs.size - 1)
            val currentAyah = ayahs[currentIdx]
            onAyahVisible(surah.surahNumber, currentAyah.ayahNumber, surah.nameTransliteration)
        }
    }

    val backgroundColor = when (readingSurfaceTheme) {
        "sepia" -> SepiaBackground
        "dark" -> DarkBackground
        "high_contrast" -> Color.Black
        else -> WarmIvory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = surah?.nameTransliteration ?: "Surah",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${surah?.revelationType} • ${surah?.ayahCount} Ayahs • Juz ${surah?.startJuz}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_button")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Jump to Ayah button
                    IconButton(
                        onClick = { showJumpDialog = true },
                        modifier = Modifier.testTag("btn_jump_ayah")
                    ) {
                        Icon(Icons.Default.PinDrop, contentDescription = "Jump to Ayah", tint = ForestGreen)
                    }

                    // Script toggle button
                    TextButton(
                        onClick = { showIndoPak = !showIndoPak },
                        modifier = Modifier.testTag("toggle_script_button")
                    ) {
                        Text(
                            text = if (showIndoPak) "Indo-Pak" else "Uthmani",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }

                    // Reading Settings
                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("reader_settings_button")
                    ) {
                        Icon(imageVector = Icons.Outlined.FormatSize, contentDescription = "Reading Settings", tint = ForestGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (playbackState.isPlaying || playbackState.currentAyah > 0) {
                AudioBottomBar(
                    playbackState = playbackState,
                    onPauseAudio = onPauseAudio,
                    onResumeAudio = onResumeAudio,
                    onToggleRepeat = onToggleRepeat,
                    onSetSpeed = onSetSpeed,
                    onSetReciter = onSetReciter,
                    onPrevAyah = {
                        val prev = (playbackState.currentAyah - 1).coerceAtLeast(1)
                        onPlayAyahAudio(playbackState.currentSurah, prev)
                    },
                    onNextAyah = {
                        val maxCount = surah?.ayahCount ?: 286
                        val next = (playbackState.currentAyah + 1).coerceAtMost(maxCount)
                        onPlayAyahAudio(playbackState.currentSurah, next)
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Surah Decorative Header
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftJade),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah?.nameArabic ?: "",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${surah?.nameTransliteration} (${surah?.nameTranslationEn})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Charcoal
                        )
                        Text(
                            text = "${surah?.nameTranslationUr} • Juz ${surah?.startJuz}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )

                        // Bismillah (except Surah 9 At-Tawbah)
                        if (surah?.surahNumber != 9) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal,
                                color = ForestGreen,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Ayahs
            items(ayahs, key = { it.ayahId }) { ayah ->
                val isBookmarked = bookmarks.any { it.ayahId == ayah.ayahId }
                val hasNotes = notes.any { it.ayahId == ayah.ayahId }

                AyahCard(
                    ayah = ayah,
                    surahName = surah?.nameTransliteration ?: "",
                    isBookmarked = isBookmarked,
                    hasNotes = hasNotes,
                    onToggleBookmark = {
                        onToggleBookmark(ayah, surah?.nameTransliteration ?: "")
                    },
                    onPlayAudio = {
                        onPlayAyahAudio(ayah.surahNumber, ayah.ayahNumber)
                    },
                    onPracticeWithAi = {
                        onPracticeWithAi(ayah)
                    },
                    onTapAyah = {
                        selectedAyahForSheet = ayah
                    },
                    showIndoPak = showIndoPak,
                    fontSizeSp = fontSizeSp,
                    activeTranslation = activeTranslation
                )
            }
        }
    }

    // Ayah Interactive Details Bottom Sheet (Word-by-word, Notes, Copy, Tafsir)
    selectedAyahForSheet?.let { ayah ->
        val isBookmarked = bookmarks.any { it.ayahId == ayah.ayahId }
        val ayahNotes = notes.filter { it.ayahId == ayah.ayahId }
        val wordBreakdown = remember(ayah.ayahId) { QuranWordByWordData.getWordBreakdown(ayah.ayahId) }

        ModalBottomSheet(
            onDismissRequest = { selectedAyahForSheet = null },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${surah?.nameTransliteration} : Ayah ${ayah.ayahNumber}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "Juz ${ayah.juz} • Page ${ayah.page}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )
                    }
                    IconButton(onClick = {
                        onToggleBookmark(ayah, surah?.nameTransliteration ?: "")
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) MutedGold else SecondaryGrey
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            selectedAyahForSheet = null
                            onPlayAyahAudio(ayah.surahNumber, ayah.ayahNumber)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Listen")
                    }

                    OutlinedButton(
                        onClick = {
                            selectedAyahForSheet = null
                            onPracticeWithAi(ayah)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Practice")
                    }

                    OutlinedButton(
                        onClick = {
                            val copyContent = buildString {
                                append(ayah.textUthmani).append("\n\n")
                                if (ayah.translationUr.isNotBlank()) append(ayah.translationUr).append("\n")
                                if (ayah.translationEn.isNotBlank()) append(ayah.translationEn).append("\n")
                                append("— Holy Quran (${surah?.nameTransliteration} ${surah?.surahNumber}:${ayah.ayahNumber} - Quran Companion)")
                            }
                            clipboardManager.setText(AnnotatedString(copyContent))
                            Toast.makeText(context, "Ayah copied with attribution", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Word-by-Word Breakdown if available
                if (wordBreakdown.isNotEmpty()) {
                    Text(
                        text = "Word-by-Word Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepEmerald
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(wordBreakdown) { wb ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SoftJade.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = wb.arabic, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepEmerald)
                                    Text(text = wb.transliteration, fontSize = 11.sp, color = SecondaryGrey)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = wb.urdu, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ForestGreen)
                                    Text(text = wb.english, fontSize = 11.sp, color = Charcoal)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Reflection Notes for this verse
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal Reflection Notes (${ayahNotes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = {
                        noteInputText = ""
                        showNoteInputDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Note")
                    }
                }

                if (ayahNotes.isEmpty()) {
                    Text(
                        text = "No notes added yet. Record reflections or teacher guidance for this verse.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryGrey
                    )
                } else {
                    ayahNotes.forEach { n ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LightSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = n.noteText,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Add Note Dialog
    if (showNoteInputDialog && selectedAyahForSheet != null) {
        val ayah = selectedAyahForSheet!!
        AlertDialog(
            onDismissRequest = { showNoteInputDialog = false },
            title = { Text("Add Reflection Note") },
            text = {
                OutlinedTextField(
                    value = noteInputText,
                    onValueChange = { noteInputText = it },
                    placeholder = { Text("Write your thoughts, tajweed tips, or reflections...") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteInputText.isNotBlank()) {
                            onAddNote(
                                ayah.ayahId,
                                ayah.surahNumber,
                                ayah.ayahNumber,
                                surah?.nameTransliteration ?: "",
                                noteInputText
                            )
                            showNoteInputDialog = false
                            Toast.makeText(context, "Note saved to local database", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Save Note", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteInputDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Jump to Ayah Dialog (PRD U-17)
    if (showJumpDialog) {
        var jumpAyahInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = { Text("Jump to Ayah") },
            text = {
                Column {
                    Text("Enter Ayah number (1 to ${surah?.ayahCount ?: 286}):")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = jumpAyahInput,
                        onValueChange = { jumpAyahInput = it.filter { ch -> ch.isDigit() } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val num = jumpAyahInput.toIntOrNull()
                        if (num != null && num in 1..(surah?.ayahCount ?: 286)) {
                            showJumpDialog = false
                            val targetIdx = ayahs.indexOfFirst { it.ayahNumber == num }
                            if (targetIdx >= 0) {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(targetIdx + 1)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please enter a valid Ayah number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Jump", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Reading Settings Bottom Sheet
    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Text(
                    text = "Reading Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Translation selector
                Text(text = "Translation", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeTranslation == "ur",
                        onClick = { activeTranslation = "ur" },
                        label = { Text("Urdu (جالندھری)") }
                    )
                    FilterChip(
                        selected = activeTranslation == "en",
                        onClick = { activeTranslation = "en" },
                        label = { Text("English (Saheeh)") }
                    )
                    FilterChip(
                        selected = activeTranslation == "none",
                        onClick = { activeTranslation = "none" },
                        label = { Text("Arabic Only") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Reading Surface Palette (Warm Ivory, Sepia, Dark, High Contrast)
                Text(text = "Reading Surface Theme", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = readingSurfaceTheme == "warm_ivory",
                        onClick = { readingSurfaceTheme = "warm_ivory" },
                        label = { Text("Ivory") }
                    )
                    FilterChip(
                        selected = readingSurfaceTheme == "sepia",
                        onClick = { readingSurfaceTheme = "sepia" },
                        label = { Text("Sepia") }
                    )
                    FilterChip(
                        selected = readingSurfaceTheme == "dark",
                        onClick = { readingSurfaceTheme = "dark" },
                        label = { Text("Night") }
                    )
                    FilterChip(
                        selected = readingSurfaceTheme == "high_contrast",
                        onClick = { readingSurfaceTheme = "high_contrast" },
                        label = { Text("OLED") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Arabic font size slider
                Text(
                    text = "Arabic Font Size: ${fontSizeSp.toInt()} sp",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = fontSizeSp,
                    onValueChange = { fontSizeSp = it },
                    valueRange = 20f..44f,
                    steps = 12,
                    colors = SliderDefaults.colors(
                        thumbColor = ForestGreen,
                        activeTrackColor = ForestGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showSettingsSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply & Close", color = Color.White)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AudioBottomBar(
    playbackState: AudioPlaybackState,
    onPauseAudio: () -> Unit,
    onResumeAudio: () -> Unit,
    onToggleRepeat: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onSetReciter: (QariReciter) -> Unit,
    onPrevAyah: () -> Unit,
    onNextAyah: () -> Unit
) {
    var showQariSheet by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.clickable { showQariSheet = true }) {
                    Text(
                        text = "Reciting: Ayah ${playbackState.currentAyah}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = playbackState.selectedReciter.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SecondaryGrey, modifier = Modifier.size(16.dp))
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Previous Ayah
                    IconButton(onClick = onPrevAyah) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Ayah", tint = ForestGreen)
                    }

                    // Play / Pause
                    IconButton(
                        onClick = {
                            if (playbackState.isPlaying) onPauseAudio() else onResumeAudio()
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(ForestGreen, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                            tint = Color.White
                        )
                    }

                    // Next Ayah
                    IconButton(onClick = onNextAyah) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next Ayah", tint = ForestGreen)
                    }

                    // Repeat Toggle
                    IconButton(onClick = onToggleRepeat) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = if (playbackState.isRepeatEnabled) ForestGreen else SecondaryGrey
                        )
                    }
                }
            }
        }
    }

    if (showQariSheet) {
        AlertDialog(
            onDismissRequest = { showQariSheet = false },
            title = { Text("Select Qari (قاری منتخب کریں)") },
            text = {
                Column {
                    QariReciter.entries.forEach { reciter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSetReciter(reciter)
                                    showQariSheet = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(reciter.displayName, fontWeight = FontWeight.Medium)
                            RadioButton(
                                selected = playbackState.selectedReciter == reciter,
                                onClick = {
                                    onSetReciter(reciter)
                                    showQariSheet = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQariSheet = false }) {
                    Text("Close")
                }
            }
        )
    }
}
