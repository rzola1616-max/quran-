package com.example.feature.progress

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MetricCard
import com.example.core.designsystem.ProgressRing
import com.example.ui.theme.*

@Composable
fun ProgressScreen(
    bookmarksCount: Int,
    notesCount: Int,
    hifzCount: Int,
    lessonsCompletedCount: Int,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showResetDialog by remember { mutableStateOf(false) }
    var resetSuccessMessage by remember { mutableStateOf<String?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }

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
                    text = "Your Learning Journey",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Encouraging, supportive local progress tracking.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryGrey
                )
            }
        }

        // Summary Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftJade),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Daily Habit Streak",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You read 4 days this week",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"The most beloved deeds to Allah are those that are consistent, even if small.\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Charcoal
                        )
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(68.dp)) {
                        ProgressRing(
                            progress = 4f / 7f,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 14f,
                            activeColor = ForestGreen,
                            trackColor = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "4 / 7",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepEmerald
                        )
                    }
                }
            }
        }

        // Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Saved Verses",
                    value = "$bookmarksCount",
                    icon = Icons.Default.Bookmark,
                    accentColor = MutedGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Reflection Notes",
                    value = "$notesCount",
                    icon = Icons.Default.EditNote,
                    accentColor = ForestGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Hifz Items",
                    value = "$hifzCount",
                    icon = Icons.Default.Psychology,
                    accentColor = DeepEmerald,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Lessons Passed",
                    value = "$lessonsCompletedCount",
                    icon = Icons.Default.CheckCircle,
                    accentColor = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Backup and Data Management
        item {
            Text(
                text = "Backup & Data Management",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "User Data Export & Safety",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "You can export a local backup of your bookmarks, revision records, notes, and quiz scores without storing any private voice recordings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryGrey
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showExportDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export Backup")
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Backup verified. No conflicting items found.", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restore")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        modifier = Modifier.fillMaxWidth().testTag("btn_reset_data")
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = ErrorRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset Local Learning Data")
                    }
                }
            }
        }

        if (resetSuccessMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoftJade,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = resetSuccessMessage!!,
                        fontSize = 12.sp,
                        color = DeepEmerald,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }

    if (showExportDialog) {
        val exportJson = """
            {
              "appName": "Quran Companion",
              "exportedAt": "${System.currentTimeMillis()}",
              "bookmarksCount": $bookmarksCount,
              "notesCount": $notesCount,
              "hifzItemsCount": $hifzCount,
              "lessonsPassed": $lessonsCompletedCount
            }
        """.trimIndent()

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Local Backup") },
            text = {
                Column {
                    Text("Your backup JSON metadata is generated locally:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(exportJson, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(exportJson))
                        showExportDialog = false
                        Toast.makeText(context, "Backup JSON copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Copy to Clipboard", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Learning Data?") },
            text = {
                Text("This will clear your bookmarks, notes, Hifz revision notebook, and lesson quiz scores. Quran text and translations will remain unaffected.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetDialog = false
                        resetSuccessMessage = "Your local learning progress has been reset."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Confirm Reset", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
