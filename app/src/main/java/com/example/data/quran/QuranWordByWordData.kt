package com.example.data.quran

data class WordMeaning(
    val position: Int,
    val arabic: String,
    val transliteration: String,
    val english: String,
    val urdu: String
)

object QuranWordByWordData {
    fun getWordBreakdown(ayahId: Int): List<WordMeaning> {
        return when (ayahId) {
            1001 -> listOf(
                WordMeaning(1, "بِسْمِ", "Bismi", "In the name", "نام سے"),
                WordMeaning(2, "اللَّهِ", "Allahi", "of Allah", "اللہ کے"),
                WordMeaning(3, "الرَّحْمَٰنِ", "Ar-Rahmani", "the Entirely Merciful", "بڑا مہربان"),
                WordMeaning(4, "الرَّحِيمِ", "Ar-Raheemi", "the Especially Merciful", "نہایت رحم والا")
            )
            1002 -> listOf(
                WordMeaning(1, "الْحَمْدُ", "Al-Hamdu", "[All] praise", "تمام تعریفیں"),
                WordMeaning(2, "لِلَّهِ", "Lillahi", "[is due] to Allah", "اللہ ہی کے لیے ہیں"),
                WordMeaning(3, "رَبِّ", "Rabbi", "Lord", "پروردگار"),
                WordMeaning(4, "الْعَالَمِينَ", "Al-'Alamina", "of the worlds", "تمام جہانوں کا")
            )
            1003 -> listOf(
                WordMeaning(1, "الرَّحْمَٰنِ", "Ar-Rahmani", "The Entirely Merciful", "بہت بڑا مہربان"),
                WordMeaning(2, "الرَّحِيمِ", "Ar-Raheemi", "the Especially Merciful", "نہایت رحم والا")
            )
            1004 -> listOf(
                WordMeaning(1, "مَالِكِ", "Maliki", "Sovereign / Master", "مالک"),
                WordMeaning(2, "يَوْمِ", "Yawmi", "of the Day", "دن کا"),
                WordMeaning(3, "الدِّينِ", "Ad-Deeni", "of Recompense / Judgment", "بدلے کے")
            )
            1005 -> listOf(
                WordMeaning(1, "إِيَّاكَ", "Iyyaka", "You alone", "صرف تجھ ہی کو"),
                WordMeaning(2, "نَعْبُدُ", "Na'budu", "we worship", "ہم پوجتے ہیں"),
                WordMeaning(3, "وَإِيَّاكَ", "Wa Iyyaka", "and You alone", "اور صرف تجھ ہی سے"),
                WordMeaning(4, "نَسْتَعِينُ", "Nasta'een", "we ask for help", "ہم مدد چاہتے ہیں")
            )
            1006 -> listOf(
                WordMeaning(1, "اهْدِنَا", "Ihdina", "Guide us", "ہمیں چلا"),
                WordMeaning(2, "الصِّرَاطَ", "As-Sirata", "the path", "راستہ"),
                WordMeaning(3, "الْمُسْتَقِيمَ", "Al-Mustaqeema", "the straight", "سیدھا")
            )
            1007 -> listOf(
                WordMeaning(1, "صِرَاطَ", "Sirata", "The path", "راستہ"),
                WordMeaning(2, "الَّذِينَ", "Alladhina", "of those", "ان لوگوں کا"),
                WordMeaning(3, "أَنْعَمْتَ", "An'amta", "You have bestowed favor", "تو نے انعام کیا"),
                WordMeaning(4, "عَلَيْهِمْ", "'Alayhim", "upon them", "ان پر"),
                WordMeaning(5, "غَيْرِ", "Ghayri", "not of", "نہ کہ"),
                WordMeaning(6, "الْمَغْضُوبِ", "Al-Maghdoobi", "those who have earned anger", "جن پر غضب ہوا"),
                WordMeaning(7, "عَلَيْهِمْ", "'Alayhim", "upon them", "ان پر"),
                WordMeaning(8, "وَلَا", "Wa La", "and nor", "اور نہ"),
                WordMeaning(9, "الضَّالِّينَ", "Ad-Dallina", "those who are astray", "گمراہوں کا")
            )
            112001 -> listOf(
                WordMeaning(1, "قُلْ", "Qul", "Say", "کہہ دیجیے"),
                WordMeaning(2, "هُوَ", "Huwa", "He is", "وہ"),
                WordMeaning(3, "اللَّهُ", "Allahu", "Allah", "اللہ"),
                WordMeaning(4, "أَحَدٌ", "Ahad", "[who is] One", "ایک ہے")
            )
            112002 -> listOf(
                WordMeaning(1, "اللَّهُ", "Allahu", "Allah", "اللہ"),
                WordMeaning(2, "الصَّمَدُ", "As-Samad", "the Eternal Refuge / Absolute", "بے نیاز ہے")
            )
            112003 -> listOf(
                WordMeaning(1, "لَمْ", "Lam", "He neither", "نہ"),
                WordMeaning(2, "يَلِدْ", "Yalid", "begets", "اس نے کسی کو جنا"),
                WordMeaning(3, "وَلَمْ", "Wa Lam", "and nor", "اور نہ"),
                WordMeaning(4, "يُولَدْ", "Yoolad", "is born", "وہ جنا گیا")
            )
            112004 -> listOf(
                WordMeaning(1, "وَلَمْ", "Wa Lam", "And not", "اور نہیں"),
                WordMeaning(2, "يَكُن", "Yakun", "is", "ہے"),
                WordMeaning(3, "لَّهُ", "Lahu", "for Him", "اس کے لیے"),
                WordMeaning(4, "كُفُوًا", "Kufuwan", "equal / equivalent", "برابر"),
                WordMeaning(5, "أَحَدٌ", "Ahad", "anyone", "کوئی ایک بھی")
            )
            2255 -> listOf(
                WordMeaning(1, "اللَّهُ", "Allahu", "Allah", "اللہ"),
                WordMeaning(2, "لَا إِلَٰهَ", "La Ilaha", "there is no deity", "کوئی معبود نہیں"),
                WordMeaning(3, "إِلَّا هُوَ", "Illa Huwa", "except Him", "سوائے اس کے"),
                WordMeaning(4, "الْحَيُّ", "Al-Hayyu", "the Ever-Living", "زندہ جاوید"),
                WordMeaning(5, "الْقَيُّومُ", "Al-Qayyoomu", "the Sustainer of all", "سب کا سنبھالنے والا"),
                WordMeaning(6, "لَا تَأْخُذُهُ", "La Ta'khudhuhu", "neither overtakes Him", "نہ اسے پکڑتی ہے"),
                WordMeaning(7, "سِنَةٌ", "Sinatun", "drowsiness / slumber", "اونگھ"),
                WordMeaning(8, "وَلَا نَوْمٌ", "Wa La Nawm", "nor sleep", "اور نہ نیند")
            )
            else -> emptyList()
        }
    }
}
