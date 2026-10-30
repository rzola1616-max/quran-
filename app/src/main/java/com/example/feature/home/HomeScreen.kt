package com.example.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.database.LastReadEntity
import com.example.core.designsystem.ContinueReadingCard
import com.example.core.designsystem.ProgressRing
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    lastRead: LastReadEntity?,
    onNavigateToQuran: (Int, Int) -> Unit,
    onNavigateToLearn: () -> Unit,
    onNavigateToAiTeacher: () -> Unit,
    onNavigateToHifz: () -> Unit,
    onNavigateToDownloads: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Column {
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    fontSize = 20.sp,
                    color = ForestGreen,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Welcome to Quran Companion",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Learn, read, listen, and revise at your own pace.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryGrey
                )
            }
        }

        // Continue Reading Card
        item {
            val surahNum = lastRead?.surahNumber ?: 1
            val ayahNum = lastRead?.ayahNumber ?: 1
            val surahTitle = lastRead?.surahName ?: "Al-Fatihah"
            ContinueReadingCard(
                surahNumber = surahNum,
                ayahNumber = ayahNum,
                surahName = surahTitle,
                onContinueClick = { onNavigateToQuran(surahNum, ayahNum) }
            )
        }

        // Daily Goal Progress Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().testTag("daily_goal_card")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Today's Learning Goal",
                                style = MaterialTheme.typography.labelLarge,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "10 of 15 Minutes Completed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Keep up your supportive daily recitation habit.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                        ProgressRing(
                            progress = 0.67f,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 14f,
                            activeColor = ForestGreen,
                            trackColor = SoftJade
                        )
                        Text(
                            text = "67%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }
            }
        }

        // Quick Actions Grid (2x2 + 1)
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionItem(
                    title = "Read Quran",
                    subtitle = "114 Surahs",
                    icon = Icons.Default.MenuBook,
                    accent = DeepEmerald,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToQuran(1, 1) }
                )
                QuickActionItem(
                    title = "Learn Qaida",
                    subtitle = "Letters & Tajweed",
                    icon = Icons.Default.School,
                    accent = ForestGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToLearn
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionItem(
                    title = "AI Teacher",
                    subtitle = "Live Recitation",
                    icon = Icons.Default.Mic,
                    accent = Color(0xFF1B5E20),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiTeacher
                )
                QuickActionItem(
                    title = "Hifz Practice",
                    subtitle = "Recall & Test",
                    icon = Icons.Default.Psychology,
                    accent = MutedGold,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToHifz
                )
            }
        }

        // Daily Ayah Inspiration Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Verse",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "Surah Al-Fatihah: 2",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryGrey
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"All praise is due to Allah, Lord of the worlds.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Downloads Manager Shortcut
        item {
            OutlinedCard(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToDownloads() }
                    .testTag("downloads_shortcut_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DownloadForOffline,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Offline Content Packs",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manage translations, audio recitations, and models",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryGrey
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Downloads",
                        tint = SecondaryGrey
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag("action_${title.lowercase().replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accent)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
        }
    }
}
