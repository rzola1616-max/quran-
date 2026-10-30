package com.example.feature.daily_lesson

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.core.ai.*
import com.example.core.database.AyahEntity
import com.example.data.daily_lesson.DailyLesson
import com.example.data.daily_lesson.DailyLessonData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DailyLessonScreen(
    currentDay: Int = 1,
    completedDays: Set<Int> = emptySet(),
    engine: RecitationRecognitionEngine,
    engineState: RecitationEngineState,
    onPlayReferenceAudio: (Int, Int) -> Unit,
    onMarkLessonCompleted: (Int, Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var selectedDay by remember { mutableIntStateOf(currentDay) }
    val lesson = remember(selectedDay) { DailyLessonData.getLessonByDay(selectedDay) }
    var selectedAyahIndex by remember { mutableIntStateOf(0) }
    val currentAyah = lesson.targetAyahs.getOrElse(selectedAyahIndex) { lesson.targetAyahs.first() }

    var useIndoPakScript by remember { mutableStateOf(true) }
    var selectedWordForCorrection by remember { mutableStateOf<WordAssessment?>(null) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasMicPermission = isGranted
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "روزانہ کا سبق • Daily Lesson",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "سبق نمبر $selectedDay از ۳۰ • ${lesson.surahNameUr}",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("daily_lesson_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MutedGold.copy(alpha = 0.15f),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = MutedGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${completedDays.size} دن", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepEmerald)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Horizontal 30 Days Selector
            item {
                Column {
                    Text(
                        text = "اسباق کا انتخاب (۳۰ روزہ دورہ)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(DailyLessonData.getAllLessons()) { item ->
                            val isSelected = item.dayNumber == selectedDay
                            val isCompleted = completedDays.contains(item.dayNumber)

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when {
                                    isSelected -> ForestGreen
                                    isCompleted -> SoftJade
                                    else -> MaterialTheme.colorScheme.surface
                                },
                                tonalElevation = if (isSelected) 4.dp else 1.dp,
                                modifier = Modifier
                                    .clickable {
                                        selectedDay = item.dayNumber
                                        selectedAyahIndex = 0
                                        engine.reset()
                                    }
                                    .testTag("day_chip_${item.dayNumber}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    if (isCompleted) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Completed",
                                            tint = if (isSelected) Color.White else ForestGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = "دن ${item.dayNumber}",
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isSelected -> Color.White
                                            isCompleted -> DeepEmerald
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Daily Lesson Card (Title & Tajweed Rule Focus)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SoftJade
                            ) {
                                Text(
                                    text = "${lesson.surahNameUr} (${lesson.surahNameArabic})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepEmerald,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "آیات ${lesson.startAyah} تا ${lesson.endAyah}",
                                fontSize = 12.sp,
                                color = SecondaryGrey,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = lesson.titleUr,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = lesson.titleEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tajweed Rule & Makhraj Box
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.School, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "تجوید کا خصوصی اصول:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                                Text(
                                    text = lesson.tajweedFocusUr,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MutedGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "مخارج کی اصلاحی نصیحت:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MutedGold
                                )
                                Text(
                                    text = lesson.makhrajTipUr,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Target Ayah Display Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("target_ayah_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${currentAyah.ayahNumber}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "آیت نمبر ${currentAyah.ayahNumber}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    onClick = { useIndoPakScript = !useIndoPakScript },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (useIndoPakScript) "عثمانی رسم الخط" else "انڈو پاک رسم الخط",
                                        fontSize = 11.sp
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        onPlayReferenceAudio(currentAyah.surahNumber, currentAyah.ayahNumber)
                                    }
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "قاری کی آواز سنیں", tint = ForestGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Arabic Text
                        Text(
                            text = if (useIndoPakScript) currentAyah.textIndoPak else currentAyah.textUthmani,
                            fontSize = 28.sp,
                            lineHeight = 46.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentAyah.translationUr,
                            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                            color = SecondaryGrey,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // If multiple ayahs exist in this lesson, provide Ayah tabs
                        if (lesson.targetAyahs.size > 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                lesson.targetAyahs.forEachIndexed { idx, ay ->
                                    val isCur = idx == selectedAyahIndex
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isCur) ForestGreen else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .clickable {
                                                selectedAyahIndex = idx
                                                engine.reset()
                                            }
                                    ) {
                                        Text(
                                            text = "آیت ${ay.ayahNumber}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCur) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Live Recitation Listening & Interactive Mistake Highlighting Section
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (engineState) {
                            is RecitationEngineState.Listening -> ForestGreen.copy(alpha = 0.08f)
                            is RecitationEngineState.Completed -> SoftJade.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("recitation_evaluation_card")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (engineState) {
                            is RecitationEngineState.Idle -> {
                                Icon(
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "آپ پڑھیں، ایپ سنے گی اور غلطیاں بتائے گی",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "مائیک کا بٹن دبا کر واضح آواز میں تلاوت کریں۔ ایپ ہر ہر لفظ کو سن کر درست تلفظ، غلطیوں اور تجوید کی نشاندہی کرے گی۔",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SecondaryGrey,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        if (hasMicPermission) {
                                            val targetText = if (useIndoPakScript) currentAyah.textIndoPak else currentAyah.textUthmani
                                            engine.startListening(targetText)
                                        } else {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("start_recitation_button")
                                ) {
                                    Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("تلاوت شروع کریں (Start Reciting)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                            }

                            is RecitationEngineState.Listening -> {
                                val pulseAnim by rememberInfiniteTransition(label = "pulse").animateFloat(
                                    initialValue = 1f,
                                    targetValue = 1.3f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(500, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "mic_pulse"
                                )

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size((56 * pulseAnim).dp)
                                            .clip(CircleShape)
                                            .background(ForestGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "Listening",
                                            tint = Color.White,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "ایپ آپ کی تلاوت سن رہی ہے...",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "آواز کی سطح: ${(engineState.amplitude * 100).toInt()}% • روانی سے پڑھتے رہیں",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SecondaryGrey
                                )

                                Spacer(modifier = Modifier.height(18.dp))
                                Button(
                                    onClick = { engine.stopListening() },
                                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("stop_recitation_button")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("تلاوت مکمل (Finish & Highlight Mistakes)", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            is RecitationEngineState.Processing -> {
                                CircularProgressIndicator(color = ForestGreen)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("تلفظ اور مخارج کا تجزیہ کیا جا رہا ہے...", style = MaterialTheme.typography.titleSmall)
                            }

                            is RecitationEngineState.Completed -> {
                                // Highlighting Overview
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (engineState.accuracyPercentage >= 80) Icons.Default.CheckCircle else Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = if (engineState.accuracyPercentage >= 80) ForestGreen else MutedGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "صحتِ قرات: ${engineState.accuracyPercentage}%",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepEmerald
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = ForestGreen.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "اعتماد: ${(engineState.overallConfidence * 100).toInt()}%",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = engineState.feedbackSummary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Color Legend for Mistakes
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LegendDot(color = ForestGreen, text = "درست (Correct)")
                                    LegendDot(color = ErrorRed, text = "غلطی (Mistake)")
                                    LegendDot(color = MutedGold, text = "تجوید (Tajweed)")
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "کسی بھی لفظ کو چھو کر اس کی درستگی اور مخرج سنیں:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SecondaryGrey
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // WORD-BY-WORD HIGHLIGHTING IN FLOW ROW
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    engineState.wordAssessments.forEach { wordItem ->
                                        WordHighlightChip(
                                            assessment = wordItem,
                                            isSelected = selectedWordForCorrection?.index == wordItem.index,
                                            onClick = {
                                                selectedWordForCorrection = wordItem
                                                engine.speakWord(wordItem.wordArabic)
                                            }
                                        )
                                    }
                                }

                                // Interactive Correction Guidance Card
                                AnimatedVisibility(visible = selectedWordForCorrection != null) {
                                    selectedWordForCorrection?.let { wordAssess ->
                                        Spacer(modifier = Modifier.height(16.dp))
                                        WordCorrectionCard(
                                            assessment = wordAssess,
                                            onHearAudio = { engine.speakWord(wordAssess.wordArabic) },
                                            onDismiss = { selectedWordForCorrection = null }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Action Buttons: Repeat or Complete
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { engine.reset() },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("دوبارہ پڑھیں")
                                    }

                                    Button(
                                        onClick = {
                                            onMarkLessonCompleted(selectedDay, engineState.accuracyPercentage)
                                            showCompletionDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f).testTag("mark_lesson_completed_button")
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("سبق مکمل کریں", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            is RecitationEngineState.LowConfidence -> {
                                Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = SecondaryGrey, modifier = Modifier.size(42.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "آواز واضح نہیں تھی",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = engineState.reason,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SecondaryGrey,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { engine.reset() },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("دوبارہ کوشش کریں (Try Again)")
                                }
                            }

                            is RecitationEngineState.Error -> {
                                Text(
                                    text = engineState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ErrorRed,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { engine.reset() }) {
                                    Text("ٹھیک ہے (Dismiss)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Congratulatory Dialog on Lesson Completion
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            icon = {
                Icon(Icons.Default.Celebration, contentDescription = null, tint = MutedGold, modifier = Modifier.size(44.dp))
            },
            title = {
                Text(
                    text = "ماشاء اللہ! مبارک ہو",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "آپ نے سبق نمبر $selectedDay کامیابی سے مکمل کر لیا ہے۔ آپ کا تسلسل اور لگن لائقِ تحسین ہے۔",
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = lesson.virtueUr,
                        fontSize = 12.sp,
                        color = ForestGreen,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletionDialog = false
                        if (selectedDay < 30) {
                            selectedDay += 1
                            selectedAyahIndex = 0
                            engine.reset()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("اگلا سبق (Next Lesson)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompletionDialog = false }) {
                    Text("بند کریں (Close)")
                }
            }
        )
    }
}

@Composable
fun LegendDot(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun WordHighlightChip(
    assessment: WordAssessment,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when (assessment.status) {
        WordRecitationStatus.CORRECT -> Color(0xFFE8F5E9)      // Soft Green
        WordRecitationStatus.MISTAKE -> Color(0xFFFFEBEE)      // Soft Red
        WordRecitationStatus.TAJWEED_ATTENTION -> Color(0xFFFFF8E1) // Soft Amber
    }

    val borderColor = when (assessment.status) {
        WordRecitationStatus.CORRECT -> ForestGreen
        WordRecitationStatus.MISTAKE -> ErrorRed
        WordRecitationStatus.TAJWEED_ATTENTION -> MutedGold
    }

    val textColor = when (assessment.status) {
        WordRecitationStatus.CORRECT -> DeepEmerald
        WordRecitationStatus.MISTAKE -> Color(0xFFC62828)
        WordRecitationStatus.TAJWEED_ATTENTION -> Color(0xFFF57F17)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.5.dp else 1.dp,
            color = if (isSelected) DeepEmerald else borderColor
        ),
        modifier = Modifier
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .clickable { onClick() }
            .testTag("word_chip_${assessment.index}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = when (assessment.status) {
                    WordRecitationStatus.CORRECT -> Icons.Default.Check
                    WordRecitationStatus.MISTAKE -> Icons.Default.Close
                    WordRecitationStatus.TAJWEED_ATTENTION -> Icons.Default.PriorityHigh
                },
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = assessment.wordArabic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = textColor
            )
        }
    }
}

@Composable
fun WordCorrectionCard(
    assessment: WordAssessment,
    onHearAudio: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = when (assessment.status) {
            WordRecitationStatus.MISTAKE -> Color(0xFFFFF0F2)
            WordRecitationStatus.TAJWEED_ATTENTION -> Color(0xFFFFFBEA)
            WordRecitationStatus.CORRECT -> SoftJade
        },
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = when (assessment.status) {
                WordRecitationStatus.MISTAKE -> ErrorRed.copy(alpha = 0.5f)
                WordRecitationStatus.TAJWEED_ATTENTION -> MutedGold.copy(alpha = 0.5f)
                WordRecitationStatus.CORRECT -> ForestGreen.copy(alpha = 0.3f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (assessment.status) {
                            WordRecitationStatus.CORRECT -> Icons.Default.CheckCircle
                            WordRecitationStatus.MISTAKE -> Icons.Default.Cancel
                            WordRecitationStatus.TAJWEED_ATTENTION -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = when (assessment.status) {
                            WordRecitationStatus.CORRECT -> ForestGreen
                            WordRecitationStatus.MISTAKE -> ErrorRed
                            WordRecitationStatus.TAJWEED_ATTENTION -> MutedGold
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (assessment.status) {
                            WordRecitationStatus.CORRECT -> "درست تلفظ (Accurate)"
                            WordRecitationStatus.MISTAKE -> "تلفظ کی تصحیح درکار ہے (Correction Required)"
                            WordRecitationStatus.TAJWEED_ATTENTION -> "تجوید کی اصلاح (Tajweed Attention)"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (assessment.status) {
                            WordRecitationStatus.CORRECT -> DeepEmerald
                            WordRecitationStatus.MISTAKE -> Color(0xFFC62828)
                            WordRecitationStatus.TAJWEED_ATTENTION -> Color(0xFFF57F17)
                        }
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "بند کریں", modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Large word with tashkeel and Audio Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onHearAudio,
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = ForestGreen),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("درست آواز سنیں", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Text(
                    text = assessment.wordArabic,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explanation of what was mispronounced
            if (!assessment.mistakeExplanationUr.isNullOrBlank()) {
                Text(
                    text = "غلطی کی نشاندہی:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ErrorRed
                )
                Text(
                    text = assessment.mistakeExplanationUr,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // How to fix it (Correction Guide)
            if (!assessment.correctionGuideUr.isNullOrBlank()) {
                Text(
                    text = "صحیح تلفظ کا طریقہ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Text(
                    text = assessment.correctionGuideUr,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Makhraj detail
            if (!assessment.makhrajDetailUr.isNullOrBlank()) {
                Text(
                    text = "مخرج کی وضاحت:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedGold
                )
                Text(
                    text = assessment.makhrajDetailUr,
                    fontSize = 12.sp,
                    color = SecondaryGrey,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
