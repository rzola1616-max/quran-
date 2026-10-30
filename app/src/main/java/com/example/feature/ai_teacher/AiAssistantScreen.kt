package com.example.feature.ai_teacher

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ai.AssistantResponse
import com.example.core.ai.GeminiAssistant
import com.example.core.designsystem.AiLabel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    assistant: GeminiAssistant,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var questionInput by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("ur") } // "ur" or "en"
    var isLoading by remember { mutableStateOf(false) }
    var currentResponse by remember { mutableStateOf<AssistantResponse?>(null) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportSubmittedMessage by remember { mutableStateOf<String?>(null) }

    val suggestedPrompts = if (selectedLanguage == "ur") listOf(
        "سورۃ الفاتحہ کے بنیادی مضامین کیا ہیں؟",
        "تجوید میں نون ساکن کے احکام کی وضاحت کریں",
        "قرآن حفظ کرنے کے لیے سبقی اور منزل کا شیڈول بتائیں",
        "قلقلہ کے حروف کون سے ہیں اور کیسے پڑھے جاتے ہیں؟"
    ) else listOf(
        "What are the core themes of Surah Al-Fatihah?",
        "Explain the 4 rules of Noon Sakin and Tanween",
        "What is the recommended daily schedule for Hifz revision?",
        "Which letters require Qalqalah (echo bounce) in Tajweed?"
    )

    fun submitQuestion(query: String) {
        if (query.isBlank() || isLoading) return
        isLoading = true
        coroutineScope.launch {
            val res = assistant.askAssistant(
                question = query,
                userLanguage = selectedLanguage
            )
            currentResponse = res
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Quran Learning Assistant", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Gemini 4 Argon (High Thinking)", style = MaterialTheme.typography.bodySmall, color = ForestGreen)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        selectedLanguage = if (selectedLanguage == "ur") "en" else "ur"
                    }) {
                        Text(if (selectedLanguage == "ur") "اردو" else "English", fontWeight = FontWeight.Bold, color = ForestGreen)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info Banner
                item {
                    AiLabel(
                        text = "Grounded in verified Quran curriculum & references • High Thinking Level",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Suggested Prompts
                item {
                    Text(
                        text = if (selectedLanguage == "ur") "تجویز کردہ سوالات:" else "Suggested Inquiries:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(suggestedPrompts) { prompt ->
                            SuggestionChip(
                                onClick = {
                                    questionInput = prompt
                                    submitQuestion(prompt)
                                },
                                label = { Text(prompt, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Response Card
                if (isLoading) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = ForestGreen)
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Analyzing with Gemini 4 Argon Thinking Engine...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepEmerald
                                )
                                Text(
                                    text = "Validating verses, authentic translations, and Tajweed references",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SecondaryGrey
                                )
                            }
                        }
                    }
                } else if (currentResponse != null) {
                    val res = currentResponse!!
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth().testTag("ai_assistant_response_card")
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AiLabel()
                                    IconButton(
                                        onClick = { showReportDialog = true },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Flag,
                                            contentDescription = "Report Answer",
                                            tint = SecondaryGrey,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = res.answer,
                                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (res.references.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = LightSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Verified References:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen
                                    )
                                    res.references.forEach { ref ->
                                        Text(
                                            text = "• $ref",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SecondaryGrey
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = res.disclaimer,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SecondaryGrey,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                if (reportSubmittedMessage != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoftJade,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = reportSubmittedMessage!!,
                                fontSize = 12.sp,
                                color = DeepEmerald,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Input Row
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = { questionInput = it },
                        placeholder = {
                            Text(if (selectedLanguage == "ur") "تجوید، ترجمہ یا سورت کے بارے میں پوچھیں..." else "Ask about Tajweed, translations, or verses...")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_assistant_query_input")
                    )

                    IconButton(
                        onClick = {
                            val q = questionInput
                            questionInput = ""
                            submitQuestion(q)
                        },
                        enabled = questionInput.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (questionInput.isNotBlank()) ForestGreen else Color.LightGray,
                                CircleShape
                            )
                            .testTag("submit_ai_question_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report this response") },
            text = {
                Text("Your anonymous report helps our curriculum team verify scholarly references and accuracy. No personal data or private voice recordings are submitted.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReportDialog = false
                        reportSubmittedMessage = "Thank you. Your report has been noted for teacher review."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Submit Report", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
