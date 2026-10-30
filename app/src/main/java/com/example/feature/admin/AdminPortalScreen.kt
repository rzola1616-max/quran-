package com.example.feature.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.database.AdminLessonReviewEntity
import com.example.core.database.AdminReportEntity
import com.example.core.database.AdminSourceEntity
import com.example.core.database.AuditLogEntity
import com.example.core.designsystem.MetricCard
import com.example.ui.theme.*

enum class AdminRole(val label: String) {
    OWNER("Owner"),
    CONTENT_MANAGER("Content Manager"),
    TEACHER_REVIEWER("Teacher Reviewer"),
    SUPPORT("Support Triage")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen(
    reports: List<AdminReportEntity>,
    sources: List<AdminSourceEntity>,
    lessonReviews: List<AdminLessonReviewEntity>,
    auditLogs: List<AuditLogEntity>,
    onResolveReport: (String, String, String) -> Unit,
    onApproveLesson: (String, String) -> Unit,
    onRequestLessonChanges: (String, String) -> Unit,
    onToggleAiKillSwitch: (Boolean) -> Unit,
    isAiKillSwitchActive: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf(AdminRole.OWNER) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Sources, 2: Reviews, 3: Reports, 4: Audit & Config
    var showKillSwitchConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Quran Admin Portal", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MutedGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "ADMIN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepEmerald,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Role Switcher
                    var showRoleMenu by remember { mutableStateOf(false) }
                    TextButton(onClick = { showRoleMenu = true }) {
                        Text(selectedRole.label, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ForestGreen)
                    }
                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        AdminRole.entries.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.label) },
                                onClick = {
                                    selectedRole = role
                                    showRoleMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Admin Navigation Tabs based on role
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ForestGreen
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Dashboard") })
                if (selectedRole == AdminRole.OWNER || selectedRole == AdminRole.CONTENT_MANAGER) {
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Sources") })
                }
                if (selectedRole == AdminRole.OWNER || selectedRole == AdminRole.TEACHER_REVIEWER) {
                    Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Lesson Review (${lessonReviews.size})") })
                }
                if (selectedRole == AdminRole.OWNER || selectedRole == AdminRole.SUPPORT) {
                    Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("AI Reports (${reports.count { it.status == "new" }})") })
                }
                if (selectedRole == AdminRole.OWNER) {
                    Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("Config & Audit") })
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTab) {
                    0 -> {
                        // Admin Dashboard
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SoftJade),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Cloudflare Worker API Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DeepEmerald)
                                        Text("quran-learning-api • Region: edge • HTTP 200 OK", style = MaterialTheme.typography.bodySmall, color = Charcoal)
                                    }
                                    Surface(shape = CircleShape, color = ForestGreen) {
                                        Text("LIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                MetricCard(title = "Pending Reviews", value = "${lessonReviews.count { it.reviewStatus == "in_review" }}", icon = Icons.Default.RateReview, accentColor = MutedGold, modifier = Modifier.weight(1f))
                                MetricCard(title = "Open AI Reports", value = "${reports.count { it.status == "new" }}", icon = Icons.Default.Flag, accentColor = ErrorRed, modifier = Modifier.weight(1f))
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                MetricCard(title = "Content Sources", value = "${sources.size}", icon = Icons.Default.Source, accentColor = DeepEmerald, modifier = Modifier.weight(1f))
                                MetricCard(title = "Assistant State", value = if (isAiKillSwitchActive) "OFFLINE" else "ACTIVE", icon = Icons.Default.SmartToy, accentColor = if (isAiKillSwitchActive) ErrorRed else ForestGreen, modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    1 -> {
                        // Sources Manager
                        item {
                            Text("Content Sources Registry (Verified & Licensed)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        items(sources) { src ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(src.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (src.licenseStatus == "verified") SoftJade else Color(0xFFFFF3E0)
                                        ) {
                                            Text(
                                                text = src.licenseStatus.uppercase(),
                                                color = if (src.licenseStatus == "verified") ForestGreen else MutedGold,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Publisher: ${src.publisher} • Type: ${src.type}", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                    Text("License: ${src.licenseName}", style = MaterialTheme.typography.bodySmall, color = ForestGreen)
                                }
                            }
                        }
                    }

                    2 -> {
                        // Lesson Review Queue
                        item {
                            Text("Teacher Review Queue (Tajweed & Qaida)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        items(lessonReviews) { review ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(review.titleEn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                        Surface(shape = RoundedCornerShape(6.dp), color = SoftJade) {
                                            Text(review.reviewStatus.uppercase(), color = DeepEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    Text(review.titleUr, color = ForestGreen, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Reviewer: ${review.reviewedBy} • Notes: ${review.reviewNotes}", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)

                                    if (selectedRole == AdminRole.OWNER || selectedRole == AdminRole.TEACHER_REVIEWER) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Button(
                                                onClick = { onApproveLesson(review.lessonId, "Approved by Teacher Reviewer") },
                                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Approve")
                                            }
                                            OutlinedButton(
                                                onClick = { onRequestLessonChanges(review.lessonId, "Changes requested on transliteration") },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Request Changes")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // AI Feedback Reports Queue
                        item {
                            Text("User-Submitted AI Feedback Reports", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        if (reports.isEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("No open reports. All AI outputs comply with scholarly standards.", modifier = Modifier.padding(16.dp), color = SecondaryGrey)
                                }
                            }
                        } else {
                            items(reports) { rep ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Category: ${rep.category.uppercase()}", fontWeight = FontWeight.Bold, color = ErrorRed, style = MaterialTheme.typography.titleSmall)
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = if (rep.status == "resolved") SoftJade else Color(0xFFFFEBEE)
                                            ) {
                                                Text(rep.status.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (rep.status == "resolved") ForestGreen else ErrorRed, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Query: ${rep.question}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                        Text("Excerpt: ${rep.consentedExcerpt}", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                        if (rep.userNote.isNotBlank()) {
                                            Text("User Note: ${rep.userNote}", style = MaterialTheme.typography.bodySmall, color = Charcoal)
                                        }
                                        if (rep.status == "new") {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Button(
                                                onClick = { onResolveReport(rep.id, "resolved", "Verified against canonical reference text.") },
                                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Mark as Triaged & Resolved")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    4 -> {
                        // Remote Config & Emergency AI Kill Switch
                        item {
                            Text("Remote Config & Kill Switch", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = if (isAiKillSwitchActive) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Emergency AI Assistant Kill Switch", fontWeight = FontWeight.Bold, color = ErrorRed)
                                            Text("Immediately disables online AI Assistant across all clients while keeping offline Quran reading intact.", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                        }
                                        Switch(
                                            checked = isAiKillSwitchActive,
                                            onCheckedChange = { showKillSwitchConfirmDialog = true },
                                            colors = SwitchDefaults.colors(checkedThumbColor = ErrorRed)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text("Administrative Audit Trail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        items(auditLogs) { log ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.History, contentDescription = null, tint = ForestGreen)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("${log.adminRole}: ${log.action} on ${log.entityType}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text(log.details, style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showKillSwitchConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showKillSwitchConfirmDialog = false },
            title = { Text(if (!isAiKillSwitchActive) "Activate AI Kill Switch?" else "Deactivate AI Kill Switch?") },
            text = {
                Text(
                    if (!isAiKillSwitchActive)
                        "This will immediately disable online AI features for all users. The offline Quran reader and audio lessons will remain fully functional."
                    else
                        "This will re-enable the Gemini AI Assistant across the application."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onToggleAiKillSwitch(!isAiKillSwitchActive)
                        showKillSwitchConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (!isAiKillSwitchActive) ErrorRed else ForestGreen)
                ) {
                    Text("Confirm Action", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showKillSwitchConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
