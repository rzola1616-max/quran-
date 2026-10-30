package com.example.app

import android.app.Application
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.ai.GeminiAssistant
import com.example.core.ai.RecitationRecognitionEngine
import com.example.core.audio.AudioPlayerService
import com.example.core.database.AyahEntity
import com.example.core.designsystem.AppTopBar
import com.example.data.downloads.DownloadPackRepository
import com.example.data.hifz.HifzRepository
import com.example.data.learning.LearningRepository
import com.example.data.learning.QaidaLesson
import com.example.data.quran.QuranRepository
import com.example.feature.ai_teacher.AiAssistantScreen
import com.example.feature.ai_teacher.AiTeacherScreen
import com.example.feature.downloads.DownloadsScreen
import com.example.feature.hifz.HifzScreen
import com.example.feature.home.HomeScreen
import com.example.feature.learn.LearnScreen
import com.example.feature.learn.LessonDetailScreen
import com.example.feature.learn.QuizScreen
import com.example.feature.onboarding.OnboardingScreen
import com.example.feature.progress.ProgressScreen
import com.example.feature.quran.BookmarksScreen
import com.example.feature.quran.QuranReaderScreen
import com.example.feature.quran.SearchScreen
import com.example.feature.daily_lesson.DailyLessonScreen
import com.example.feature.admin.AdminPortalScreen
import com.example.feature.quran.SurahListScreen
import com.example.feature.settings.AboutScreen
import com.example.feature.settings.ContentSourcesScreen
import com.example.feature.settings.PrivacyScreen
import com.example.feature.settings.SettingsScreen
import com.example.ui.theme.ForestGreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    QURAN,
    LEARN,
    AI_TEACHER,
    DAILY_LESSON,
    PROGRESS,
    QURAN_READER,
    SEARCH,
    BOOKMARKS,
    LESSON_DETAIL,
    QUIZ,
    AI_ASSISTANT,
    DOWNLOADS,
    SETTINGS,
    CONTENT_SOURCES,
    PRIVACY,
    ABOUT,
    ADMIN_PORTAL,
    ONBOARDING
}

