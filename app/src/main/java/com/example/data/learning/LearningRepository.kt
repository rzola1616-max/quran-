package com.example.data.learning

import android.content.Context
import com.example.core.database.AppDatabase
import com.example.core.database.LessonProgressEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

data class QaidaLesson(
    val id: String,
    val path: String, // "qaida", "tajweed"
    val titleEn: String,
    val titleUr: String,
    val descriptionEn: String,
    val descriptionUr: String,
    val examples: List<LessonExample>,
    val quizQuestions: List<QuizQuestion>
)

data class LessonExample(
    val arabic: String,
    val transliteration: String,
    val tipEn: String,
    val tipUr: String
)

data class QuizQuestion(
    val questionEn: String,
    val questionUr: String,
    val promptArabic: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationEn: String
)

class LearningRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val progressDao = db.userProgressDao()

    fun getAllProgress(): Flow<List<LessonProgressEntity>> =
        progressDao.getAllLessonProgress()

    suspend fun saveLessonCompletion(lessonId: String, score: Int) = withContext(Dispatchers.IO) {
        progressDao.updateLessonProgress(
            LessonProgressEntity(
                lessonId = lessonId,
                isCompleted = true,
                score = score,
                lastAttemptTimestamp = System.currentTimeMillis()
            )
        )
    }

    fun getLessons(): List<QaidaLesson> {
        return listOf(
            QaidaLesson(
                id = "qaida_1",
                path = "qaida",
                titleEn = "Lesson 1: Individual Letters (Mufradat)",
                titleUr = "سبق 1: مفردات (حروفِ تہجی)",
                descriptionEn = "Learn the 28 core Arabic letters and their exact articulation points (Makharij).",
                descriptionUr = "قرآن پاک کے 28 بنیادی حروف اور ان کے درست مخارج سیکھیں۔",
                examples = listOf(
                    LessonExample("ا", "Alif", "Pronounced from the emptiness of mouth and throat", "حلق اور منہ کے خالی حصے سے"),
                    LessonExample("ب", "Baa", "From the wet portion of the lips", "دونوں ہونٹوں کے تری والے حصے سے"),
                    LessonExample("ت", "Taa", "Tip of tongue touching the roots of upper front teeth", "زبان کی نوک اوپر کے سامنے والے دو دانتوں کی جڑ سے"),
                    LessonExample("ث", "Thaa", "Tip of tongue touching edge of upper front teeth softly", "زبان کا کونا اوپر کے اگلے دانتوں کے اندرونی کنارے سے"),
                    LessonExample("ج", "Jeem", "Middle of the tongue touching the hard palate", "زبان کا درمیانی حصہ تالو کے ساتھ مل کر"),
                    LessonExample("ح", "Haa", "Middle of the throat with clear exhaled breath", "حلق کے درمیانی حصے سے صاف آواز میں"),
                    LessonExample("خ", "Khaa", "Top of the throat nearest to the mouth", "حلق کے اوپری حصے سے")
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        questionEn = "Which letter is articulated from the moist part of the lips?",
                        questionUr = "کون سا حرف ہونٹوں کے تری والے حصے سے ادا ہوتا ہے؟",
                        promptArabic = "ب",
                        options = listOf("Baa (ب)", "Taa (ت)", "Alif (ا)", "Jeem (ج)"),
                        correctIndex = 0,
                        explanationEn = "Baa (ب) is articulated by bringing both lips together firmly."
                    ),
                    QuizQuestion(
                        questionEn = "Identify the letter pronounced from the middle of the throat:",
                        questionUr = "حلق کے درمیانی حصے سے ادا ہونے والا حرف پہچانیں:",
                        promptArabic = "ح",
                        options = listOf("Khaa (خ)", "Haa (ح)", "Alif (ا)", "Thaa (ث)"),
                        correctIndex = 1,
                        explanationEn = "Haa (ح) comes from the central throat with clear airflow."
                    )
                )
            ),
            QaidaLesson(
                id = "qaida_2",
                path = "qaida",
                titleEn = "Lesson 2: Short Vowels (Harakat)",
                titleUr = "سبق 2: حرکات (زبر، زیر، پیش)",
                descriptionEn = "Master Fatha (Zabar /a/), Kasra (Zair /i/), and Damma (Paish /u/).",
                descriptionUr = "حرکاتِ ثلاثہ: زبر، زیر اور پیش کی درست صوتی مقدار سیکھیں۔",
                examples = listOf(
                    LessonExample("بَ", "Ba (Fatha)", "Short /a/ sound, do not stretch", "زبر کی آواز اوپر کو اٹھتی ہے"),
                    LessonExample("بِ", "Bi (Kasra)", "Short /i/ sound, lower the jaw gently", "زیر کی آواز نیچے کو جھکتی ہے"),
                    LessonExample("بُ", "Bu (Damma)", "Short /u/ sound, round the lips nicely", "پیش کی ادائیگی میں ہونٹ گول ہوتے ہیں"),
                    LessonExample("دَ رَ سَ", "Da-Ra-Sa", "Three open short vowels in smooth sequence", "تینوں پر زبر، بغیر کھینچے رواں پڑھیں")
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        questionEn = "Which movement requires rounding of the lips?",
                        questionUr = "کس حرکت کی ادائیگی میں دونوں ہونٹ گول کیے جاتے ہیں؟",
                        promptArabic = "بُ",
                        options = listOf("Fatha (Zabar)", "Kasra (Zair)", "Damma (Paish)", "Sukoon"),
                        correctIndex = 2,
                        explanationEn = "Damma (Paish) requires rounding the lips to produce the /u/ vowel."
                    )
                )
            ),
            QaidaLesson(
                id = "qaida_3",
                path = "qaida",
                titleEn = "Lesson 3: Tanween (Double Vowels)",
                titleUr = "سبق 3: تنوین (دو زبر، دو زیر، دو پیش)",
                descriptionEn = "Double Fatha (-an), Double Kasra (-in), and Double Damma (-un) with hidden Noon Sakin.",
                descriptionUr = "تنوین کی حقیقت نون ساکن ہے جو آواز میں ظاہر ہوتی ہے لیکن لکھی نہیں جاتی۔",
                examples = listOf(
                    LessonExample("بً", "Ban", "Fathatain: produces an 'an' sound", "دو زبر: بن کی آواز"),
                    LessonExample("بٍ", "Bin", "Kasratain: produces an 'in' sound", "دو زیر: بن کی آواز"),
                    LessonExample("بٌ", "Bun", "Dammatain: produces an 'un' sound", "دو پیش: بن کی آواز"),
                    LessonExample("عَلِيمًا", "'Aleeman", "Tanween on final letter followed by Alif", "عَلِيمًا (دو زبر کے ساتھ نون کی آواز)")
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        questionEn = "What letter sound is embedded inside Tanween?",
                        questionUr = "تنوین کی اندرونی صوتی حقیقت کون سے حرف کی ہوتی ہے؟",
                        promptArabic = "بً",
                        options = listOf("Meem Sakin", "Noon Sakin (نْ)", "Waw Maddah", "Alif"),
                        correctIndex = 1,
                        explanationEn = "Tanween produces the sound of a Noon Sakin (نْ) at the end of the word."
                    )
                )
            ),
            QaidaLesson(
                id = "tajweed_1",
                path = "tajweed",
                titleEn = "Tajweed Rule: Noon Sakin & Tanween",
                titleUr = "تجوید کا قاعدہ: نون ساکن و تنوین کے احکام",
                descriptionEn = "The 4 essential rules: Izhar (Clarity), Idgham (Merging), Iqlab (Conversion), and Ikhfa (Concealment).",
                descriptionUr = "نون ساکن اور تنوین کے 4 اہم احکام: اظہار، ادغام، اقلاب، اور اخفاء۔",
                examples = listOf(
                    LessonExample("مَنْ آمَنَ", "Izhar (Clarity)", "Pronounce Noon clearly before throat letters (ء ه ع ح غ خ)", "حروفِ حلقی سے پہلے نون کو بغیر غنہ صاف پڑھیں"),
                    LessonExample("مَن يَقُولُ", "Idgham (Merging)", "Merge Noon into Ya with Ghunnah (ی ن م و)", "یرملون کے حروف میں نون کو ملا کر غنہ کریں"),
                    LessonExample("مِن بَعْدِ", "Iqlab (Conversion)", "Turn Noon into gentle Meem before Ba (ب)", "ب سے پہلے نون کو میم سے بدل کر غنہ کریں"),
                    LessonExample("مِن قَبْلِ", "Ikhfa (Concealment)", "Conceal Noon with gentle nasalization for 1.5 seconds", "حروفِ اخفاء کے وقت نون کی آواز ناک میں چھپائیں")
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        questionEn = "In 'مِن بَعْدِ' (Min Ba'di), which Tajweed rule applies to the Noon Sakin?",
                        questionUr = "'مِن بَعْدِ' میں نون ساکن پر کون سا تجویدی قاعدہ لاگو ہوتا ہے؟",
                        promptArabic = "مِن بَعْدِ",
                        options = listOf("Izhar", "Idgham", "Iqlab (Turn into Meem)", "Qalqalah"),
                        correctIndex = 2,
                        explanationEn = "Before the letter Baa (ب), Noon Sakin is converted into a gentle Meem (Iqlab) with Ghunnah."
                    ),
                    QuizQuestion(
                        questionEn = "How many letters cause Izhar (Clear Pronunciation)?",
                        questionUr = "حروفِ حلقی (اظہار کے حروف) کتنے ہیں؟",
                        promptArabic = "ء ه ع ح غ خ",
                        options = listOf("4 letters", "6 letters (Throat letters)", "15 letters", "2 letters"),
                        correctIndex = 1,
                        explanationEn = "The 6 throat letters are Hamzah, Haa, 'Ayn, Haa, Ghayn, and Khaa."
                    )
                )
            ),
            QaidaLesson(
                id = "tajweed_2",
                path = "tajweed",
                titleEn = "Tajweed Rule: Qalqalah (Echo Sound)",
                titleUr = "تجوید کا قاعدہ: قلقلہ (گونج دار آواز)",
                descriptionEn = "The 5 bouncing letters (ق ط ب ج د - Qutb Jad) when carrying a Sukoon.",
                descriptionUr = "قطب جد (ق، ط، ب، ج، د) جب ساکن ہوں تو مخرج میں گونج پیدا ہوتی ہے۔",
                examples = listOf(
                    LessonExample("قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Al-Falaq (Stop)", "Strong bounce on Qaf at verse end (Qalqalah Kubra)", "وقف کی حالت میں ق پر قوی قلقلہ"),
                    LessonExample("لَمْ يَلِدْ", "Lam Yalid", "Clear bounce on Dal Sakinah", "دال ساکن پر گونج پیدا کریں"),
                    LessonExample("تَبَّتْ يَدَا أَبِي لَهَبٍ وَتَبَّ", "Wa Tabb", "Maximum Qalqalah on Shaddah stop", "مشدد حرف پر وقف کی صورت میں سب سے قوی قلقلہ")
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        questionEn = "Which phrase mnemonic combines the 5 Qalqalah letters?",
                        questionUr = "قلقلہ کے 5 حروف کا مجموعہ کیا ہے؟",
                        promptArabic = "ق ط ب ج د",
                        options = listOf("Yarmaloon", "Qutb Jad (قُطْبُ جَدٍّ)", "Huruf Halqi", "Huroof Maddah"),
                        correctIndex = 1,
                        explanationEn = "The mnemonic 'Qutb Jad' (ق ط ب ج د) unites the five echo letters."
                    )
                )
            )
        )
    }
}
