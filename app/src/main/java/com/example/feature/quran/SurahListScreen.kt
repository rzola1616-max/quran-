package com.example.feature.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.database.SurahEntity
import com.example.ui.theme.*

@Composable
fun SurahListScreen(
    surahs: List<SurahEntity>,
    onSelectSurah: (Int) -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Surah, 1: Juz
    var searchQuery by remember { mutableStateOf("") }

    val filteredSurahs = remember(surahs, searchQuery) {
        if (searchQuery.isBlank()) surahs
        else surahs.filter {
            it.nameTransliteration.contains(searchQuery, ignoreCase = true) ||
            it.nameArabic.contains(searchQuery) ||
            it.nameTranslationEn.contains(searchQuery, ignoreCase = true) ||
            it.surahNumber.toString() == searchQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab selector (Surahs vs Juz) and quick buttons
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = ForestGreen
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Surahs (114)", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("tab_surahs")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Juz (30)", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("tab_juz")
                        )
                    }
                    IconButton(
                        onClick = onNavigateToBookmarks,
                        modifier = Modifier.testTag("btn_bookmarks")
                    ) {
                        Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Bookmarks", tint = MutedGold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Surah name or number...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SecondaryGrey) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("surah_search_field")
                )
            }
        }

        if (selectedTab == 0) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredSurahs, key = { it.surahNumber }) { surah ->
                    SurahListItem(
                        surah = surah,
                        onClick = { onSelectSurah(surah.surahNumber) }
                    )
                }
            }
        } else {
            // 30 Juz List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items((1..30).toList()) { juzNumber ->
                    val surahForJuz = surahs.firstOrNull { it.startJuz == juzNumber } ?: surahs.first()
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSurah(surahForJuz.surahNumber) }
                            .testTag("juz_item_$juzNumber")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(SoftJade),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$juzNumber",
                                        fontWeight = FontWeight.Bold,
                                        color = DeepEmerald
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Juz $juzNumber (پارہ $juzNumber)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Starts at Surah ${surahForJuz.nameTransliteration}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SecondaryGrey
                                    )
                                }
                            }
                            Text(
                                text = "الجزء $juzNumber",
                                fontSize = 18.sp,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: SurahEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("surah_item_${surah.surahNumber}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SoftJade),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.surahNumber}",
                        fontWeight = FontWeight.Bold,
                        color = DeepEmerald,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = surah.nameTransliteration,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${surah.revelationType} • ${surah.ayahCount} Ayahs • Juz ${surah.startJuz}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryGrey
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = surah.nameArabic,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Text(
                    text = surah.nameTranslationUr,
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryGrey
                )
            }
        }
    }
}