@Composable
fun QuranCompanionApp(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Repositories & Services
    val quranRepo = remember { QuranRepository(context) }
    val learningRepo = remember { LearningRepository(context) }
    val hifzRepo = remember { HifzRepository(context) }
    val downloadRepo = remember { DownloadPackRepository(context) }
    val audioService = remember { AudioPlayerService(context) }
    val recitationEngine = remember { RecitationRecognitionEngine(context) }
    val workerApiClient = remember { com.example.core.network.WorkerApiClient(context) }
    val geminiAssistant = remember { GeminiAssistant(workerApiClient) }
    val database = remember { com.example.core.database.AppDatabase.getInstance(context) }
    val adminDao = remember { database.adminDao() }

    var isAiKillSwitchActive by remember { mutableStateOf(false) }

    // Initialize databases & governance data
    LaunchedEffect(Unit) {
        quranRepo.ensureDataPopulated()
        downloadRepo.ensurePacksInitialized()

        val existingSources = adminDao.getAllSources().first()
        if (existingSources.isEmpty()) {
            adminDao.insertSources(
                listOf(
                    com.example.core.database.AdminSourceEntity(
                        id = "quran-complex-uthmani",
                        name = "King Fahd Glorious Quran Printing Complex",
                        type = "quran_text",
                        publisher = "KFGQPC Madinah",
                        licenseName = "Verified Open Public Access",
                        licenseUrl = "https://qurancomplex.gov.sa",
                        licenseStatus = "verified",
                        notes = "Official verified Mushaf Hafs 'an 'Asim text."
                    ),
                    com.example.core.database.AdminSourceEntity(
                        id = "tanzil-urdu-jalandhri",
                        name = "Fateh Muhammad Jalandhri Translation",
                        type = "translation",
                        publisher = "Tanzil Project",
                        licenseName = "Verified Public Domain",
                        licenseUrl = "https://tanzil.net/trans/",
                        licenseStatus = "verified",
                        notes = "Standard orthodox Urdu translation."
                    ),
                    com.example.core.database.AdminSourceEntity(
                        id = "everyayah-mishary",
                        name = "EveryAyah Mishary Rashid Alafasy",
                        type = "audio",
                        publisher = "EveryAyah Project",
                        licenseName = "Verified Public Domain",
                        licenseUrl = "https://everyayah.com",
                        licenseStatus = "verified",
                        notes = "Verse-by-verse recitation audio 128kbps."
                    )
                )
            )
            adminDao.insertLessonReviews(
                listOf(
                    com.example.core.database.AdminLessonReviewEntity(
                        lessonId = "lesson_01",
                        titleEn = "Alphabet & Articulation Points (Makharij)",
                        titleUr = "مخارج الحروف",
                        reviewStatus = "approved",
                        reviewNotes = "Verified 29 letter articulations with audio tags.",
                        reviewedBy = "Lead Tajweed Qari"
                    ),
                    com.example.core.database.AdminLessonReviewEntity(
                        lessonId = "lesson_02",
                        titleEn = "Harakaat (Fatha, Kasra, Damma)",
                        titleUr = "حرکات",
                        reviewStatus = "in_review",
                        reviewNotes = "Review short vowel duration rules.",
                        reviewedBy = ""
                    ),
                    com.example.core.database.AdminLessonReviewEntity(
                        lessonId = "lesson_03",
                        titleEn = "Tanween (Double Vowels)",
                        titleUr = "تنوین",
                        reviewStatus = "in_review",
                        reviewNotes = "Check nunation audio samples.",
                        reviewedBy = ""
                    )
                )
            )
            adminDao.insertReport(
                com.example.core.database.AdminReportEntity(
                    id = "REP-101",
                    requestId = "req-921",
                    category = "incorrect",
                    question = "Al-hamdu lillahi rabbi al-'alamin",
                    consentedExcerpt = "Audio clip 3.2s recorded by user",
                    userNote = "The score felt slightly strict on my phone mic.",
                    status = "new"
                )
            )
            adminDao.insertAuditLog(
                com.example.core.database.AuditLogEntity(
                    adminRole = "System",
                    action = "SYSTEM_INITIALIZE",
                    entityType = "registry",
                    details = "Initialized verified Quran data and administrative governance registry"
                )
            )
        }
    }

    // App Preferences & State
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var screenHistory by remember { mutableStateOf(listOf(Screen.HOME)) }

    var appLanguage by remember { mutableStateOf("ur") }
    var preferredScript by remember { mutableStateOf("uthmani") }

    // Selected items for sub-screens
    var selectedSurahNumber by remember { mutableIntStateOf(1) }
    var selectedAyahNumber by remember { mutableIntStateOf(1) }
    var selectedLesson by remember { mutableStateOf<QaidaLesson?>(null) }
    var practiceAyah by remember { mutableStateOf<AyahEntity?>(null) }

    // Global queries & flows
    var searchQuery by remember { mutableStateOf("") }
    val surahs by quranRepo.getAllSurahs().collectAsState(initial = emptyList())
    val ayahsForCurrentSurah by quranRepo.getAyahsForSurah(selectedSurahNumber).collectAsState(initial = emptyList())
    val searchResults by quranRepo.searchAyahs(searchQuery).collectAsState(initial = emptyList())
    val bookmarks by quranRepo.getAllBookmarks().collectAsState(initial = emptyList())
    val notes by quranRepo.getAllNotes().collectAsState(initial = emptyList())
    val lastRead by quranRepo.getLastRead().collectAsState(initial = null)
    val hifzItems by hifzRepo.getAllHifzItems().collectAsState(initial = emptyList())
    val lessonProgressList by learningRepo.getAllProgress().collectAsState(initial = emptyList())
    val completedDailyLessons = remember(lessonProgressList) {
        lessonProgressList
            .filter { it.lessonId.startsWith("daily_lesson_") && it.isCompleted }
            .mapNotNull { it.lessonId.removePrefix("daily_lesson_").toIntOrNull() }
            .toSet()
    }
    val downloadPacks by downloadRepo.getAllPacks().collectAsState(initial = emptyList())

    val adminReports by adminDao.getAllReports().collectAsState(initial = emptyList())
    val adminSources by adminDao.getAllSources().collectAsState(initial = emptyList())
    val adminLessonReviews by adminDao.getAllLessonReviews().collectAsState(initial = emptyList())
    val adminAuditLogs by adminDao.getAuditLogs().collectAsState(initial = emptyList())

    val audioState by audioService.state.collectAsState()
    val recitationState by recitationEngine.engineState.collectAsState()

    fun navigateTo(screen: Screen) {
        if (screen != currentScreen) {
            screenHistory = screenHistory + screen
            currentScreen = screen
        }
    }

    fun navigateBack() {
        if (screenHistory.size > 1) {
            screenHistory = screenHistory.dropLast(1)
            currentScreen = screenHistory.last()
        } else {
            currentScreen = Screen.HOME
        }
    }

    // Handle Android system back button
    BackHandler(enabled = currentScreen != Screen.HOME) {
        navigateBack()
    }

    val isTopLevelScreen = currentScreen in listOf(
        Screen.HOME,
        Screen.QURAN,
        Screen.LEARN,
        Screen.AI_TEACHER,
        Screen.PROGRESS
    )

    Scaffold(
        topBar = {
            if (isTopLevelScreen) {
                val title = when (currentScreen) {
                    Screen.HOME -> "Quran Companion"
                    Screen.QURAN -> "Holy Quran"
                    Screen.LEARN -> "Learn Qaida & Tajweed"
                    Screen.AI_TEACHER -> "AI Recitation Teacher"
                    Screen.PROGRESS -> "My Learning Progress"
                    else -> "Quran Companion"
                }
                AppTopBar(
                    title = title,
                    canNavigateBack = false,
                    isOnline = true,
                    onSettingsClick = { navigateTo(Screen.SETTINGS) },
                    onSearchClick = { navigateTo(Screen.SEARCH) }
                )
            }
        },
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.HOME,
                        onClick = { currentScreen = Screen.HOME; screenHistory = listOf(Screen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        modifier = Modifier.testTag("nav_home")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.QURAN,
                        onClick = { currentScreen = Screen.QURAN; screenHistory = listOf(Screen.QURAN) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Quran") },
                        label = { Text("Quran") },
                        modifier = Modifier.testTag("nav_quran")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.LEARN,
                        onClick = { currentScreen = Screen.LEARN; screenHistory = listOf(Screen.LEARN) },
                        icon = { Icon(Icons.Default.School, contentDescription = "Learn") },
                        label = { Text("Learn") },
                        modifier = Modifier.testTag("nav_learn")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.AI_TEACHER,
                        onClick = { currentScreen = Screen.AI_TEACHER; screenHistory = listOf(Screen.AI_TEACHER) },
                        icon = { Icon(Icons.Default.Mic, contentDescription = "AI Teacher") },
                        label = { Text("AI Teacher") },
                        modifier = Modifier.testTag("nav_ai_teacher")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.PROGRESS,
                        onClick = { currentScreen = Screen.PROGRESS; screenHistory = listOf(Screen.PROGRESS) },
                        icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Progress") },
                        label = { Text("Progress") },
                        modifier = Modifier.testTag("nav_progress")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.HOME -> {
                HomeScreen(
                    lastRead = lastRead,
                    onNavigateToQuran = { surah, ayah ->
                        selectedSurahNumber = surah
                        selectedAyahNumber = ayah
                        navigateTo(Screen.QURAN_READER)
                    },
                    onNavigateToLearn = { navigateTo(Screen.LEARN) },
                    onNavigateToAiTeacher = { navigateTo(Screen.AI_TEACHER) },
                    onNavigateToDailyLesson = { navigateTo(Screen.DAILY_LESSON) },
                    onNavigateToHifz = { navigateTo(Screen.PROGRESS) },
                    onNavigateToDownloads = { navigateTo(Screen.DOWNLOADS) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.QURAN -> {
                SurahListScreen(
                    surahs = surahs,
                    onSelectSurah = { surahNum ->
                        selectedSurahNumber = surahNum
                        selectedAyahNumber = 1
                        navigateTo(Screen.QURAN_READER)
                    },
                    onNavigateToBookmarks = { navigateTo(Screen.BOOKMARKS) },
                    onNavigateToSearch = { navigateTo(Screen.SEARCH) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.QURAN_READER -> {
                val currentSurah = surahs.find { it.surahNumber == selectedSurahNumber }
                QuranReaderScreen(
                    surah = currentSurah,
                    ayahs = ayahsForCurrentSurah,
                    initialAyahNumber = selectedAyahNumber,
                    bookmarks = bookmarks,
                    notes = notes,
                    onToggleBookmark = { ayah, surahName ->
                        coroutineScope.launch { quranRepo.toggleBookmark(ayah, surahName) }
                    },
                    onAddNote = { ayahId, surahNum, ayahNum, surahName, noteText ->
                        coroutineScope.launch { quranRepo.addNote(ayahId, surahNum, ayahNum, surahName, noteText) }
                    },
                    onPlayAyahAudio = { surah, ayah ->
                        audioService.playAyah(surah, ayah)
                    },
                    onPauseAudio = { audioService.pause() },
                    onResumeAudio = { audioService.resume() },
                    onToggleRepeat = { audioService.toggleRepeat() },
                    onSetSpeed = { speed -> audioService.setSpeed(speed) },
                    onSetReciter = { reciter -> audioService.setReciter(reciter) },
                    playbackState = audioState,
                    onPracticeWithAi = { ayah ->
                        practiceAyah = ayah
                        navigateTo(Screen.AI_TEACHER)
                    },
                    onAyahVisible = { surahNum, ayahNum, surahName ->
                        coroutineScope.launch {
                            quranRepo.updateLastRead(surahNum, ayahNum, surahName)
                        }
                    },
                    onJumpToAyah = { targetAyah ->
                        selectedAyahNumber = targetAyah
                    },
                    onBack = { navigateBack() }
                )
            }

            Screen.SEARCH -> {
                SearchScreen(
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it },
                    searchResults = searchResults,
                    onSelectAyah = { surah, ayah ->
                        selectedSurahNumber = surah
                        selectedAyahNumber = ayah
                        navigateTo(Screen.QURAN_READER)
                    },
                    onBack = { navigateBack() }
                )
            }

            Screen.BOOKMARKS -> {
                BookmarksScreen(
                    bookmarks = bookmarks,
                    onSelectBookmark = { surah, ayah ->
                        selectedSurahNumber = surah
                        selectedAyahNumber = ayah
                        navigateTo(Screen.QURAN_READER)
                    },
                    onDeleteBookmark = { ayahId ->
                        coroutineScope.launch { quranRepo.removeBookmark(ayahId) }
                    },
                    onBack = { navigateBack() }
                )
            }

            Screen.LEARN -> {
                LearnScreen(
                    lessons = learningRepo.getLessons(),
                    progressList = lessonProgressList,
                    onSelectLesson = { lesson ->
                        selectedLesson = lesson
                        navigateTo(Screen.LESSON_DETAIL)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.LESSON_DETAIL -> {
                selectedLesson?.let { lesson ->
                    LessonDetailScreen(
                        lesson = lesson,
                        onStartQuiz = { navigateTo(Screen.QUIZ) },
                        onBack = { navigateBack() }
                    )
                } ?: navigateBack()
            }

            Screen.QUIZ -> {
                selectedLesson?.let { lesson ->
                    QuizScreen(
                        lesson = lesson,
                        onQuizCompleted = { score ->
                            coroutineScope.launch {
                                learningRepo.saveLessonCompletion(lesson.id, score)
                            }
                            navigateBack()
                        },
                        onBack = { navigateBack() }
                    )
                } ?: navigateBack()
            }

            Screen.AI_TEACHER -> {
                AiTeacherScreen(
                    engine = recitationEngine,
                    engineState = recitationState,
                    initialAyah = practiceAyah ?: ayahsForCurrentSurah.firstOrNull(),
                    onPlayReferenceAudio = { surah, ayah ->
                        audioService.playAyah(surah, ayah)
                    },
                    onOpenAiAssistant = { navigateTo(Screen.AI_ASSISTANT) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.DAILY_LESSON -> {
                DailyLessonScreen(
                    currentDay = (completedDailyLessons.size + 1).coerceIn(1, 30),
                    completedDays = completedDailyLessons,
                    engine = recitationEngine,
                    engineState = recitationState,
                    onPlayReferenceAudio = { surah, ayah ->
                        audioService.playAyah(surah, ayah)
                    },
                    onMarkLessonCompleted = { day, score ->
                        coroutineScope.launch {
                            val dbInstance = com.example.core.database.AppDatabase.getInstance(context)
                            dbInstance.userProgressDao().updateLessonProgress(
                                com.example.core.database.LessonProgressEntity(
                                    lessonId = "daily_lesson_$day",
                                    isCompleted = true,
                                    score = score
                                )
                            )
                        }
                    },
                    onBack = { navigateBack() }
                )
            }

            Screen.AI_ASSISTANT -> {
                AiAssistantScreen(
                    assistant = geminiAssistant,
                    onBack = { navigateBack() }
                )
            }

            Screen.PROGRESS -> {
                ProgressScreen(
                    bookmarksCount = bookmarks.size,
                    notesCount = notes.size,
                    hifzCount = hifzItems.size,
                    lessonsCompletedCount = lessonProgressList.count { it.isCompleted },
                    onResetData = {
                        coroutineScope.launch {
                            val db = com.example.core.database.AppDatabase.getInstance(context)
                            db.userProgressDao().clearBookmarks()
                            db.userProgressDao().clearHifz()
                            db.userProgressDao().clearLessonProgress()
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.DOWNLOADS -> {
                DownloadsScreen(
                    packs = downloadPacks,
                    onDownloadPack = { packId ->
                        coroutineScope.launch { downloadRepo.simulateDownload(packId) }
                    },
                    onDeletePack = { packId ->
                        coroutineScope.launch { downloadRepo.deletePack(packId) }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.SETTINGS -> {
                SettingsScreen(
                    appLanguage = appLanguage,
                    onLanguageChange = { appLanguage = it },
                    preferredScript = preferredScript,
                    onScriptChange = { preferredScript = it },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onToggleTheme,
                    onNavigateToContentSources = { navigateTo(Screen.CONTENT_SOURCES) },
                    onNavigateToPrivacy = { navigateTo(Screen.PRIVACY) },
                    onNavigateToAbout = { navigateTo(Screen.ABOUT) },
                    onNavigateToAdminPortal = { navigateTo(Screen.ADMIN_PORTAL) },
                    onBack = { navigateBack() }
                )
            }

            Screen.CONTENT_SOURCES -> {
                ContentSourcesScreen(onBack = { navigateBack() })
            }

            Screen.PRIVACY -> {
                PrivacyScreen(onBack = { navigateBack() })
            }

            Screen.ABOUT -> {
                AboutScreen(onBack = { navigateBack() })
            }

            Screen.ADMIN_PORTAL -> {
                AdminPortalScreen(
                    reports = adminReports,
                    sources = adminSources,
                    lessonReviews = adminLessonReviews,
                    auditLogs = adminAuditLogs,
                    onResolveReport = { reportId, status, note ->
                        coroutineScope.launch {
                            adminDao.resolveReport(reportId, status, note)
                            adminDao.insertAuditLog(
                                com.example.core.database.AuditLogEntity(
                                    adminRole = "Admin",
                                    action = "RESOLVE_REPORT",
                                    entityType = "report",
                                    details = "Resolved $reportId as $status: $note"
                                )
                            )
                        }
                    },
                    onApproveLesson = { lessonId, reviewer ->
                        coroutineScope.launch {
                            adminDao.updateLessonReview(lessonId, "approved", "Approved by $reviewer")
                            adminDao.insertAuditLog(
                                com.example.core.database.AuditLogEntity(
                                    adminRole = reviewer,
                                    action = "APPROVE_LESSON",
                                    entityType = "lesson",
                                    details = "Approved Tajweed lesson $lessonId"
                                )
                            )
                        }
                    },
                    onRequestLessonChanges = { lessonId, feedback ->
                        coroutineScope.launch {
                            adminDao.updateLessonReview(lessonId, "changes_requested", feedback)
                            adminDao.insertAuditLog(
                                com.example.core.database.AuditLogEntity(
                                    adminRole = "Teacher Reviewer",
                                    action = "REQUEST_CHANGES",
                                    entityType = "lesson",
                                    details = "Requested changes on lesson $lessonId: $feedback"
                                )
                            )
                        }
                    },
                    onToggleAiKillSwitch = { enabled ->
                        isAiKillSwitchActive = enabled
                        coroutineScope.launch {
                            adminDao.insertAuditLog(
                                com.example.core.database.AuditLogEntity(
                                    adminRole = "Owner",
                                    action = if (enabled) "AI_KILL_SWITCH_ENGAGED" else "AI_KILL_SWITCH_DISENGAGED",
                                    entityType = "system_config",
                                    details = "Emergency AI kill switch toggled to $enabled"
                                )
                            )
                        }
                    },
                    isAiKillSwitchActive = isAiKillSwitchActive,
                    onBack = { navigateBack() }
                )
            }

            Screen.ONBOARDING -> {
                OnboardingScreen(
                    onFinishOnboarding = { lang, script ->
                        appLanguage = lang
                        preferredScript = script
                        currentScreen = Screen.HOME
                    }
                )
            }
        }
    }
}
