package com.example.feature.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    appLanguage: String,
    onLanguageChange: (String) -> Unit,
    preferredScript: String,
    onScriptChange: (String) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onNavigateToContentSources: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToAdminPortal: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var keepScreenOn by remember { mutableStateOf(true) }
    var dailyReminder by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Preferences", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Language Selection
            item {
                SectionHeader("Interface Language")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLanguageChange("ur") }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("اردو (Urdu)", fontWeight = FontWeight.SemiBold)
                            RadioButton(selected = appLanguage == "ur", onClick = { onLanguageChange("ur") })
                        }
                        HorizontalDivider(color = LightSurfaceVariant)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLanguageChange("en") }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("English", fontWeight = FontWeight.SemiBold)
                            RadioButton(selected = appLanguage == "en", onClick = { onLanguageChange("en") })
                        }
                    }
                }
            }

            // Script Selection
            item {
                SectionHeader("Quran Script")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onScriptChange("uthmani") }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Uthmani Script (عثماني)", fontWeight = FontWeight.SemiBold)
                                Text("Standard Madinah Mushaf style", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                            }
                            RadioButton(selected = preferredScript == "uthmani", onClick = { onScriptChange("uthmani") })
                        }
                        HorizontalDivider(color = LightSurfaceVariant)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onScriptChange("indopak") }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Indo-Pak Script (انڈو پاک / نستعلیق)", fontWeight = FontWeight.SemiBold)
                                Text("South Asian traditional script", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                            }
                            RadioButton(selected = preferredScript == "indopak", onClick = { onScriptChange("indopak") })
                        }
                    }
                }
            }

            // Reading Preferences
            item {
                SectionHeader("Reading Preferences")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Theme", fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = onThemeChange,
                                colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen)
                            )
                        }
                        HorizontalDivider(color = LightSurfaceVariant)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Keep Screen On While Reading", fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = keepScreenOn,
                                onCheckedChange = { keepScreenOn = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen)
                            )
                        }
                        HorizontalDivider(color = LightSurfaceVariant)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Daily Learning Reminder", fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = dailyReminder,
                                onCheckedChange = { dailyReminder = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen)
                            )
                        }
                    }
                }
            }

            // Information & Disclosures
            item {
                SectionHeader("Information & Attributions")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        SettingsNavigationItem(
                            icon = Icons.Default.Source,
                            title = "Content Sources & Licences",
                            subtitle = "Verified text, translations & audio origins",
                            onClick = onNavigateToContentSources
                        )
                        HorizontalDivider(color = LightSurfaceVariant)
                        SettingsNavigationItem(
                            icon = Icons.Default.Security,
                            title = "Privacy & Microphone Disclosure",
                            subtitle = "What stays on your device",
                            onClick = onNavigateToPrivacy
                        )
                        HorizontalDivider(color = LightSurfaceVariant)
                        SettingsNavigationItem(
                            icon = Icons.Default.Info,
                            title = "About Quran Companion",
                            subtitle = "Version 1.0 • Offline-First Native Android",
                            onClick = onNavigateToAbout
                        )
                    }
                }
            }

            // Administration & Governance Portal (Part B)
            item {
                SectionHeader("Administration & Governance")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        SettingsNavigationItem(
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Admin & Governance Portal",
                            subtitle = "Content review, QA reports, sources & AI kill-switch",
                            onClick = onNavigateToAdminPortal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = ForestGreen,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun SettingsNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = ForestGreen)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = SecondaryGrey)
    }
}
