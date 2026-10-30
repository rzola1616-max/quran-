package com.example.feature.ai_teacher

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.core.ai.RecitationEngineState
import com.example.core.ai.RecitationIssue
import com.example.core.ai.RecitationRecognitionEngine
import com.example.core.database.AyahEntity
import com.example.core.ai.WordAssessment
import com.example.feature.daily_lesson.WordHighlightChip
import com.example.feature.daily_lesson.WordCorrectionCard
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiTeacherScreen(
    engine: RecitationRecognitionEngine,
    engineState: RecitationEngineState,
    initialAyah: AyahEntity?,
    onPlayReferenceAudio: (Int, Int) -> Unit,
    onOpenAiAssistant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedWordForCorrection by remember { mutableStateOf<WordAssessment?>(null) }
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

    val targetAyah = initialAyah ?: AyahEntity(
        ayahId = 1001,
        surahNumber = 1,
        ayahNumber = 1,
        textUthmani = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
        textIndoPak = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِیْمِ",
        translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
        translationUr = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔",
        juz = 1, page = 1, ruku = 1
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mode & Assistant Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SoftJade),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAiAssistant() }
                    .testTag("open_ai_assistant_banner")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = DeepEmerald,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ask AI Learning Assistant",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepEmerald
                            )
                            Text(
                                text = "Deep thinking explanations, translations & study plans",
                                style = MaterialTheme.typography.bodySmall,
                                color = Charcoal
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Assistant",
                        tint = DeepEmerald
                    )
                }
            }
        }

        // Selected Ayah for Practice
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ForestGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Surah ${targetAyah.surahNumber} • Ayah ${targetAyah.ayahNumber}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        IconButton(onClick = {
                            onPlayReferenceAudio(targetAyah.surahNumber, targetAyah.ayahNumber)
                        }) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Hear Reference Recitation", tint = ForestGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = targetAyah.textUthmani,
                        fontSize = 28.sp,
                        lineHeight = 44.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = targetAyah.translationUr,
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                        color = SecondaryGrey,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Live Listening & Model Feedback Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (engineState) {
                        is RecitationEngineState.Listening -> ForestGreen.copy(alpha = 0.08f)
                        is RecitationEngineState.Completed -> SoftJade.copy(alpha = 0.6f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().testTag("recitation_practice_panel")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (engineState) {
                        is RecitationEngineState.Idle -> {
                            Text(
                                text = "Live Recitation Correction",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Recite clearly into your phone. The on-device engine will verify word alignment and Tajweed rules without sending raw audio to the cloud.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryGrey,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (hasMicPermission) {
                                        engine.startListening(targetAyah.textUthmani)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("start_recitation_button")
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Start Reciting", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        is RecitationEngineState.Listening -> {
                            val pulseAnim by rememberInfiniteTransition(label = "mic_pulse").animateFloat(
                                initialValue = 1f,
                                targetValue = 1.25f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(600, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "mic_pulse_anim"
                            )

                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size((52 * pulseAnim).dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Listening",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Listening to your recitation...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )
                            Text(
                                text = "Audio level: ${(engineState.amplitude * 100).toInt()}% • Keep reciting",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryGrey
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { engine.stopListening() },
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("stop_recitation_button")
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Stop & Analyze", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        is RecitationEngineState.Processing -> {
                            CircularProgressIndicator(color = ForestGreen)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Analyzing pronunciation & phonemes...", style = MaterialTheme.typography.titleSmall)
                        }

                        is RecitationEngineState.Completed -> {
                            Icon(
                                imageVector = if (engineState.issues.isEmpty()) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (engineState.issues.isEmpty()) ForestGreen else MutedGold,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = engineState.feedbackSummary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "صحتِ تلاوت: ${engineState.accuracyPercentage}% • اعتماد: ${(engineState.overallConfidence * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "الفاظ پر چھو کر تلفظ کی جانچ اور آواز سنیں:",
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryGrey
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
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

                            selectedWordForCorrection?.let { wordAssess ->
                                Spacer(modifier = Modifier.height(10.dp))
                                WordCorrectionCard(
                                    assessment = wordAssess,
                                    onHearAudio = { engine.speakWord(wordAssess.wordArabic) },
                                    onDismiss = { selectedWordForCorrection = null }
                                )
                            }

                            if (engineState.issues.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                engineState.issues.forEach { issue ->
                                    IssueCard(issue = issue)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onPlayReferenceAudio(targetAyah.surahNumber, targetAyah.ayahNumber)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Hear Again")
                                }
                                Button(
                                    onClick = { engine.reset() },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Try Again")
                                }
                            }
                        }

                        is RecitationEngineState.LowConfidence -> {
                            Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = SecondaryGrey, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "I could not confidently assess this part—please repeat",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = engineState.reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryGrey,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { engine.reset() },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Try Again", color = Color.White)
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
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IssueCard(issue: RecitationIssue) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF8E1),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = MutedGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Word: ${issue.wordArabic}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = issue.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Charcoal
                )
            }
        }
    }
}
