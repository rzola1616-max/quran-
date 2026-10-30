package com.example.data.daily_lesson

import com.example.core.database.AyahEntity

data class DailyLesson(
    val dayNumber: Int,
    val titleUr: String,
    val titleEn: String,
    val surahNumber: Int,
    val surahNameArabic: String,
    val surahNameUr: String,
    val startAyah: Int,
    val endAyah: Int,
    val targetAyahs: List<AyahEntity>,
    val tajweedFocusUr: String,
    val makhrajTipUr: String,
    val virtueUr: String,
    val estimatedMinutes: Int = 5
)

object DailyLessonData {
    fun getAllLessons(): List<DailyLesson> {
        val list = mutableListOf<DailyLesson>()

        // Day 1: Al-Fatihah (Complete)
        list.add(
            DailyLesson(
                dayNumber = 1,
                titleUr = "ام القرآن کی درست تلاوت",
                titleEn = "Reciting Al-Fatihah with Accurate Makharij",
                surahNumber = 1,
                surahNameArabic = "الفاتحة",
                surahNameUr = "سورۃ الفاتحہ",
                startAyah = 1,
                endAyah = 7,
                targetAyahs = listOf(
                    AyahEntity(1001, 1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِیْمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔", 1, 1, 1),
                    AyahEntity(1002, 1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِیْنَ", "[All] praise is [due] to Allah, Lord of the worlds -", "سب تعریفیں اللہ ہی کے لیے ہیں جو تمام جہانوں کا پروردگار ہے۔", 1, 1, 1),
                    AyahEntity(1003, 1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "الرَّحْمٰنِ الرَّحِیْمِ", "The Entirely Merciful, the Especially Merciful,", "بہت بڑا مہربان، نہایت رحم کرنے والا۔", 1, 1, 1),
                    AyahEntity(1004, 1, 4, "مَالِكِ يَوْمِ الدِّينِ", "مٰلِكِ یَوْمِ الدِّیْنِ", "Sovereign of the Day of Recompense.", "روزِ جزا کا مالک ہے۔", 1, 1, 1),
                    AyahEntity(1005, 1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "اِیَّاكَ نَعْبُدُ وَاِیَّاكَ نَسْتَعِیْنُ", "It is You we worship and You we ask for help.", "ہم صرف تیری ہی عبادت کرتے ہیں اور صرف تجھ ہی سے مدد چاہتے ہیں۔", 1, 1, 1),
                    AyahEntity(1006, 1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "اِهْدِنَا الصِّرَاطَ الْمُسْتَقِیْمَ", "Guide us to the straight path -", "ہمیں سیدھے راستے کی ہدایت فرما۔", 1, 1, 1),
                    AyahEntity(1007, 1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "صِرَاطَ الَّذِیْنَ اَنْعَمْتَ عَلَیْهِمْ ۙ غَیْرِ الْمَغْضُوْبِ عَلَیْهِمْ وَلَا الضَّآلِّیْنَ", "The path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.", "ان لوگوں کے راستے پر جن پر تو نے انعام فرمایا، جن پر نہ غضب ہوا اور نہ وہ گمراہ ہوئے۔", 1, 1, 1)
                ),
                tajweedFocusUr = "حروفِ حلقیہ (ح، ع) اور موٹے حروف (ص، ط، ق) کی صاف ادائیگی۔",
                makhrajTipUr = "'الْحَمْدُ' اور 'الرَّحْمٰنِ' میں 'ح' کو حلق کے درمیان سے ادا کریں، عام 'ہ' کی طرح نہ پڑھیں۔",
                virtueUr = "سورۃ الفاتحہ ہر نماز کی بنیاد اور تمام بیماریوں کی شفا ہے۔"
            )
        )

        // Day 2: Al-Baqarah 1-5 (Opening)
        list.add(
            DailyLesson(
                dayNumber = 2,
                titleUr = "متقین کی صفات اور غنہ کے احکام",
                titleEn = "Qualities of the Righteous & Rules of Ghunnah",
                surahNumber = 2,
                surahNameArabic = "البقرة",
                surahNameUr = "سورۃ البقرہ",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(2001, 2, 1, "الم", "الٓمّٓ", "Alif, Lam, Meem.", "الف، لام، میم۔", 1, 2, 1),
                    AyahEntity(2002, 2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "ذٰلِكَ الْكِتٰبُ لَا رَیْبَ ۛ فِیْهِ ۛ هُدًى لِّلْمُتَّقِیْنَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -", "یہ وہ کتاب ہے جس میں کوئی شک نہیں، پرہیزگاروں کے لیے ہدایت ہے۔", 1, 2, 1),
                    AyahEntity(2003, 2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "الَّذِیْنَ یُؤْمِنُوْنَ بِالْغَیْبِ وَیُقِیْمُوْنَ الصَّلٰوةَ وَمِمَّا رَزَقْنٰهُمْ یُنْفِقُوْنَ", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,", "جو غیب پر ایمان لاتے ہیں، نماز قائم کرتے ہیں، اور جو کچھ ہم نے دیا اس میں سے خرچ کرتے ہیں۔", 1, 2, 1),
                    AyahEntity(2004, 2, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ", "وَالَّذِیْنَ یُؤْمِنُوْنَ بِمَآ اُنْزِلَ اِلَیْكَ وَمَآ اُنْزِلَ مِنْ قَبْلِكَ ۚ وَبِالْاٰخِرَةِ هُمْ یُوْقِنُوْنَ", "And who believe in what has been revealed to you and what was revealed before you and in the Hereafter they are certain.", "اور جو ایمان لاتے ہیں اس پر جو آپ پر نازل کیا گیا اور جو آپ سے پہلے نازل کیا گیا، اور آخرت پر وہ یقین رکھتے ہیں۔", 1, 2, 1),
                    AyahEntity(2005, 2, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ", "اُولٰٓىِٕكَ عَلٰى هُدًى مِّنْ رَّبِّهِمْ ۙ وَاُولٰٓىِٕكَ هُمُ الْمُفْلِحُوْنَ", "Those are upon guidance from their Lord, and it is those who are the successful.", "یہی لوگ اپنے رب کی طرف سے ہدایت پر ہیں اور یہی فلاح پانے والے ہیں۔", 1, 2, 1)
                ),
                tajweedFocusUr = "حروفِ مقطعات (الٓمّٓ) میں مدِ لازم اور 'مِمَّا' میں میم مشدد کا غنہ۔",
                makhrajTipUr = "'یُنْفِقُوْنَ' میں نون ساکن کے بعد 'ف' پر اخفاء کریں (ناک میں آواز چھپائیں)۔",
                virtueUr = "سورۃ البقرہ کا پڑھنا برکت ہے اور چھوڑنا حسرت، اور جادوگر اس پر قابو نہیں پا سکتے۔"
            )
        )

        // Day 3: Ayat-ul-Kursi (Al-Baqarah 255)
        list.add(
            DailyLesson(
                dayNumber = 3,
                titleUr = "آیۃ الکرسی اور حروفِ مستعلیہ",
                titleEn = "Ayat-ul-Kursi & Emphatic Letters",
                surahNumber = 2,
                surahNameArabic = "البقرة",
                surahNameUr = "سورۃ البقرہ",
                startAyah = 255,
                endAyah = 255,
                targetAyahs = listOf(
                    AyahEntity(
                        2255, 2, 255,
                        "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
                        "اَللّٰهُ لَاۤ اِلٰهَ اِلَّا هُوَ ۚ اَلْحَیُّ الْقَیُّوْمُ ۚ لَا تَاْخُذُهٗ سِنَةٌ وَّلَا نَوْمٌ ؕ لَهٗ مَا فِی السَّمٰوٰتِ وَمَا فِی الْاَرْضِ ؕ مَنْ ذَا الَّذِیْ یَشْفَعُ عِنْدَهٗۤ اِلَّا بِاِذْنِهٖ ؕ یَعْلَمُ مَا بَیْنَ اَیْدِیْهِمْ وَمَا خَلْفَهُمْ ۚ وَلَا یُحِیْطُوْنَ بِشَیْءٍ مِّنْ عِلْمِهٖۤ اِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِیُّهُ السَّمٰوٰتِ وَالْاَرْضَ ۚ وَلَا یَـُٔوْدُهٗ حِفْظُهُمَا ۚ وَهُوَ الْعَلِیُّ الْعَظِیْمُ",
                        "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence...",
                        "اللہ کے سوا کوئی معبود نہیں، وہ ہمیشہ زندہ رہنے والا اور سب کو سنبھالنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند...",
                        3, 42, 1
                    )
                ),
                tajweedFocusUr = "حرف 'ظ' اور 'ض' کا درست مخرج اور لفظِ جلالہ (اللہ) کا پر پڑھنا۔",
                makhrajTipUr = "'الْعَظِیْمُ' میں 'ظ' زبان کی نوک اوپر کے سامنے والے دانتوں کے کناروں سے لگا کر موٹا ادا کریں۔",
                virtueUr = "ہر فرض نماز کے بعد آیۃ الکرسی پڑھنے والے کے جنت میں جانے میں صرف موت حائل ہے۔"
            )
        )

        // Day 4: Al-Baqarah 285-286 (Khavateem)
        list.add(
            DailyLesson(
                dayNumber = 4,
                titleUr = "خواتیم سورة البقرۃ اور دعائیں",
                titleEn = "Final Verses of Al-Baqarah & Dua",
                surahNumber = 2,
                surahNameArabic = "البقرة",
                surahNameUr = "سورۃ البقرہ",
                startAyah = 285,
                endAyah = 286,
                targetAyahs = listOf(
                    AyahEntity(
                        2285, 2, 285,
                        "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ",
                        "اٰمَنَ الرَّسُوْلُ بِمَآ اُنْزِلَ اِلَیْهِ مِنْ رَّبِّهٖ وَالْمُؤْمِنُوْنَ ؕ كُلٌّ اٰمَنَ بِاللّٰهِ وَمَلٰٓىِٕكَتِهٖ وَكُتُبِهٖ وَرُسُلِهٖ ۫ لَا نُفَرِّقُ بَیْنَ اَحَدٍ مِّنْ رُّسُلِهٖ ۫ وَقَالُوْا سَمِعْنَا وَاَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَاِلَیْكَ الْمَصِیْرُ",
                        "The Messenger has believed in what was revealed to him from his Lord, and so have the believers...",
                        "رسول اس پر ایمان لائے جو ان کے رب کی طرف سے ان پر نازل ہوا اور تمام مومنین بھی...",
                        3, 49, 1
                    ),
                    AyahEntity(
                        2286, 2, 286,
                        "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
                        "لَا یُكَلِّفُ اللّٰهُ نَفْسًا اِلَّا وُسْعَهَا ؕ لَهَا مَا كَسَبَتْ وَعَلَیْهَا مَا اكْتَسَبَتْ ؕ رَبَّنَا لَا تُؤَاخِذْنَاۤ اِنْ نَّسِیْنَاۤ اَوْ اَخْطَاْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَیْنَاۤ اِصْرًا كَمَا حَمَلْتَهٗ عَلَى الَّذِیْنَ مِنْ قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهٖ ۚ وَاعْفُ عَنَّا ۪ وَاغْفِرْ لَنَا ۪ وَارْحَمْنَا ۪ اَنْتَ مَوْلٰىنَا فَانْصُرْنَا عَلَى الْقَوْمِ الْكٰفِرِیْنَ",
                        "Allah does not burden a soul beyond that it can bear...",
                        "اللہ کسی جان پر اس کی طاقت سے زیادہ بوجھ نہیں ڈالتا...",
                        3, 49, 1
                    )
                ),
                tajweedFocusUr = "نون ساکن پر ادغام بلا غنہ (مِن رَّبِّهِ) اور مدِ منفصل۔",
                makhrajTipUr = "'غُفْرَانَكَ' میں 'غ' حلق کے اوپری حصے سے ادا کریں، حلق کو کھردرے پن کے بغیر صاف آواز دیں۔",
                virtueUr = "جو رات کو سورۃ البقرہ کی آخری دو آیات پڑھ لے وہ اس کے لیے (ہر برائی سے) کافی ہو جاتی ہیں۔"
            )
        )

        // Day 5: Surah Al-Kahf 1-5
        list.add(
            DailyLesson(
                dayNumber = 5,
                titleUr = "سورۃ الکہف: فتنوں سے امان",
                titleEn = "Surah Al-Kahf: Protection from Trials",
                surahNumber = 18,
                surahNameArabic = "الكهف",
                surahNameUr = "سورۃ الکہف",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(18001, 18, 1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ", "اَلْحَمْدُ لِلّٰهِ الَّذِیْۤ اَنْزَلَ عَلٰى عَبْدِهِ الْكِتٰبَ وَلَمْ یَجْعَلْ لَّهٗ عِوَجًا ۜ", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.", "سب تعریف اللہ ہی کے لیے ہے جس نے اپنے بندے پر کتاب نازل فرمائی اور اس میں کوئی ٹیڑھ پن نہیں رکھا۔", 15, 293, 1),
                    AyahEntity(18002, 18, 2, "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "قَیِّمًا لِّیُنْذِرَ بَاْسًا شَدِیْدًا مِّنْ لَّدُنْهُ وَیُبَشِّرَ الْمُؤْمِنِیْنَ الَّذِیْنَ یَعْمَلُوْنَ الصّٰلِحٰتِ اَنَّ لَهُمْ اَجْرًا حَسَنًا", "To warn of severe punishment from Him and to give good tidings to the believers...", "سیدھی کچی کتاب تاکہ وہ اللہ کی طرف سے سخت عذاب سے ڈرائے اور مومنوں کو خوشخبری دے...", 15, 293, 1),
                    AyahEntity(18003, 18, 3, "مَّاكِثِينَ فِيهِ أَبَدًا", "مَّاكِثِیْنَ فِیْهِ اَبَدًا", "In which they will remain forever", "جس میں وہ ہمیشہ رہیں گے", 15, 293, 1)
                ),
                tajweedFocusUr = "عِوَجًا میں سکتہ (بغیر سانس توڑے تھوڑی دیر آواز روکنا) اور تنوین کے احکام۔",
                makhrajTipUr = "'قَیِّمًا' میں قاف پر موٹا زبر ادا کریں اور یاء مشدد کو مضبوطی سے واضح کریں۔",
                virtueUr = "جمعہ کے دن سورۃ الکہف پڑھنے والے کے لیے دو جمعوں کے درمیان نور روشن رہتا ہے۔"
            )
        )

        // Day 6: Ya-Sin 1-6 (Heart of Quran)
        list.add(
            DailyLesson(
                dayNumber = 6,
                titleUr = "سورۃ یٰس: قلب القرآن",
                titleEn = "Surah Ya-Sin: Heart of the Quran",
                surahNumber = 36,
                surahNameArabic = "يس",
                surahNameUr = "سورۃ یٰس",
                startAyah = 1,
                endAyah = 6,
                targetAyahs = listOf(
                    AyahEntity(36001, 36, 1, "يس", "یٰسٓ", "Ya, Seen.", "یا، سین۔", 22, 440, 1),
                    AyahEntity(36002, 36, 2, "وَالْقُرْآنِ الْحَكِيمِ", "وَالْقُرْاٰنِ الْحَكِیْمِ", "By the wise Qur'an.", "حکمت والے قرآن کی قسم۔", 22, 440, 1),
                    AyahEntity(36003, 36, 3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "اِنَّكَ لَمِنَ الْمُرْسَلِیْنَ", "Indeed you are from among the messengers,", "بے شک آپ رسولوں میں سے ہیں۔", 22, 440, 1),
                    AyahEntity(36004, 36, 4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "عَلٰى صِرَاطٍ مُّسْتَقِیْمٍ", "On a straight path.", "سیدھے راستے پر۔", 22, 440, 1),
                    AyahEntity(36005, 36, 5, "تَنزِيلَ الْعَزِيزِ الرَّحِيمِ", "تَنْزِیْلَ الْعَزِیْزِ الرَّحِیْمِ", "[This is] a revelation of the Exalted in Might, the Merciful,", "یہ غالب اور نہایت رحم کرنے والے کی طرف سے نازل کردہ ہے۔", 22, 440, 1),
                    AyahEntity(36006, 36, 6, "لِتُنذِرَ قَوْمًا مَّا أُنذِرَ آبَاؤُهُمْ فَهُمْ غَافِلُونَ", "لِتُنْذِرَ قَوْمًا مَّآ اُنْذِرَ اٰبَآؤُهُمْ فَهُمْ غٰفِلُوْنَ", "That you may warn a people whose forefathers were not warned, so they are unaware.", "تاکہ آپ اس قوم کو ڈرائیں جن کے باپ دادا کو نہیں ڈرایا گیا تھا سو وہ غافل ہیں۔", 22, 440, 1)
                ),
                tajweedFocusUr = "صِرَاطٍ مُّسْتَقِيمٍ میں ادغام مع الغنہ اور اخفاء حقیقی۔",
                makhrajTipUr = "'الْحَكِیْمِ' اور 'الْعَزِیْزِ' میں زائے معجمہ کی سیٹی دار آواز صاف نکالیں۔",
                virtueUr = "سورۃ یٰس قرآن کا دل ہے، جو اسے اللہ کی رضا کے لیے پڑھتا ہے اس کی بخشش ہو جاتی ہے۔"
            )
        )

        // Day 7: Ar-Rahman 1-10 (Gratitude & Qalqalah)
        list.add(
            DailyLesson(
                dayNumber = 7,
                titleUr = "سورۃ الرحمن: شکر اور قلقلہ",
                titleEn = "Surah Ar-Rahman: Blessings & Qalqalah",
                surahNumber = 55,
                surahNameArabic = "الرحمن",
                surahNameUr = "سورۃ الرحمٰن",
                startAyah = 1,
                endAyah = 6,
                targetAyahs = listOf(
                    AyahEntity(55001, 55, 1, "الرَّحْمَٰنُ", "الرَّحْمٰنُ", "The Most Merciful", "نہایت مہربان (اللہ)", 27, 531, 1),
                    AyahEntity(55002, 55, 2, "عَلَّمَ الْقُرْآنَ", "عَلَّمَ الْقُرْاٰنَ", "Taught the Qur'an,", "اسی نے قرآن سکھایا۔", 27, 531, 1),
                    AyahEntity(55003, 55, 3, "خَلَقَ الْإِنسَانَ", "خَلَقَ الْاِنْسَانَ", "Created man,", "اسی نے انسان کو پیدا کیا۔", 27, 531, 1),
                    AyahEntity(55004, 55, 4, "عَلَّمَهُ الْبَيَانَ", "عَلَّمَهُ الْبَیَانَ", "Taught him eloquence.", "اسے بولنا سکھایا۔", 27, 531, 1),
                    AyahEntity(55005, 55, 5, "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ", "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ", "The sun and the moon move by precise calculation,", "سورج اور چاند ایک حساب کے پابند ہیں۔", 27, 531, 1),
                    AyahEntity(55006, 55, 6, "وَالنَّجْمُ وَالشَّجَرُ يَسْجُدَانِ", "وَالنَّجْمُ وَالشَّجَرُ یَسْجُدٰنِ", "And the stars and trees prostrate,", "اور بوٹیاں اور درخت سجدہ کر رہے ہیں۔", 27, 531, 1)
                ),
                tajweedFocusUr = "حروفِ قلقلہ (ق، ط، ب، ج، د) اور مد عارض برائے وقف۔",
                makhrajTipUr = "'خَلَقَ' میں 'خ' اور 'ق' دونوں موٹے حروف ہیں۔ درمیان کا لام باریک رہے گا۔",
                virtueUr = "سورۃ الرحمن قرآن کی دلہن (عروس القرآن) ہے۔"
            )
        )

        // Day 8: Al-Mulk 1-5 (Salvation from Punishment)
        list.add(
            DailyLesson(
                dayNumber = 8,
                titleUr = "سورۃ الملک: عذابِ قبر سے حفاظت",
                titleEn = "Surah Al-Mulk: Deliverance & Reflection",
                surahNumber = 67,
                surahNameArabic = "الملك",
                surahNameUr = "سورۃ الملک",
                startAyah = 1,
                endAyah = 4,
                targetAyahs = listOf(
                    AyahEntity(67001, 67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "تَبٰرَكَ الَّذِیْ بِیَدِهِ الْمُلْكُ ۫ وَهُوَ عَلٰى كُلِّ شَیْءٍ قَدِیْرُ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "بڑی برکت والا ہے وہ جس کے ہاتھ میں بادشاہی ہے اور وہ ہر چیز پر قادر ہے۔", 29, 562, 1),
                    AyahEntity(67002, 67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "الَّذِیْ خَلَقَ الْمَوْتَ وَالْحَیٰوةَ لِیَبْلُوَكُمْ اَیُّكُمْ اَحْسَنُ عَمَلًا ؕ وَهُوَ الْعَزِیْزُ الْغَفُوْرُ", "He who created death and life to test you as to which of you is best in deed...", "جس نے موت اور زندگی کو پیدا کیا تاکہ تمہیں آزمائے کہ تم میں سے عمل میں کون بہتر ہے...", 29, 562, 1),
                    AyahEntity(67003, 67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ", "الَّذِیْ خَلَقَ سَبْعَ سَمٰوٰتٍ طِبَاقًا ؕ مَا تَرٰى فِیْ خَلْقِ الرَّحْمٰنِ مِنْ تَفٰوُتٍ", "He who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency.", "جس نے اوپر تلے سات آسمان بنائے۔ تم رحمن کی پیدائش میں کوئی نقص نہیں دیکھو گے۔", 29, 562, 1),
                    AyahEntity(67004, 67, 4, "فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "فَارْجِعِ الْبَصَرَ هَلْ تَرٰى مِنْ فُطُوْرٍ", "So return your vision to the sky; do you see any breaks?", "پس دوبارہ نگاہ ڈالو، کیا تمہیں کوئی شگاف نظر آتا ہے؟", 29, 562, 1)
                ),
                tajweedFocusUr = "راء کی تفخیم (موٹا پڑھنا) جب راء پر زبر یا پیش ہو، اور قلقلہ در 'سَبْعَ'۔",
                makhrajTipUr = "'الْبَصَرَ' میں صاد کو باریک 'س' نہ پڑھیں، سیٹی اور موٹے پن کا خیال رکھیں۔",
                virtueUr = "سورۃ الملک تیس آیات پر مشتمل سورت ہے جو اپنے پڑھنے والے کی شفاعت کرتی ہے یہاں تک کہ اس کی مغفرت ہو جائے۔"
            )
        )

        // Day 9: Ad-Duha (Complete 1-11)
        list.add(
            DailyLesson(
                dayNumber = 9,
                titleUr = "سورۃ الضحیٰ: نور اور تسلی",
                titleEn = "Surah Ad-Duha: Light and Comfort",
                surahNumber = 93,
                surahNameArabic = "الضحى",
                surahNameUr = "سورۃ الضحیٰ",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(93001, 93, 1, "وَالضُّحَىٰ", "وَالضُّحٰى", "By the morning brightness", "قسم ہے چاشت کے وقت کی،", 30, 596, 1),
                    AyahEntity(93002, 93, 2, "وَاللَّيْلِ إِذَا سَجَىٰ", "وَالَّیْلِ اِذَا سَجٰى", "And by the night when it covers with darkness,", "اور رات کی جب وہ چھا جائے،", 30, 596, 1),
                    AyahEntity(93003, 93, 3, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلٰى", "Your Lord has not taken leave of you, nor has He detested.", "آپ کے رب نے آپ کو نہ چھوڑا اور نہ وہ ناراض ہوا۔", 30, 596, 1),
                    AyahEntity(93004, 93, 4, "وَلَلْآخِرَةُ خَيْرٌ لَّكَ مِنَ الْأُولَىٰ", "وَلَلْاٰخِرَةُ خَیْرٌ لَّكَ مِنَ الْاُوْلٰى", "And the Hereafter is better for you than the first [life].", "اور یقیناً آخرت آپ کے لیے دنیا سے بہت بہتر ہے۔", 30, 596, 1),
                    AyahEntity(93005, 93, 5, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "وَلَسَوْفَ یُعْطِیْكَ رَبُّكَ فَتَرْضٰى", "And your Lord is going to give you, and you will be satisfied.", "اور عنقریب آپ کا رب آپ کو اتنا دے گا کہ آپ خوش ہو جائیں گے۔", 30, 596, 1)
                ),
                tajweedFocusUr = "حرف 'ض' کا درست مخرج (زبان کی کروٹ داڑھوں سے لگا کر) اور الف مقصورہ۔",
                makhrajTipUr = "'وَالضُّحٰى' میں ضاد کو دال یا زاء نہیں بنانا، زبان کی کروٹ سے نرمی و پُر آواز سے نکالیں۔",
                virtueUr = "غم اور اداسی کے وقت سورۃ الضحیٰ دل کو تسکین اور امید بخشتی ہے۔"
            )
        )

        // Day 10: Ash-Sharh (Complete 1-8)
        list.add(
            DailyLesson(
                dayNumber = 10,
                titleUr = "سورۃ الشرح: کشادگی اور آسانی",
                titleEn = "Surah Ash-Sharh: Relief & Ease",
                surahNumber = 94,
                surahNameArabic = "الشرح",
                surahNameUr = "سورۃ الشرح",
                startAyah = 1,
                endAyah = 6,
                targetAyahs = listOf(
                    AyahEntity(94001, 94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "اَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Did We not expand for you your breast?", "کیا ہم نے آپ کا سینہ نہیں کھول دیا؟", 30, 596, 1),
                    AyahEntity(94002, 94, 2, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "وَوَضَعْنَا عَنْكَ وِزْرَكَ", "And We removed from you your burden", "اور ہم نے آپ سے آپ کا بوجھ اتار دیا،", 30, 596, 1),
                    AyahEntity(94003, 94, 3, "الَّذِي أَنقَضَ ظَهْرَكَ", "الَّذِیْۤ اَنْقَضَ ظَهْرَكَ", "Which had weighed upon your back", "جس نے آپ کی پیٹھ توڑ رکھی تھی،", 30, 596, 1),
                    AyahEntity(94004, 94, 4, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "وَرَفَعْنَا لَكَ ذِكْرَكَ", "And raised high for you your repute.", "اور ہم نے آپ کے لیے آپ کا ذکر بلند فرما دیا۔", 30, 596, 1),
                    AyahEntity(94005, 94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "فَاِنَّ مَعَ الْعُسْرِ یُسْرًا", "For indeed, with hardship [will be] ease.", "پس بے شک تنگی کے ساتھ آسانی ہے۔", 30, 596, 1),
                    AyahEntity(94006, 94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "اِنَّ مَعَ الْعُسْرِ یُسْرًا", "Indeed, with hardship [will be] ease.", "بے شک تنگی کے ساتھ آسانی ہے۔", 30, 596, 1)
                ),
                tajweedFocusUr = "حرف شین کی تفشی (منہ میں ہوا پھیلنا) اور نون مشدد کا غنہ۔",
                makhrajTipUr = "'صَدْرَكَ' میں صاد موٹا اور دال باریک ساکنہ پر قلقلہ ہوگا۔",
                virtueUr = "ہر مشکل اور پریشانی کے بعد آسانی کا قرآنی وعدہ ہے۔"
            )
        )

        // Day 11: Surah At-Tin
        list.add(
            DailyLesson(
                dayNumber = 11,
                titleUr = "سورۃ التین: احسن تقویم اور مخارج",
                titleEn = "Surah At-Tin: Best Creation",
                surahNumber = 95,
                surahNameArabic = "التين",
                surahNameUr = "سورۃ التین",
                startAyah = 1,
                endAyah = 4,
                targetAyahs = listOf(
                    AyahEntity(95001, 95, 1, "وَالتِّينِ وَالزَّيْتُونِ", "وَالتِّیْنِ وَالزَّیْتُوْنِ", "By the fig and the olive", "قسم ہے انجیر کی اور زیتون کی،", 30, 597, 1),
                    AyahEntity(95002, 95, 2, "وَطُورِ سِينِينَ", "وَطُوْرِ سِیْنِیْنَ", "And [by] Mount Sinai", "اور طورِ سینین کی،", 30, 597, 1),
                    AyahEntity(95003, 95, 3, "وَهَٰذَا الْبَلَدِ الْأَمِينِ", "وَهٰذَا الْبَلَدِ الْاَمِیْنِ", "And [by] this secure city [Makkah],", "اور اس امن والے شہر (مکہ) کی،", 30, 597, 1),
                    AyahEntity(95004, 95, 4, "لَقَدْ خَلَقْنَا الْإِنسَانَ فِي أَحْسَنِ تَقْوِيمٍ", "لَقَدْ خَلَقْنَا الْاِنْسَانَ فِیْۤ اَحْسَنِ تَقْوِیْمٍ", "We have certainly created man in the best of stature;", "بے شک ہم نے انسان کو بہترین ساخت پر پیدا کیا۔", 30, 597, 1)
                ),
                tajweedFocusUr = "'ت' اور 'ط' میں فرق: ت باریک اور ط موٹا ہے۔",
                makhrajTipUr = "'وَطُوْرِ' میں طاء کو پر پڑھیں، تاء کی طرح باریک نہ ہو۔",
                virtueUr = "انسان کی عظمت اور شکر گزاری کی یاد دہانی۔"
            )
        )

        // Day 12: Surah Al-Alaq 1-5 (First Revelation)
        list.add(
            DailyLesson(
                dayNumber = 12,
                titleUr = "سورۃ العلق: پہلی وحی اور قلقلہ",
                titleEn = "Surah Al-Alaq: The First Revelation",
                surahNumber = 96,
                surahNameArabic = "العلق",
                surahNameUr = "سورۃ العلق",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(96001, 96, 1, "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ", "اِقْرَاْ بِاسْمِ رَبِّكَ الَّذِیْ خَلَقَ", "Recite in the name of your Lord who created -", "پڑھیے اپنے رب کے نام کے ساتھ جس نے پیدا فرمایا،", 30, 597, 1),
                    AyahEntity(96002, 96, 2, "خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ", "خَلَقَ الْاِنْسَانَ مِنْ عَلَقٍ", "Created man from a clinging substance.", "جس نے انسان کو جمے ہوئے خون سے پیدا کیا۔", 30, 597, 1),
                    AyahEntity(96003, 96, 3, "اقْرَأْ وَرَبُّكَ الْأَكْرَمُ", "اِقْرَاْ وَرَبُّكَ الْاَكْرَمُ", "Recite, and your Lord is the most Generous -", "پڑھیے اور آپ کا رب بڑا ہی کریم ہے،", 30, 597, 1),
                    AyahEntity(96004, 96, 4, "الَّذِي عَلَّمَ بِالْقَلَمِ", "الَّذِیْ عَلَّمَ بِالْقَلَمِ", "Who taught by the pen -", "جس نے قلم کے ذریعے علم سکھایا،", 30, 597, 1),
                    AyahEntity(96005, 96, 5, "عَلَّمَ الْإِنسَانَ مَا لَمْ يَعْلَمْ", "عَلَّمَ الْاِنْسَانَ مَا لَمْ یَعْلَمْ", "Taught man that which he knew not.", "انسان کو وہ سکھایا جو وہ نہیں جانتا تھا۔", 30, 597, 1)
                ),
                tajweedFocusUr = "قلقلہ اکبر جب قاف پر وقف ہو (خَلَقْ، عَلَقْ)۔",
                makhrajTipUr = "'اقْرَأْ' میں ہمزہ ساکنہ کو جھٹکے سے صاف ادا کریں، الف کی طرح لمبا نہ کریں۔",
                virtueUr = "غارِ حرا میں نازل ہونے والی قرآن پاک کی سب سے پہلی پانچ آیات مبارکہ۔"
            )
        )

        // Day 13: Surah Al-Qadr (Complete 1-5)
        list.add(
            DailyLesson(
                dayNumber = 13,
                titleUr = "سورۃ القدر: لیلۃ القدر اور غنہ",
                titleEn = "Surah Al-Qadr: Night of Decree",
                surahNumber = 97,
                surahNameArabic = "القدر",
                surahNameUr = "سورۃ القدر",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(97001, 97, 1, "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "اِنَّآ اَنْزَلْنٰهُ فِیْ لَیْلَةِ الْقَدْرِ", "Indeed, We sent the Qur'an down during the Night of Decree.", "بے شک ہم نے اس کو شبِ قدر میں نازل فرمایا۔", 30, 598, 1),
                    AyahEntity(97002, 97, 2, "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ", "وَمَآ اَدْرٰىكَ مَا لَیْلَةُ الْقَدْرِ", "And what can make you know what is the Night of Decree?", "اور آپ کو کیا معلوم کہ شبِ قدر کیا ہے؟", 30, 598, 1),
                    AyahEntity(97003, 97, 3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", "لَیْلَةُ الْقَدْرِ خَیْرٌ مِّنْ اَلْفِ شَهْرٍ", "The Night of Decree is better than a thousand months.", "شبِ قدر ہزار مہینوں سے بہتر ہے۔", 30, 598, 1),
                    AyahEntity(97004, 97, 4, "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ", "تَنَزَّلُ الْمَلٰٓىِٕكَةُ وَالرُّوْحُ فِیْهَا بِاِذْنِ رَبِّهِمْ ۚ مِنْ كُلِّ اَمْرٍ", "The angels and the Spirit descend therein by permission of their Lord for every matter.", "اس میں فرشتے اور روح القدس اپنے رب کے حکم سے ہر کام کے لیے اترتے ہیں،", 30, 598, 1),
                    AyahEntity(97005, 97, 5, "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ", "سَلٰمٌ ۟ هِیَ حَتّٰى مَطْلَعِ الْفَجْرِ", "Peace it is until the emergence of dawn.", "وہ رات طلوعِ فجر تک سراسر سلامتی ہے۔", 30, 598, 1)
                ),
                tajweedFocusUr = "قلقلہ در 'الْقَدْرِ' اور راء کا وقف پر پر یا باریک ہونا۔",
                makhrajTipUr = "'الْفَجْرِ' میں جیم ساکنہ پر قلقلہ اوسط خوب واضح ہو تاکہ آواز میں گونج پیدا ہو۔",
                virtueUr = "ہزار مہینوں (۸۳ سال ۴ ماہ) کی عبادت سے افضل رات کی فضیلت۔"
            )
        )

        // Day 14: Surah Az-Zalzalah (Complete)
        list.add(
            DailyLesson(
                dayNumber = 14,
                titleUr = "سورۃ الزلزلۃ: قیامت کا منظر اور ادغام",
                titleEn = "Surah Az-Zalzalah: The Earthquake",
                surahNumber = 99,
                surahNameArabic = "الزلزلة",
                surahNameUr = "سورۃ الزلزلہ",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(99001, 99, 1, "إِذَا زُلْزِلَتِ الْأَرْضُ زِلْزَالَهَا", "اِذَا زُلْزِلَتِ الْاَرْضُ زِلْزَالَهَا", "When the earth is shaken with its [final] earthquake", "جب زمین اپنی پوری شدت کے ساتھ ہلا دی جائے گی،", 30, 599, 1),
                    AyahEntity(99002, 99, 2, "وَأَخْرَجَتِ الْأَرْضُ أَثْقَالَهَا", "وَاَخْرَجَتِ الْاَرْضُ اَثْقَالَهَا", "And the earth discharges its burdens", "اور زمین اپنے بوجھ باہر نکال پھینکے گی،", 30, 599, 1),
                    AyahEntity(99003, 99, 3, "وَقَالَ الْإِنسَانُ مَا لَهَا", "وَقَالَ الْاِنْسَانُ مَا لَهَا", "And man says, 'What is wrong with it?'", "اور انسان کہے گا کہ اسے کیا ہو گیا ہے؟", 30, 599, 1),
                    AyahEntity(99004, 99, 4, "يَوْمَئِذٍ تُحَدِّثُ أَخْبَارَهَا", "یَوْمَىِٕذٍ تُحَدِّثُ اَخْبَارَهَا", "That Day, it will report its news", "اس دن وہ اپنی تمام خبریں بیان کرے گی،", 30, 599, 1),
                    AyahEntity(99005, 99, 5, "بِأَنَّ رَبَّكَ أَوْحَىٰ لَهَا", "بِاَنَّ رَبَّكَ اَوْحٰى لَهَا", "Because your Lord has commanded it.", "کیونکہ آپ کے رب نے اسے حکم دیا ہوگا۔", 30, 599, 1)
                ),
                tajweedFocusUr = "زائے معجمہ کی سیٹی اور حرف 'ذ' میں نرمی (سیٹی کے بغیر)۔",
                makhrajTipUr = "'اِذَا' میں ذال زبان کی نوک اوپر کے دانتوں کے کنارے سے لگنے سے ادا ہوتی ہے، 'ز' نہ بنے۔",
                virtueUr = "سورۃ الزلزلۃ نصف قرآن (آدھے قرآن) کے برابر ثواب رکھتی ہے۔"
            )
        )

        // Day 15: Surah Al-Adiyat
        list.add(
            DailyLesson(
                dayNumber = 15,
                titleUr = "سورۃ العادیات: قلقلہ اور رفتار",
                titleEn = "Surah Al-Adiyat: The Chargers",
                surahNumber = 100,
                surahNameArabic = "العاديات",
                surahNameUr = "سورۃ العادیات",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(100001, 100, 1, "وَالْعَادِيَاتِ ضَبْحًا", "وَالْعٰدِیٰتِ ضَبْحًا", "By the racers, panting,", "قسم ہے ہانپتے ہوئے دوڑنے والے گھوڑوں کی،", 30, 599, 1),
                    AyahEntity(100002, 100, 2, "فَالْمُورِيَاتِ قَدْحًا", "فَالْمُوْرِیٰتِ قَدْحًا", "And the producers of sparks [when] striking", "پھر ٹاپوں سے چنگاریاں نکالنے والوں کی،", 30, 599, 1),
                    AyahEntity(100003, 100, 3, "فَالْمُغِيرَاتِ صُبْحًا", "فَالْمُغِیْرٰتِ صُبْحًا", "And the chargers at dawn,", "پھر صبح کے وقت دھاوا بولنے والوں کی،", 30, 599, 1),
                    AyahEntity(100004, 100, 4, "فَأَثَرْنَ بِهِ نَقْعًا", "فَاَثَرْنَ بِهٖ نَقْعًا", "Stirring up thereby clouds of dust,", "پھر اس وقت گرد و غبار اڑانے والوں کی،", 30, 599, 1),
                    AyahEntity(100005, 100, 5, "فَوَسَطْنَ بِهِ جَمْعًا", "فَوَسَطْنَ بِهٖ جَمْعًا", "Penetrating thereby into the center collectively,", "پھر دشمن کی فوج میں جا گھسنے والوں کی،", 30, 599, 1)
                ),
                tajweedFocusUr = "قلقلہ در حروف 'ب' (ضَبْحًا)، 'د' (قَدْحًا)، 'ق' (نَقْعًا)، 'ط' (فَوَسَطْنَ)۔",
                makhrajTipUr = "'ضَبْحًا' میں باء ساکنہ پر جھٹکا نہ لگے بلکہ آواز کا مخرج سے پلٹنا صاف محسوس ہو۔",
                virtueUr = "قلقلہ کی تمام اقسام کی بہترین مشق کے لیے منتخب سورت۔"
            )
        )

        // Day 16: Surah Al-Qari'ah
        list.add(
            DailyLesson(
                dayNumber = 16,
                titleUr = "سورۃ القارعۃ: کھڑکھڑانے والی آفت",
                titleEn = "Surah Al-Qari'ah: The Striking Calamity",
                surahNumber = 101,
                surahNameArabic = "القارعة",
                surahNameUr = "سورۃ القارعہ",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(101001, 101, 1, "الْقَارِعَةُ", "اَلْقَارِعَةُ", "The Striking Calamity -", "کھڑکھڑانے والی!", 30, 600, 1),
                    AyahEntity(101002, 101, 2, "مَا الْقَارِعَةُ", "مَا الْقَارِعَةُ", "What is the Striking Calamity?", "کیا ہے وہ کھڑکھڑانے والی؟", 30, 600, 1),
                    AyahEntity(101003, 101, 3, "وَمَا أَدْرَاكَ مَا الْقَارِعَةُ", "وَمَآ اَدْرٰىكَ مَا الْقَارِعَةُ", "And what can make you know what is the Striking Calamity?", "اور آپ کو کیا معلوم کہ وہ کھڑکھڑانے والی کیا ہے؟", 30, 600, 1),
                    AyahEntity(101004, 101, 4, "يَوْمَ يَكُونُ النَّاسُ كَالْفَرَاشِ الْمَبْثُوثِ", "یَوْمَ یَكُوْنُ النَّاسُ كَالْفَرَاشِ الْمَبْثُوْثِ", "It is the Day when people will be like moths, dispersed,", "جس دن لوگ بکھرے ہوئے پروانوں کی طرح ہوں گے،", 30, 600, 1),
                    AyahEntity(101005, 101, 5, "وَتَكُونُ الْجِبَالُ كَالْعِهْنِ الْمَنفُوشِ", "وَتَكُوْنُ الْجِبَالُ كَالْعِهْنِ الْمَنْفُوْشِ", "And the mountains will be like wool, fluffed up.", "اور پہاڑ دھنی ہوئی رنگین اون کی طرح ہو جائیں گے۔", 30, 600, 1)
                ),
                tajweedFocusUr = "تائے مربوطہ (ة) پر وقف کی صورت میں 'ہ' کی صاف ادائیگی۔",
                makhrajTipUr = "'الْقَارِعَةُ' پر جب وقف کریں تو 'الْقَارِعَہْ' پڑھیں، ہمزہ یا تاء نہ بنے۔",
                virtueUr = "نامہ اعمال کے پلڑوں کے وزنی ہونے اور جہنم سے نجات کا تذکرہ۔"
            )
        )

        // Day 17: Surah At-Takathur
        list.add(
            DailyLesson(
                dayNumber = 17,
                titleUr = "سورۃ التکاثر: کثرت کی ہوس سے بیداری",
                titleEn = "Surah At-Takathur: Greed for Wealth",
                surahNumber = 102,
                surahNameArabic = "التكاثر",
                surahNameUr = "سورۃ التکاثر",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(102001, 102, 1, "أَلْهَاكُمُ التَّكَاثُرُ", "اَلْهٰىكُمُ التَّكَاثُرُ", "Competition in [worldly] increase diverts you", "زیادہ سے زیادہ حاصل کرنے کی ہوس نے تمہیں غافل کر دیا،", 30, 600, 1),
                    AyahEntity(102002, 102, 2, "حَتَّىٰ زُرْتُمُ الْمَقَابِرَ", "حَتّٰى زُرْتُمُ الْمَقَابِرَ", "Until you visit the graveyards.", "یہاں تک کہ تم نے قبریں جا دیکھیں۔", 30, 600, 1),
                    AyahEntity(102003, 102, 3, "كَلَّا سَوْفَ تَعْلَمُونَ", "كَلَّا سَوْفَ تَعْلَمُوْنَ", "No! You are going to know.", "ہرگز نہیں! عنقریب تم جان لو گے۔", 30, 600, 1),
                    AyahEntity(102004, 102, 4, "ثُمَّ كَلَّا سَوْفَ تَعْلَمُونَ", "ثُمَّ كَلَّا سَوْفَ تَعْلَمُوْنَ", "Then no! You are going to know.", "پھر ہرگز نہیں! عنقریب تم جان لو گے۔", 30, 600, 1),
                    AyahEntity(102005, 102, 5, "كَلَّا لَوْ تَعْلَمُونَ عِلْمَ الْيَقِينِ", "كَلَّا لَوْ تَعْلَمُوْنَ عِلْمَ الْیَقِیْنِ", "No! If you only knew with knowledge of certainty...", "ہرگز نہیں! اگر تم یقینی علم رکھتے تو ایسا نہ کرتے۔", 30, 600, 1)
                ),
                tajweedFocusUr = "حرف 'ث' کا مخرج (زبان کی نوک اوپر کے دانتوں سے) اور میم مشدد۔",
                makhrajTipUr = "'ثُمَّ' میں ثاء کو سین نہیں بنانا، زبان کی نوک کو اوپر کے دانتوں کے کناروں سے نرمی سے چھوئیں اور میم پر غنہ کریں۔",
                virtueUr = "حضور ﷺ نے فرمایا: کیا تم میں سے کوئی ہر رات ایک ہزار آیات پڑھنے کی طاقت نہیں رکھتا؟ صحابہ نے عرض کیا: یہ کس کے بس میں ہے؟ فرمایا: کیا کوئی سورۃ التکاثر نہیں پڑھ سکتا؟"
            )
        )

        // Day 18: Surah Al-Asr (Complete 1-3)
        list.add(
            DailyLesson(
                dayNumber = 18,
                titleUr = "سورۃ العصر: وقت اور کامیابی کا نسخہ",
                titleEn = "Surah Al-Asr: Time & Real Success",
                surahNumber = 103,
                surahNameArabic = "العصر",
                surahNameUr = "سورۃ العصر",
                startAyah = 1,
                endAyah = 3,
                targetAyahs = listOf(
                    AyahEntity(103001, 103, 1, "وَالْعَصْرِ", "وَالْعَصْرِ", "By time,", "زمانہ کی قسم،", 30, 601, 1),
                    AyahEntity(103002, 103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "اِنَّ الْاِنْسَانَ لَفِیْ خُسْرٍ", "Indeed, mankind is in loss,", "بے شک انسان سراسر خسارے میں ہے،", 30, 601, 1),
                    AyahEntity(103003, 103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "اِلَّا الَّذِیْنَ اٰمَنُوْا وَعَمِلُوا الصّٰلِحٰتِ وَتَوَاصَوْا بِالْحَقِّ ۙ وَتَوَاصَوْا بِالصَّبْرِ", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", "سوائے ان لوگوں کے جو ایمان لائے اور نیک عمل کیے اور ایک دوسرے کو حق اور صبر کی تلقین کی۔", 30, 601, 1)
                ),
                tajweedFocusUr = "صاد اور سین میں واضح فرق، اور راء ساکنہ پر وقف کی ادائیگی۔",
                makhrajTipUr = "'وَالْعَصْرِ' میں عین حلق کے درمیان سے اور صاد کو پر ادا کریں، راء پر وقف میں زبان نہ پھسلے۔",
                virtueUr = "امام شافعیؒ نے فرمایا: اگر اللہ صرف سورۃ العصر ہی نازل فرماتا تو یہ انسانوں کی رہنمائی کے لیے کافی تھی۔"
            )
        )

        // Day 19: Surah Al-Humazah
        list.add(
            DailyLesson(
                dayNumber = 19,
                titleUr = "سورۃ الہمزۃ: عیب جوئی سے پرہیز",
                titleEn = "Surah Al-Humazah: The Backbiter",
                surahNumber = 104,
                surahNameArabic = "الهمزة",
                surahNameUr = "سورۃ الہمزہ",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(104001, 104, 1, "وَيْلٌ لِّكُلِّ هُمَزَةٍ لُّمَزَةٍ", "وَیْلٌ لِّكُلِّ هُمَزَةٍ لُّمَزَةٍ", "Woe to every scorner and mocker", "تباہی ہے ہر طعنہ زنی کرنے والے اور عیب جوئی کرنے والے کے لیے،", 30, 601, 1),
                    AyahEntity(104002, 104, 2, "الَّذِي جَمَعَ مَالًا وَعَدَّدَهُ", "الَّذِیْ جَمَعَ مَالًا وَّعَدَّدَهٗ", "Who collects wealth and [continuously] counts it.", "جس نے مال جمع کیا اور اسے گن گن کر رکھا۔", 30, 601, 1),
                    AyahEntity(104003, 104, 3, "يَحْسَبُ أَنَّ مَالَهُ أَخْلَدَهُ", "یَحْسَبُ اَنَّ مَالَهٗۤ اَخْلَدَهٗ", "He thinks that his wealth will make him immortal.", "وہ سمجھتا ہے کہ اس کا مال اسے ہمیشہ زندہ رکھے گا۔", 30, 601, 1),
                    AyahEntity(104004, 104, 4, "كَلَّا ۖ لَيُنبَذَنَّ فِي الْحُطَمَةِ", "كَلَّا ۖ لَیُنْۢبَذَنَّ فِی الْحُطَمَةِ", "No! He will surely be thrown into the Crusher.", "ہرگز نہیں! وہ ضرور چورا چورا کرنے والی آگ (حطمہ) میں جھونکا جائے گا۔", 30, 601, 1),
                    AyahEntity(104005, 104, 5, "وَمَا أَدْرَاكَ مَا الْحُطَمَةُ", "وَمَآ اَدْرٰىكَ مَا الْحُطَمَةُ", "And what can make you know what is the Crusher?", "اور آپ کو کیا معلوم کہ وہ حطمہ کیا ہے؟", 30, 601, 1)
                ),
                tajweedFocusUr = "اقلاب: نون ساکن کے بعد 'ب' آنے پر نون کو میم سے بدل کر غنہ کرنا (لَیُنْۢبَذَنَّ)۔",
                makhrajTipUr = "'لَیُنْۢبَذَنَّ' میں دونوں ہونٹوں کو نرمی سے ملا کر ناک سے غنہ کی آواز نکالیں۔",
                virtueUr = "اخلاقِ حسنہ اور دوسروں کی عزت کی حفاظت کا اہم درس۔"
            )
        )

        // Day 20: Surah Al-Fil
        list.add(
            DailyLesson(
                dayNumber = 20,
                titleUr = "سورۃ الفیل: اصحابِ فیل اور قدرتِ الٰہی",
                titleEn = "Surah Al-Fil: The Elephant",
                surahNumber = 105,
                surahNameArabic = "الفيل",
                surahNameUr = "سورۃ الفیل",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(105001, 105, 1, "أَلَمْ تَرَ كَيْفَ فَعَلَ رَبُّكَ بِأَصْحَابِ الْفِيلِ", "اَلَمْ تَرَ كَیْفَ فَعَلَ رَبُّكَ بِاَصْحٰبِ الْفِیْلِ", "Have you not considered how your Lord dealt with the companions of the elephant?", "کیا آپ نے نہیں دیکھا کہ آپ کے رب نے ہاتھی والوں کے ساتھ کیا کیا؟", 30, 601, 1),
                    AyahEntity(105002, 105, 2, "أَلَمْ يَجْعَلْ كَيْدَهُمْ فِي تَضْلِيلٍ", "اَلَمْ یَجْعَلْ كَیْدَهُمْ فِیْ تَضْلِیْلٍ", "Did He not make their plan into misguidance?", "کیا اس نے ان کی تدبیر کو اکارت نہیں کر دیا؟", 30, 601, 1),
                    AyahEntity(105003, 105, 3, "وَأَرْسَلَ عَلَيْهِمْ طَيْرًا أَبَابِيلَ", "وَاَرْسَلَ عَلَیْهِمْ طَیْرًا اَبَابِیْلَ", "And He sent against them birds in flocks,", "اور ان پر غول در غول پرندے بھیجے،", 30, 601, 1),
                    AyahEntity(105004, 105, 4, "تَرْمِيهِم بِحِجَارَةٍ مِّن سِجِّيلٍ", "تَرْمِیْهِمْ بِحِجَارَةٍ مِّنْ سِجِّیْلٍ", "Striking them with stones of hard clay,", "جو ان پر پکی مٹی کے پتھر پھینک رہے تھے،", 30, 601, 1),
                    AyahEntity(105005, 105, 5, "فَجَعَلَهُمْ كَعَصْفٍ مَّأْكُولٍ", "فَجَعَلَهُمْ كَعَصْفٍ مَّاْكُوْلٍ", "And He made them like eaten straw.", "پھر ان کو کھائے ہوئے بھوسے کی طرح بنا دیا۔", 30, 601, 1)
                ),
                tajweedFocusUr = "میم ساکن کے احکام: اخفاء شفوی (تَرْمِیْهِمْ بِحِجَارَةٍ) اور اظہار شفوی (اَلَمْ یَجْعَلْ)۔",
                makhrajTipUr = "'تَرْمِیْهِمْ بِحِجَارَةٍ' میں میم کو باء سے ملاتے ہوئے ہونٹوں کو نرمی سے بند کر کے غنہ کریں۔",
                virtueUr = "بیت اللہ شریف کی حفاظت کا تاریخی معجزہ۔"
            )
        )

        // Day 21: Surah Quraysh
        list.add(
            DailyLesson(
                dayNumber = 21,
                titleUr = "سورۃ قریش: امن و سلامتی اور حروفِ لین",
                titleEn = "Surah Quraysh: Peace & Sustenance",
                surahNumber = 106,
                surahNameArabic = "قريش",
                surahNameUr = "سورۃ قریش",
                startAyah = 1,
                endAyah = 4,
                targetAyahs = listOf(
                    AyahEntity(106001, 106, 1, "لِإِيلَافِ قُرَيْشٍ", "لِاِیْلٰفِ قُرَیْشٍ", "For the accustomed security of the Quraysh -", "قریش کی مانوسیت کے سبب،", 30, 602, 1),
                    AyahEntity(106002, 106, 2, "إِيلَافِهِمْ رِحْلَةَ الشِّتَاءِ وَالصَّيْفِ", "اٖلٰفِهِمْ رِحْلَةَ الشِّتَآءِ وَالصَّیْفِ", "Their accustomed security [in] the caravan of winter and summer -", "سردی اور گرمی کے سفروں سے ان کے مانوس ہونے کی وجہ سے،", 30, 602, 1),
                    AyahEntity(106003, 106, 3, "فَلْيَعْبُدُوا رَبَّ هَٰذَا الْبَيْتِ", "فَلْیَعْبُدُوْا رَبَّ هٰذَا الْبَیْتِ", "Let them worship the Lord of this House,", "انہیں چاہیے کہ وہ اس گھر (کعبہ) کے رب کی عبادت کریں،", 30, 602, 1),
                    AyahEntity(106004, 106, 4, "الَّذِي أَطْعَمَهُم مِّن جُوعٍ وَآمَنَهُم مِّنْ خَوْفٍ", "الَّذِیْۤ اَطْعَمَهُمْ مِّنْ جُوْعٍ ۙ وَّاٰمَنَهُمْ مِّنْ خَوْفٍ", "Who has fed them, [saving them] from hunger and made them safe, [saving them] from fear.", "جس نے انہیں بھوک میں کھانا کھلایا اور خوف سے امن عطا فرمایا۔", 30, 602, 1)
                ),
                tajweedFocusUr = "حروفِ لین: واؤ لین اور یاء لین جب ان سے پہلے زبر ہو اور وقف کی حالت میں مد لین۔",
                makhrajTipUr = "'الصَّیْفِ' اور 'الْبَیْتِ' میں یاء لین کو جھٹکے بغیر نرمی سے ادا کریں۔",
                virtueUr = "رزق میں برکت اور ہر قسم کے خوف سے حفاظت کا مجرب عمل۔"
            )
        )

        // Day 22: Surah Al-Ma'un
        list.add(
            DailyLesson(
                dayNumber = 22,
                titleUr = "سورۃ الماعون: یتیموں کی مدد اور خلوص",
                titleEn = "Surah Al-Ma'un: Small Kindnesses",
                surahNumber = 107,
                surahNameArabic = "الماعون",
                surahNameUr = "سورۃ الماعون",
                startAyah = 1,
                endAyah = 4,
                targetAyahs = listOf(
                    AyahEntity(107001, 107, 1, "أَرَأَيْتَ الَّذِي يُكَذِّبُ بِالدِّينِ", "اَرَءَیْتَ الَّذِیْ یُكَذِّبُ بِالدِّیْنِ", "Have you seen the one who denies the Recompense?", "کیا آپ نے اسے دیکھا جو جزا و سزا کو جھٹلاتا ہے؟", 30, 602, 1),
                    AyahEntity(107002, 107, 2, "فَذَٰلِكَ الَّذِي يَدُعُّ الْيَتِيمَ", "فَذٰلِكَ الَّذِیْ یَدُعُّ الْیَتِیْمَ", "For that is the one who drives away the orphan", "یہی وہ ہے جو یتیم کو دھکے دیتا ہے،", 30, 602, 1),
                    AyahEntity(107003, 107, 3, "وَلَا يَحُضُّ عَلَىٰ طَعَامِ الْمِسْكِينِ", "وَلَا یَحُضُّ عَلٰى طَعَامِ الْمِسْكِیْنِ", "And does not encourage the feeding of the poor.", "اور مسکین کو کھانا کھلانے کی ترغیب نہیں دیتا۔", 30, 602, 1),
                    AyahEntity(107004, 107, 4, "فَوَيْلٌ لِّلْمُصَلِّينَ", "فَوَیْلٌ لِّلْمُصَلِّیْنَ", "So woe to those who pray", "پس ہلاکت ہے ان نمازیوں کے لیے،", 30, 602, 1)
                ),
                tajweedFocusUr = "حرف 'ذ' اور 'ض' کا درست مخرج اور ادغام بلا غنہ۔",
                makhrajTipUr = "'یَدُعُّ' میں عین مشدد کو حلق کے درمیان سے دبائو کے ساتھ واضح کریں۔",
                virtueUr = "نماز میں ریاکاری سے بچنے اور مخلوق خدا سے ہمدردی کا درس۔"
            )
        )

        // Day 23: Surah Al-Kawthar (Complete 1-3)
        list.add(
            DailyLesson(
                dayNumber = 23,
                titleUr = "سورۃ الکوثر: حوضِ کوثر اور راء کی تفخیم",
                titleEn = "Surah Al-Kawthar: Abundance",
                surahNumber = 108,
                surahNameArabic = "الكوثر",
                surahNameUr = "سورۃ الکوثر",
                startAyah = 1,
                endAyah = 3,
                targetAyahs = listOf(
                    AyahEntity(108001, 108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "اِنَّآ اَعْطَیْنٰكَ الْكَوْثَرَ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "بے شک ہم نے آپ کو (حوضِ) کوثر عطا فرمایا۔", 30, 602, 1),
                    AyahEntity(108002, 108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "So pray to your Lord and sacrifice [to Him alone].", "پس آپ اپنے رب کے لیے نماز پڑھیے اور قربانی کیجیے۔", 30, 602, 1),
                    AyahEntity(108003, 108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "اِنَّ شَانِئَكَ هُوَ الْاَبْتَرُ", "Indeed, your enemy is the one cut off.", "یقیناً آپ کا دشمن ہی بے نام و نشان رہے گا۔", 30, 602, 1)
                ),
                tajweedFocusUr = "نون مشدد کا غنہ، مدِ منفصل اور قلقلہ در 'الْاَبْتَرُ'۔",
                makhrajTipUr = "'وَانْحَرْ' میں نون ساکن کے بعد 'ح' پر اظہار حلق ہوگا (غنہ بالکل نہیں ہوگا)۔",
                virtueUr = "قرآن کریم کی سب سے چھوٹی لیکن بے پناہ فضیلت و برکت والی سورت۔"
            )
        )

        // Day 24: Surah Al-Kafirun (Complete 1-6)
        list.add(
            DailyLesson(
                dayNumber = 24,
                titleUr = "سورۃ الکافرون: شرک سے برأت",
                titleEn = "Surah Al-Kafirun: Purity of Faith",
                surahNumber = 109,
                surahNameArabic = "الكافرون",
                surahNameUr = "سورۃ الکافرون",
                startAyah = 1,
                endAyah = 6,
                targetAyahs = listOf(
                    AyahEntity(109001, 109, 1, "قُلْ يَا أَيُّهَا الْكَافِرُونَ", "قُلْ یٰۤاَیُّهَا الْكٰفِرُوْنَ", "Say, 'O disbelievers,'", "کہہ دیجیے کہ اے کافرو!", 30, 603, 1),
                    AyahEntity(109002, 109, 2, "لَا أَعْبُدُ مَا تَعْبُدُونَ", "لَاۤ اَعْبُدُ مَا تَعْبُدُوْنَ", "I do not worship what you worship.", "میں ان کی عبادت نہیں کرتا جن کی تم عبادت کرتے ہو،", 30, 603, 1),
                    AyahEntity(109003, 109, 3, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "وَلَاۤ اَنْتُمْ عٰبِدُوْنَ مَآ اَعْبُدُ", "Nor are you worshippers of what I worship.", "اور نہ تم اس کی عبادت کرنے والے ہو جس کی میں عبادت کرتا ہوں،", 30, 603, 1),
                    AyahEntity(109004, 109, 4, "وَلَا أَنَا عَابِدٌ مَّا عَبَدتُّمْ", "وَلَاۤ اَنَا عَابِدٌ مَّا عَبَدْتُّمْ", "Nor will I be a worshipper of what you worship.", "اور نہ میں ان کی عبادت کروں گا جن کی تم نے عبادت کی ہے،", 30, 603, 1),
                    AyahEntity(109005, 109, 5, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "وَلَاۤ اَنْتُمْ عٰبِدُوْنَ مَآ اَعْبُدُ", "Nor will you be worshippers of what I worship.", "اور نہ تم اس کی عبادت کرو گے جس کی میں عبادت کرتا ہوں۔", 30, 603, 1),
                    AyahEntity(109006, 109, 6, "لَكُمْ دِينُكُمْ وَلِيَ دِينِ", "لَكُمْ دِیْنُكُمْ وَلِیَ دِیْنِ", "For you is your religion, and for me is my religion.", "تمہارے لیے تمہارا دین ہے اور میرے لیے میرا دین۔", 30, 603, 1)
                ),
                tajweedFocusUr = "مدِ منفصل (۳ سے ۴ حرکات) اور ادغام متجانسین (عَبَدْتُّمْ: دال کا تاء میں ادغام)۔",
                makhrajTipUr = "'عَبَدْتُّمْ' میں دال کو تاء میں مدغم کریں یعنی دال نہیں پڑھی جائے گی بلکہ تاء مشدد ہو جائے گی۔",
                virtueUr = "سورۃ الکافرون چوتھائی قرآن (ربع قرآن) کے برابر ثواب رکھتی ہے اور شرک سے برأت کا اعلان ہے۔"
            )
        )

        // Day 25: Surah An-Nasr (Complete 1-3)
        list.add(
            DailyLesson(
                dayNumber = 25,
                titleUr = "سورۃ النصر: فتح اور تسبیح و استغفار",
                titleEn = "Surah An-Nasr: Victory & Forgiveness",
                surahNumber = 110,
                surahNameArabic = "النصر",
                surahNameUr = "سورۃ النصر",
                startAyah = 1,
                endAyah = 3,
                targetAyahs = listOf(
                    AyahEntity(110001, 110, 1, "إِذَا جَاءَ نَصْرُ اللَّهِ وَالْفَتْحُ", "اِذَا جَآءَ نَصْرُ اللّٰهِ وَالْفَتْحُ", "When the victory of Allah has come and the conquest,", "جب اللہ کی مدد اور فتح آ جائے،", 30, 603, 1),
                    AyahEntity(110002, 110, 2, "وَرَأَيْتَ النَّاسَ يَدْخُلُونَ فِي دِينِ اللَّهِ أَفْوَاجًا", "وَرَاَیْتَ النَّاسَ یَدْخُلُوْنَ فِیْ دِیْنِ اللّٰهِ اَفْوَاجًا", "And you see the people entering into the religion of Allah in multitudes,", "اور آپ لوگوں کو اللہ کے دین میں فوج در فوج داخل ہوتے دیکھ لیں،", 30, 603, 1),
                    AyahEntity(110003, 110, 3, "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ۚ إِنَّهُ كَانَ تَوَّابًا", "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ؕ اِنَّهٗ كَانَ تَوَّابًا", "Then exalt [Him] with praise of your Lord and ask forgiveness of Him. Indeed, He is ever Accepting of repentance.", "تو اپنے رب کی حمد کے ساتھ تسبیح کیجیے اور اس سے مغفرت طلب کیجیے، بے شک وہ بڑا معاف فرمانے والا ہے۔", 30, 603, 1)
                ),
                tajweedFocusUr = "مدِ متصل در 'جَآءَ' (۴ سے ۵ حرکات) اور لفظ اللہ پر لام کی تفخیم۔",
                makhrajTipUr = "'نَصْرُ' میں صاد کو پر پڑھیں اور سین کے ساتھ خلط ملط نہ ہونے دیں۔",
                virtueUr = "یہ سورت بھی ربع قرآن کے برابر ہے اور تسبیح و استغفار کی تلقین کرتی ہے۔"
            )
        )

        // Day 26: Surah Al-Masad (Complete 1-5)
        list.add(
            DailyLesson(
                dayNumber = 26,
                titleUr = "سورۃ المسد: قلقلہ کی جامع مشق",
                titleEn = "Surah Al-Masad: Practice of Qalqalah",
                surahNumber = 111,
                surahNameArabic = "المسد",
                surahNameUr = "سورۃ المسد",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(111001, 111, 1, "تَبَّتْ يَدَا أَبِي لَهَبٍ وَتَبَّ", "تَبَّتْ یَدَآ اَبِیْ لَهَبٍ وَّتَبَّ", "May the hands of Abu Lahab be ruined, and ruined is he.", "ٹوٹ گئے ابو لہب کے دونوں ہاتھ اور وہ تباہ ہو گیا،", 30, 603, 1),
                    AyahEntity(111002, 111, 2, "مَا أَغْنَىٰ عَنْهُ مَالُهُ وَمَا كَسَبَ", "مَآ اَغْنٰى عَنْهُ مَالُهٗ وَمَا كَسَبَ", "His wealth will not avail him or that which he gained.", "نہ اس کا مال اس کے کچھ کام آیا اور نہ وہ جو اس نے کمایا،", 30, 603, 1),
                    AyahEntity(111003, 111, 3, "سَيَصْلَىٰ نَارًا ذَاتَ لَهَبٍ", "سَیَصْلٰى نَارًا ذَاتَ لَهَبٍ", "He will [enter to] burn in a Fire of [blazing] flame", "عنقریب وہ شعلے مارتی آگ میں داخل ہوگا،", 30, 603, 1),
                    AyahEntity(111004, 111, 4, "وَامْرَأَتُهُ حَمَّالَةَ الْحَطَبِ", "وَامْرَاَتُهٗ ؕ حَمَّالَةَ الْحَطَبِ", "And his wife [as well] - the carrier of firewood.", "اور اس کی بیوی بھی، جو ایندھن اٹھائے پھرتی ہے،", 30, 603, 1),
                    AyahEntity(111005, 111, 5, "فِي جِيدِهَا حَبْلٌ مِّن مَّسَدٍ", "فِیْ جِیْدِهَا حَبْلٌ مِّنْ مَّسَدٍ", "Around her neck is a rope of [twisted] fiber.", "اس کے گلے میں کھجور کی چھال کی بٹی ہوئی رسی ہوگی۔", 30, 603, 1)
                ),
                tajweedFocusUr = "قلقلہ اکبر مشدد پر وقف (وَتَبَّ) اور قلقلہ اوسط در (كَسَبَ، لَهَبٍ، مَّسَدٍ)۔",
                makhrajTipUr = "'وَتَبَّ' پر وقف کرتے وقت باء مشدد پر تھوڑا سا دباؤ دے کر مضبوط قلقلہ ادا کریں۔",
                virtueUr = "قلقلہ کی تمام درجوں کی درست ادائیگی کے لیے بہترین تمرین۔"
            )
        )

        // Day 27: Surah Al-Ikhlas (Complete 1-4)
        list.add(
            DailyLesson(
                dayNumber = 27,
                titleUr = "سورۃ الاخلاص: توحیدِ خالص اور ثلثِ قرآن",
                titleEn = "Surah Al-Ikhlas: Pure Monotheism",
                surahNumber = 112,
                surahNameArabic = "الإخلاص",
                surahNameUr = "سورۃ الاخلاص",
                startAyah = 1,
                endAyah = 4,
                targetAyahs = listOf(
                    AyahEntity(112001, 112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "قُلْ هُوَ اللّٰهُ اَحَدٌ", "Say, 'He is Allah, [who is] One,'", "کہہ دیجیے کہ وہ اللہ ایک ہے،", 30, 604, 1),
                    AyahEntity(112002, 112, 2, "اللَّهُ الصَّمَدُ", "اَللّٰهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "اللہ بے نیاز ہے،", 30, 604, 1),
                    AyahEntity(112003, 112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "لَمْ یَلِدْ ۙ وَلَمْ یُوْلَدْ", "He neither begets nor is born,", "نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے،", 30, 604, 1),
                    AyahEntity(112004, 112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "وَلَمْ یَكُنْ لَّهٗ كُفُوًا اَحَدٌ", "Nor is there to Him any equivalent.", "اور کوئی اس کا ہمسر نہیں ہے۔", 30, 604, 1)
                ),
                tajweedFocusUr = "قلقلہ دال (اَحَدْ، الصَّمَدْ، یَلِدْ، یُوْلَدْ) اور اظہار شفوی در 'لَمْ یَلِدْ'۔",
                makhrajTipUr = "'الصَّمَدُ' میں صاد کو پر پڑھیں اور دال پر وقف کے وقت صاف قلقلہ کی گونج پیدا ہو۔",
                virtueUr = "سورۃ الاخلاص ثلث قرآن (ایک تہائی قرآن) کے برابر فضیلت رکھتی ہے۔"
            )
        )

        // Day 28: Surah Al-Falaq (Complete 1-5)
        list.add(
            DailyLesson(
                dayNumber = 28,
                titleUr = "سورۃ الفلق: شرور سے پناہ اور قلقلہ قاف",
                titleEn = "Surah Al-Falaq: Seeking Refuge from Evil",
                surahNumber = 113,
                surahNameArabic = "الفلق",
                surahNameUr = "سورۃ الفلق",
                startAyah = 1,
                endAyah = 5,
                targetAyahs = listOf(
                    AyahEntity(113001, 113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "قُلْ اَعُوْذُ بِرَبِّ الْفَلَقِ", "Say, 'I seek refuge in the Lord of daybreak'", "آپ کہیے کہ میں صبح کے رب کی پناہ مانگتا ہوں،", 30, 604, 1),
                    AyahEntity(113002, 113, 2, "مِن شَرِّ مَا خَلَقَ", "مِنْ شَرِّ مَا خَلَقَ", "From the evil of that which He created", "ہر اس چیز کے شر سے جو اس نے پیدا کی،", 30, 604, 1),
                    AyahEntity(113003, 113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "وَمِنْ شَرِّ غَاسِقٍ اِذَا وَقَبَ", "And from the evil of darkness when it settles", "اور اندھیری رات کے شر سے جب وہ چھا جائے،", 30, 604, 1),
                    AyahEntity(113004, 113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "وَمِنْ شَرِّ النَّفّٰثٰتِ فِی الْعُقَدِ", "And from the evil of the blowers in knots", "اور گرہوں میں پھونکنے والیوں کے شر سے،", 30, 604, 1),
                    AyahEntity(113005, 113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "وَمِنْ شَرِّ حَاسِدٍ اِذَا حَسَدَ", "And from the evil of an envier when he envies.", "اور حسد کرنے والے کے شر سے جب وہ حسد کرے۔", 30, 604, 1)
                ),
                tajweedFocusUr = "اخفاء نون ساکن (مِن شَرِّ) اور قلقلہ قاف (الْفَلَقِ، خَلَقَ) و باء (وَقَبَ)۔",
                makhrajTipUr = "'النَّفّٰثٰتِ' میں ثاء کو سین نہ پڑھیں اور نون مشدد پر کامل غنہ کریں۔",
                virtueUr = "معوذتین ہر قسم کے جادو، نظرِ بد اور شیطانی وسوسوں سے مضبوط ترین ڈھال ہیں۔"
            )
        )

        // Day 29: Surah An-Nas (Complete 1-6)
        list.add(
            DailyLesson(
                dayNumber = 29,
                titleUr = "سورۃ الناس: وسوسوں سے امان اور غنہ",
                titleEn = "Surah An-Nas: Refuge against Whispers",
                surahNumber = 114,
                surahNameArabic = "الناس",
                surahNameUr = "سورۃ الناس",
                startAyah = 1,
                endAyah = 6,
                targetAyahs = listOf(
                    AyahEntity(114001, 114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "قُلْ اَعُوْذُ بِرَبِّ النَّاسِ", "Say, 'I seek refuge in the Lord of mankind,'", "کہہ دیجیے کہ میں انسانوں کے رب کی پناہ میں آتا ہوں،", 30, 604, 1),
                    AyahEntity(114002, 114, 2, "مَلِكِ النَّاسِ", "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "جو انسانوں کا حقیقی بادشاہ ہے،", 30, 604, 1),
                    AyahEntity(114003, 114, 3, "إِلَٰهِ النَّاسِ", "اِلٰهِ النَّاسِ", "The God of mankind,", "جو انسانوں کا معبودِ برحق ہے،", 30, 604, 1),
                    AyahEntity(114004, 114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "مِنْ شَرِّ الْوَسْوَاسِ ۬ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "بار بار وسوسہ ڈال کر پیچھے ہٹ جانے والے کے شر سے،", 30, 604, 1),
                    AyahEntity(114005, 114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "الَّذِیْ یُوَسْوِسُ فِیْ صُدُوْرِ النَّاسِ", "Who whispers into the breasts of mankind -", "جو لوگوں کے سینوں میں وسوسے ڈالتا ہے،", 30, 604, 1),
                    AyahEntity(114006, 114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.", "خواہ وہ جنات میں سے ہو یا انسانوں میں سے۔", 30, 604, 1)
                ),
                tajweedFocusUr = "نون مشدد کا غنہ در 'النَّاسِ' اور مد عارض برائے وقف۔",
                makhrajTipUr = "'صُدُوْرِ' میں صاد موٹا اور دال باریک ہے، سین اور دال کا فرق ملحوظ رکھیں۔",
                virtueUr = "ہر صبح و شام تین بار پڑھنے سے ہر بری چیز سے کفایت ہوتی ہے۔"
            )
        )

        // Day 30: Comprehensive Quran Review & Dua
        list.add(
            DailyLesson(
                dayNumber = 30,
                titleUr = "جامع جائزہ، تکمیل دورہ اور دعائے ختم القرآن",
                titleEn = "Comprehensive Review & Completion Dua",
                surahNumber = 1,
                surahNameArabic = "الفاتحة",
                surahNameUr = "دعائے تکمیل قرآن",
                startAyah = 1,
                endAyah = 7,
                targetAyahs = listOf(
                    AyahEntity(1001, 1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِیْمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔", 1, 1, 1),
                    AyahEntity(1002, 1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِیْنَ", "[All] praise is [due] to Allah, Lord of the worlds -", "سب تعریفیں اللہ ہی کے لیے ہیں جو تمام جہانوں کا پروردگار ہے۔", 1, 1, 1),
                    AyahEntity(1005, 1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "اِیَّاكَ نَعْبُدُ وَاِیَّاكَ نَسْتَعِیْنُ", "It is You we worship and You we ask for help.", "ہم صرف تیری ہی عبادت کرتے ہیں اور صرف تجھ ہی سے مدد چاہتے ہیں۔", 1, 1, 1),
                    AyahEntity(1006, 1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "اِهْدِنَا الصِّرَاطَ الْمُسْتَقِیْمَ", "Guide us to the straight path -", "ہمیں سیدھے راستے کی ہدایت فرما۔", 1, 1, 1)
                ),
                tajweedFocusUr = "تمام ۳۰ دنوں کے مخارج، قلقلہ، غنہ اور مدود کا مکمل جائزہ۔",
                makhrajTipUr = "ماشاء اللہ! آپ نے ۳۰ روزہ قرآنی اسباق کا مرحلہ کامیابی سے طے کیا۔ اس تجوید پر مستقل تلاوت جاری رکھیں۔",
                virtueUr = "قرآن سیکھنے اور سکھانے والا تمام انسانوں میں سب سے بہترین ہے۔ (صحیح بخاری)"
            )
        )

        return list
    }

    fun getLessonByDay(day: Int): DailyLesson {
        val all = getAllLessons()
        val index = (day - 1).coerceIn(0, all.size - 1)
        return all[index]
    }
}
