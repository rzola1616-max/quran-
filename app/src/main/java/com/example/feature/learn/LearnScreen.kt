package com.example.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
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
import com.example.core.database.LessonProgressEntity
import com.example.data.learning.QaidaLesson
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    lessons: List<QaidaLesson>,
    progressList: List<LessonProgressEntity>,
    onSelectLesson: (QaidaLesson) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPath by remember { mutableStateOf("qaida") } // "qaida" or "tajweed"

    val currentLessons = remember(lessons, selectedPath) {
        lessons.filter { it.path == selectedPath }
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
                    text = "Structured Learning Path",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Step-by-step Qaida and teacher-verified Tajweed rules with interactive sound examples.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryGrey
                )
            }
        }

        // Path Selector (Qaida vs Tajweed)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = selectedPath == "qaida",
                    onClick = { selectedPath = "qaida" },
                    label = { Text("Noorani Qaida (قاعدہ)") },
                    leadingIcon = {
                        Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    modifier = Modifier.testTag("filter_qaida")
                )
                FilterChip(
                    selected = selectedPath == "tajweed",
                    onClick = { selectedPath = "tajweed" },
                    label = { Text("Tajweed Rules (تجوید)") },
                    leadingIcon = {
                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    modifier = Modifier.testTag("filter_tajweed")
                )
            }
        }

        // Lesson Cards
        items(currentLessons, key = { it.id }) { lesson ->
            val progress = progressList.find { it.lessonId == lesson.id }
            val isCompleted = progress?.isCompleted == true

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) SoftJade.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectLesson(lesson) }
                    .testTag("lesson_card_${lesson.id}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = lesson.titleEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lesson.titleUr,
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.descriptionEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryGrey
                        )

                        if (isCompleted) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ForestGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Quiz Passed • Score: ${progress?.score ?: 100}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Lesson",
                        tint = ForestGreen
                    )
                }
            }
        }
    }
}
