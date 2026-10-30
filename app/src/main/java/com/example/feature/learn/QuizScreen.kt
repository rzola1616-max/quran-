package com.example.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.learning.QaidaLesson
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.SecondaryGrey
import com.example.ui.theme.SoftJade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    lesson: QaidaLesson,
    onQuizCompleted: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isQuizFinished by remember { mutableStateOf(false) }

    val questions = lesson.quizQuestions
    val currentQuestion = questions.getOrNull(currentQuestionIdx)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz: ${lesson.titleEn}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isQuizFinished) {
                // Results screen
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Quiz Completed!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    val percent = if (questions.isNotEmpty()) (correctAnswersCount * 100) / questions.size else 100
                    Text(
                        text = "Your Score: $percent%",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You correctly answered $correctAnswersCount of ${questions.size} questions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryGrey,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { onQuizCompleted(if (questions.isNotEmpty()) (correctAnswersCount * 100) / questions.size else 100) },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Save & Return to Lessons", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else if (currentQuestion != null) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Question ${currentQuestionIdx + 1} of ${questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentQuestion.questionEn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentQuestion.questionUr,
                        style = MaterialTheme.typography.titleMedium,
                        color = SecondaryGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prompt Arabic Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SoftJade,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQuestion.promptArabic,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = DeepEmerald,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Options list
                    currentQuestion.options.forEachIndexed { index, option ->
                        val isSelected = selectedOptionIdx == index
                        val isCorrect = currentQuestion.correctIndex == index

                        val containerColor = when {
                            isSubmitted && isCorrect -> ForestGreen.copy(alpha = 0.2f)
                            isSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            isSelected -> SoftJade
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = containerColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clickable(enabled = !isSubmitted) { selectedOptionIdx = index }
                                .testTag("quiz_option_$index")
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    if (isSubmitted) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentQuestion.explanationEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreen
                        )
                    }
                }

                // Action Button (Submit / Next)
                Button(
                    onClick = {
                        if (!isSubmitted) {
                            if (selectedOptionIdx == currentQuestion.correctIndex) {
                                correctAnswersCount++
                            }
                            isSubmitted = true
                        } else {
                            if (currentQuestionIdx < questions.size - 1) {
                                currentQuestionIdx++
                                selectedOptionIdx = null
                                isSubmitted = false
                            } else {
                                isQuizFinished = true
                            }
                        }
                    },
                    enabled = selectedOptionIdx != null,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp).testTag("quiz_action_button")
                ) {
                    Text(
                        text = if (!isSubmitted) "Submit Answer" else if (currentQuestionIdx < questions.size - 1) "Next Question" else "See Results",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
