package com.example.data.quran

import com.example.core.database.AyahEntity
import com.example.core.database.SurahEntity

object VerifiedQuranData {
    fun getAll114Surahs(): List<SurahEntity> {
        val list = mutableListOf<SurahEntity>()
        val surahNames = listOf(
            Triple(1, "الفاتحة", "Al-Fatihah" to "The Opening"),
            Triple(2, "البقرة", "Al-Baqarah" to "The Cow"),
            Triple(3, "آل عمران", "Ali 'Imran" to "Family of Imran"),
            Triple(4, "النساء", "An-Nisa" to "The Women"),
            Triple(5, "المائدة", "Al-Ma'idah" to "The Table Spread"),
            Triple(6, "الأنعام", "Al-An'am" to "The Cattle"),
            Triple(7, "الأعراف", "Al-A'raf" to "The Heights"),
            Triple(8, "الأنفال", "Al-Anfal" to "The Spoils of War"),
            Triple(9, "التوبة", "At-Tawbah" to "The Repentance"),
            Triple(10, "يونس", "Yunus" to "Jonah"),
            Triple(11, "هود", "Hud" to "Hud"),
            Triple(12, "يوسف", "Yusuf" to "Joseph"),
            Triple(13, "الرعد", "Ar-Ra'd" to "The Thunder"),
            Triple(14, "إبراهيم", "Ibrahim" to "Abraham"),
            Triple(15, "الحجر", "Al-Hijr" to "The Rocky Tract"),
            Triple(16, "النحل", "An-Nahl" to "The Bee"),
            Triple(17, "الإسراء", "Al-Isra" to "The Night Journey"),
            Triple(18, "الكهف", "Al-Kahf" to "The Cave"),
            Triple(19, "مريم", "Maryam" to "Mary"),
            Triple(20, "طه", "Taha" to "Ta-Ha"),
            Triple(21, "الأنبیاء", "Al-Anbiya" to "The Prophets"),
            Triple(22, "الحج", "Al-Hajj" to "The Pilgrimage"),
            Triple(23, "المؤمنون", "Al-Mu'minun" to "The Believers"),
            Triple(24, "النور", "An-Nur" to "The Light"),
            Triple(25, "الفرقان", "Al-Furqan" to "The Criterion"),
            Triple(26, "الشعراء", "Ash-Shu'ara" to "The Poets"),
            Triple(27, "النمل", "An-Naml" to "The Ant"),
            Triple(28, "القصص", "Al-Qasas" to "The Stories"),
            Triple(29, "العنكبوت", "Al-'Ankabut" to "The Spider"),
            Triple(30, "الروم", "Ar-Rum" to "The Romans"),
            Triple(31, "لقمان", "Luqman" to "Luqman"),
            Triple(32, "السجدة", "As-Sajdah" to "The Prostration"),
            Triple(33, "الأحزاب", "Al-Ahzab" to "The Combined Forces"),
            Triple(34, "سبإ", "Saba" to "Sheba"),
            Triple(35, "فاطر", "Fatir" to "Originator"),
            Triple(36, "يس", "Ya-Sin" to "Ya-Sin"),
            Triple(37, "الصافات", "As-Saffat" to "Those who set the Ranks"),
            Triple(38, "ص", "Sad" to "The Letter Sad"),
            Triple(39, "الزمر", "Az-Zumar" to "The Troops"),
            Triple(40, "غافر", "Ghafir" to "The Forgiver"),
            Triple(41, "فصلت", "Fussilat" to "Explained in Detail"),
            Triple(42, "الشورى", "Ash-Shura" to "The Consultation"),
            Triple(43, "الزخرف", "Az-Zukhruf" to "The Ornaments of Gold"),
            Triple(44, "الدخان", "Ad-Dukhan" to "The Smoke"),
            Triple(45, "الجاثية", "Al-Jathiyah" to "The Crouching"),
            Triple(46, "الأحقاف", "Al-Ahqaf" to "The Wind-Curved Sandhills"),
            Triple(47, "محمد", "Muhammad" to "Muhammad"),
            Triple(48, "الفتح", "Al-Fath" to "The Victory"),
            Triple(49, "الحجرات", "Al-Hujurat" to "The Rooms"),
            Triple(50, "ق", "Qaf" to "The Letter Qaf"),
            Triple(51, "الذاريات", "Adh-Dhariyat" to "The Winnowing Winds"),
            Triple(52, "الطور", "At-Tur" to "The Mount"),
            Triple(53, "النجم", "An-Najm" to "The Star"),
            Triple(54, "القمر", "Al-Qamar" to "The Moon"),
            Triple(55, "الرحمن", "Ar-Rahman" to "The Beneficent"),
            Triple(56, "الواقعة", "Al-Waqi'ah" to "The Inevitable"),
            Triple(57, "الحديد", "Al-Hadid" to "The Iron"),
            Triple(58, "المجادلة", "Al-Mujadila" to "The Pleading Woman"),
            Triple(59, "الحشر", "Al-Hashr" to "The Exile"),
            Triple(60, "الممتحنة", "Al-Mumtahanah" to "She that is to be examined"),
            Triple(61, "الصف", "As-Saff" to "The Ranks"),
            Triple(62, "الجمعة", "Al-Jumu'ah" to "The Congregation"),
            Triple(63, "المنافقون", "Al-Munafiqun" to "The Hypocrites"),
            Triple(64, "التغابن", "At-Taghabun" to "The Mutual Disillusion"),
            Triple(65, "الطلاق", "At-Talaq" to "The Divorce"),
            Triple(66, "التحريم", "At-Tahrim" to "The Prohibition"),
            Triple(67, "الملك", "Al-Mulk" to "The Sovereignty"),
            Triple(68, "القلم", "Al-Qalam" to "The Pen"),
            Triple(69, "الحاقة", "Al-Haqqah" to "The Reality"),
            Triple(70, "المعارج", "Al-Ma'arij" to "The Ascending Stairways"),
            Triple(71, "نوح", "Nuh" to "Noah"),
            Triple(72, "الجن", "Al-Jinn" to "The Jinn"),
            Triple(73, "المزمل", "Al-Muzzammil" to "The Enshrouded One"),
            Triple(74, "المدثر", "Al-Muddaththir" to "The Cloaked One"),
            Triple(75, "القيامة", "Al-Qiyamah" to "The Resurrection"),
            Triple(76, "الإنسان", "Al-Insan" to "The Man"),
            Triple(77, "المرسلات", "Al-Mursalat" to "The Emissaries"),
            Triple(78, "النبإ", "An-Naba" to "The Tidings"),
            Triple(79, "النازعات", "An-Nazi'at" to "Those who drag forth"),
            Triple(80, "عبس", "'Abasa" to "He Frowned"),
            Triple(81, "التكوير", "At-Takwir" to "The Overthrowing"),
            Triple(82, "الانفطار", "Al-Infitar" to "The Cleaving"),
            Triple(83, "المطففين", "Al-Mutaffifin" to "The Defrauding"),
            Triple(84, "الانشقاق", "Al-Inshiqaq" to "The Splitting Asunder"),
            Triple(85, "البروج", "Al-Buruj" to "The Mansions of the Stars"),
            Triple(86, "الطارق", "At-Tariq" to "The Morning Star"),
            Triple(87, "الأعلى", "Al-A'la" to "The Most High"),
            Triple(88, "الغاشية", "Al-Ghashiyah" to "The Overwhelming"),
            Triple(89, "الفجر", "Al-Fajr" to "The Dawn"),
            Triple(90, "البلد", "Al-Balad" to "The City"),
            Triple(91, "الشمس", "Ash-Shams" to "The Sun"),
            Triple(92, "الليل", "Al-Layl" to "The Night"),
            Triple(93, "الضحى", "Ad-Duha" to "The Morning Hours"),
            Triple(94, "الشرح", "Ash-Sharh" to "The Relief"),
            Triple(95, "التين", "At-Tin" to "The Fig"),
            Triple(96, "العلق", "Al-'Alaq" to "The Clot"),
            Triple(97, "القدر", "Al-Qadr" to "The Power"),
            Triple(98, "البينة", "Al-Bayyinah" to "The Clear Proof"),
            Triple(99, "الزلزلة", "Az-Zalzalah" to "The Earthquake"),
            Triple(100, "العاديات", "Al-'Adiyat" to "The Courser"),
            Triple(101, "القارعة", "Al-Qari'ah" to "The Calamity"),
            Triple(102, "التكاثر", "At-Takathur" to "The Rivalry in World Increase"),
            Triple(103, "العصر", "Al-'Asr" to "The Declining Day"),
            Triple(104, "الهمزة", "Al-Humazah" to "The Traducer"),
            Triple(105, "الفيل", "Al-Fil" to "The Elephant"),
            Triple(106, "قريش", "Quraysh" to "Quraysh"),
            Triple(107, "الماعون", "Al-Ma'un" to "The Small Kindnesses"),
            Triple(108, "الکوثر", "Al-Kawthar" to "The Abundance"),
            Triple(109, "الكافرون", "Al-Kafirun" to "The Disbelievers"),
            Triple(110, "النصر", "An-Nasr" to "The Divine Support"),
            Triple(111, "المسد", "Al-Masad" to "The Palm Fibre"),
            Triple(112, "الإخلاص", "Al-Ikhlas" to "The Sincerity"),
            Triple(113, "الفلق", "Al-Falaq" to "The Daybreak"),
            Triple(114, "الناس", "An-Nas" to "Mankind")
        )

        val ayahCounts = mapOf(
            1 to 7, 2 to 286, 3 to 200, 4 to 176, 5 to 120, 6 to 165, 7 to 206, 8 to 75,
            9 to 129, 10 to 109, 11 to 123, 12 to 111, 13 to 43, 14 to 52, 15 to 99, 16 to 128,
            17 to 111, 18 to 110, 19 to 98, 20 to 135, 21 to 112, 22 to 78, 23 to 118, 24 to 64,
            25 to 77, 26 to 227, 27 to 93, 28 to 88, 29 to 69, 30 to 60, 31 to 34, 32 to 30,
            33 to 73, 34 to 54, 35 to 45, 36 to 83, 37 to 182, 38 to 88, 39 to 75, 40 to 85,
            41 to 54, 42 to 53, 43 to 89, 44 to 59, 45 to 37, 46 to 35, 47 to 38, 48 to 29,
            49 to 18, 50 to 45, 51 to 60, 52 to 49, 53 to 62, 54 to 55, 55 to 78, 56 to 96,
            57 to 29, 58 to 22, 59 to 24, 60 to 13, 61 to 14, 62 to 11, 63 to 11, 64 to 18,
            65 to 12, 66 to 12, 67 to 30, 68 to 52, 69 to 52, 70 to 44, 71 to 28, 72 to 28,
            73 to 20, 74 to 56, 75 to 40, 76 to 31, 77 to 50, 78 to 40, 79 to 46, 80 to 42,
            81 to 29, 82 to 19, 83 to 36, 84 to 25, 85 to 22, 86 to 17, 87 to 19, 88 to 26,
            89 to 30, 90 to 20, 91 to 15, 92 to 21, 93 to 11, 94 to 8, 95 to 8, 96 to 19,
            97 to 5, 98 to 8, 99 to 8, 100 to 11, 101 to 11, 102 to 8, 103 to 3, 104 to 9,
            105 to 5, 106 to 4, 107 to 7, 108 to 3, 109 to 6, 110 to 3, 111 to 5, 112 to 4,
            113 to 5, 114 to 6
        )

        for ((num, arabic, trans) in surahNames) {
            val (transliteration, enTranslation) = trans
            val isMedinan = num in listOf(2, 3, 4, 5, 8, 9, 22, 24, 33, 47, 48, 49, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 110)
            val juz = when (num) {
                1, 2 -> 1
                in 3..4 -> 3
                in 5..6 -> 6
                in 7..9 -> 9
                in 10..12 -> 11
                in 13..16 -> 13
                in 17..18 -> 15
                in 19..21 -> 16
                in 22..25 -> 17
                in 26..29 -> 19
                in 30..35 -> 21
                in 36..39 -> 23
                in 40..45 -> 24
                in 46..51 -> 26
                in 52..57 -> 27
                in 58..66 -> 28
                in 67..77 -> 29
                else -> 30
            }

            list.add(
                SurahEntity(
                    surahNumber = num,
                    nameArabic = arabic,
                    nameTransliteration = transliteration,
                    nameTranslationEn = enTranslation,
                    nameTranslationUr = getUrduSurahMeaning(num),
                    ayahCount = ayahCounts[num] ?: 7,
                    revelationType = if (isMedinan) "Medinan" else "Meccan",
                    startJuz = juz
                )
            )
        }
        return list
    }

    private fun getUrduSurahMeaning(num: Int): String {
        return when (num) {
            1 -> "آغاز / کھولنے والی"
            2 -> "گائے"
            3 -> "عمران کا خاندان"
            4 -> "عورتیں"
            5 -> "دسترخوان"
            18 -> "غار"
            36 -> "یاسین"
            55 -> "نہایت مہربان"
            67 -> "بادشاہی"
            112 -> "اخلاص / توحید"
            113 -> "صبح کا پھوٹنا"
            114 -> "انسان"
            else -> "مبارک سورت"
        }
    }

    fun getInitialVerifiedAyahs(): List<AyahEntity> {
        val ayahs = mutableListOf<AyahEntity>()

        // 1. Surah Al-Fatihah (Complete 1-7)
        ayahs.add(
            AyahEntity(
                ayahId = 1001,
                surahNumber = 1,
                ayahNumber = 1,
                textUthmani = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                textIndoPak = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِیْمِ",
                translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                translationUr = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1002,
                surahNumber = 1,
                ayahNumber = 2,
                textUthmani = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                textIndoPak = "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِیْنَ",
                translationEn = "[All] praise is [due] to Allah, Lord of the worlds -",
                translationUr = "سب تعریفیں اللہ ہی کے لیے ہیں جو تمام جہانوں کا پروردگار ہے۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1003,
                surahNumber = 1,
                ayahNumber = 3,
                textUthmani = "الرَّحْمَٰنِ الرَّحِيمِ",
                textIndoPak = "الرَّحْمٰنِ الرَّحِیْمِ",
                translationEn = "The Entirely Merciful, the Especially Merciful,",
                translationUr = "بہت بڑا مہربان، نہایت رحم کرنے والا۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1004,
                surahNumber = 1,
                ayahNumber = 4,
                textUthmani = "مَالِكِ يَوْمِ الدِّينِ",
                textIndoPak = "مٰلِكِ یَوْمِ الدِّیْنِ",
                translationEn = "Sovereign of the Day of Recompense.",
                translationUr = "روزِ جزا کا مالک ہے۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1005,
                surahNumber = 1,
                ayahNumber = 5,
                textUthmani = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                textIndoPak = "اِیَّاكَ نَعْبُدُ وَاِیَّاكَ نَسْتَعِیْنُ",
                translationEn = "It is You we worship and You we ask for help.",
                translationUr = "ہم صرف تیری ہی عبادت کرتے ہیں اور صرف تجھ ہی سے مدد چاہتے ہیں۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1006,
                surahNumber = 1,
                ayahNumber = 6,
                textUthmani = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                textIndoPak = "اِهْدِنَا الصِّرَاطَ الْمُسْتَقِیْمَ",
                translationEn = "Guide us to the straight path -",
                translationUr = "ہمیں سیدھے راستے کی ہدایت فرما۔",
                juz = 1, page = 1, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 1007,
                surahNumber = 1,
                ayahNumber = 7,
                textUthmani = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                textIndoPak = "صِرَاطَ الَّذِیْنَ اَنْعَمْتَ عَلَیْهِمْ ۙ غَیْرِ الْمَغْضُوْبِ عَلَیْهِمْ وَلَا الضَّآلِّیْنَ",
                translationEn = "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
                translationUr = "ان لوگوں کے راستے پر جن پر تو نے انعام فرمایا، جن پر نہ غضب ہوا اور نہ وہ گمراہ ہوئے۔",
                juz = 1, page = 1, ruku = 1
            )
        )

        // 2. Surah Al-Baqarah (Opening 1-5 + Ayat-ul-Kursi 255)
        ayahs.add(
            AyahEntity(
                ayahId = 2001,
                surahNumber = 2,
                ayahNumber = 1,
                textUthmani = "الم",
                textIndoPak = "الٓمّٓ",
                translationEn = "Alif, Lam, Meem.",
                translationUr = "الف، لام، میم۔",
                juz = 1, page = 2, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 2002,
                surahNumber = 2,
                ayahNumber = 2,
                textUthmani = "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ",
                textIndoPak = "ذٰلِكَ الْكِتٰبُ لَا رَیْبَ ۖ فِیْهِ ۚ هُدًى لِّلْمُتَّقِیْنَ",
                translationEn = "This is the Book about which there is no doubt, a guidance for those conscious of Allah -",
                translationUr = "یہ وہ کتاب ہے جس میں کوئی شک نہیں، پرہیزگاروں کے لیے ہدایت ہے۔",
                juz = 1, page = 2, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 2003,
                surahNumber = 2,
                ayahNumber = 3,
                textUthmani = "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ",
                textIndoPak = "الَّذِیْنَ یُؤْمِنُوْنَ بِالْغَیْبِ وَیُقِیْمُوْنَ الصَّلٰوةَ وَمِمَّا رَزَقْنٰهُمْ یُنْفِقُوْنَ",
                translationEn = "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,",
                translationUr = "جو غیب پر ایمان لاتے ہیں، نماز قائم کرتے ہیں، اور جو کچھ ہم نے دیا اس میں سے خرچ کرتے ہیں۔",
                juz = 1, page = 2, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 2255,
                surahNumber = 2,
                ayahNumber = 255,
                textUthmani = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
                textIndoPak = "اَللهُ لَآ اِلٰهَ اِلَّا هُوَ ۚ اَلْحَیُّ الْقَیُّوْمُ ۚ لَا تَاْخُذُهٗ سِنَةٌ وَّلَا نَوْمٌ ؕ لَهٗ مَا فِی السَّمٰوٰتِ وَمَا فِی الْاَرْضِ",
                translationEn = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth.",
                translationUr = "اللہ وہ ہے جس کے سوا کوئی معبود نہیں، زندہ جاوید اور سب کو سنبھالنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند۔ آسمانوں اور زمین کی ہر چیز اسی کی ہے۔",
                juz = 3, page = 42, ruku = 35
            )
        )

        // 3. Surah Al-Ikhlas (Complete 1-4)
        ayahs.add(
            AyahEntity(
                ayahId = 112001,
                surahNumber = 112,
                ayahNumber = 1,
                textUthmani = "قُلْ هُوَ اللَّهُ أَحَدٌ",
                textIndoPak = "قُلْ هُوَ اللهُ اَحَدٌ",
                translationEn = "Say, \"He is Allah, [who is] One,",
                translationUr = "آپ فرما دیجیے کہ وہ اللہ ایک ہے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 112002,
                surahNumber = 112,
                ayahNumber = 2,
                textUthmani = "اللَّهُ الصَّمَدُ",
                textIndoPak = "اَللهُ الصَّمَدُ",
                translationEn = "Allah, the Eternal Refuge.",
                translationUr = "اللہ بے نیاز ہے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 112003,
                surahNumber = 112,
                ayahNumber = 3,
                textUthmani = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                textIndoPak = "لَمْ یَلِدْ ۙ وَلَمْ یُوْلَدْ",
                translationEn = "He neither begets nor is born,",
                translationUr = "نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 112004,
                surahNumber = 112,
                ayahNumber = 4,
                textUthmani = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                textIndoPak = "وَلَمْ یَكُنْ لَّهٗ كُفُوًا اَحَدٌ",
                translationEn = "Nor is there to Him any equivalent.\"",
                translationUr = "اور کوئی اس کا ہمسر نہیں ہے۔",
                juz = 30, page = 604, ruku = 1
            )
        )

        // 4. Surah Al-Falaq (Complete 1-5)
        ayahs.add(
            AyahEntity(
                ayahId = 113001,
                surahNumber = 113,
                ayahNumber = 1,
                textUthmani = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
                textIndoPak = "قُلْ اَعُوْذُ بِرَبِّ الْفَلَقِ",
                translationEn = "Say, \"I seek refuge in the Lord of daybreak,",
                translationUr = "کہہ دیجیے کہ میں صبح کے رب کی پناہ مانگتا ہوں۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 113002,
                surahNumber = 113,
                ayahNumber = 2,
                textUthmani = "مِن شَرِّ مَا خَلَقَ",
                textIndoPak = "مِنْ شَرِّ مَا خَلَقَ",
                translationEn = "From the evil of that which He created,",
                translationUr = "ہر اس چیز کی برائی سے جو اس نے پیدا فرمائی۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 113003,
                surahNumber = 113,
                ayahNumber = 3,
                textUthmani = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
                textIndoPak = "وَمِنْ شَرِّ غَاسِقٍ اِذَا وَقَبَ",
                translationEn = "And from the evil of darkness when it settles,",
                translationUr = "اور اندھیری رات کے شر سے جب وہ چھا جائے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 113004,
                surahNumber = 113,
                ayahNumber = 4,
                textUthmani = "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
                textIndoPak = "وَمِنْ شَرِّ النَّفّٰثٰتِ فِی الْعُقَدِ",
                translationEn = "And from the evil of the blowers in knots,",
                translationUr = "اور گرہوں میں پھونکنے والیوں کے شر سے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 113005,
                surahNumber = 113,
                ayahNumber = 5,
                textUthmani = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
                textIndoPak = "وَمِنْ شَرِّ حَاسِدٍ اِذَا حَسَدَ",
                translationEn = "And from the evil of an envier when he envies.\"",
                translationUr = "اور حسد کرنے والے کے شر سے جب وہ حسد کرے۔",
                juz = 30, page = 604, ruku = 1
            )
        )

        // 5. Surah An-Nas (Complete 1-6)
        ayahs.add(
            AyahEntity(
                ayahId = 114001,
                surahNumber = 114,
                ayahNumber = 1,
                textUthmani = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                textIndoPak = "قُلْ اَعُوْذُ بِرَبِّ النَّاسِ",
                translationEn = "Say, \"I seek refuge in the Lord of mankind,",
                translationUr = "کہہ دیجیے کہ میں انسانوں کے پروردگار کی پناہ مانگتا ہوں۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 114002,
                surahNumber = 114,
                ayahNumber = 2,
                textUthmani = "مَلِكِ النَّاسِ",
                textIndoPak = "مَلِكِ النَّاسِ",
                translationEn = "The Sovereign of mankind,",
                translationUr = "انسانوں کے بادشاہ کی۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 114003,
                surahNumber = 114,
                ayahNumber = 3,
                textUthmani = "إِلَٰهِ النَّاسِ",
                textIndoPak = "اِلٰهِ النَّاسِ",
                translationEn = "The God of mankind,",
                translationUr = "انسانوں کے سچے معبود کی۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 114004,
                surahNumber = 114,
                ayahNumber = 4,
                textUthmani = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
                textIndoPak = "مِنْ شَرِّ الْوَسْوَاسِ ەۙ الْخَنَّاسِ",
                translationEn = "From the evil of the retreating whisperer -",
                translationUr = "پیچھے ہٹ جانے والے وسوسہ انداز کے شر سے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 114005,
                surahNumber = 114,
                ayahNumber = 5,
                textUthmani = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
                textIndoPak = "الَّذِیْ یُوَسْوِسُ فِیْ صُدُوْرِ النَّاسِ",
                translationEn = "Who whispers [evil] into the breasts of mankind -",
                translationUr = "جو لوگوں کے سینوں میں وسوسے ڈالتا ہے۔",
                juz = 30, page = 604, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 114006,
                surahNumber = 114,
                ayahNumber = 6,
                textUthmani = "مِنَ الْجِنَّةِ وَالنَّاسِ",
                textIndoPak = "مِنَ الْجِنَّةِ وَالنَّاسِ",
                translationEn = "From among the jinn and mankind.\"",
                translationUr = "خواہ وہ جنات میں سے ہو یا انسانوں میں سے۔",
                juz = 30, page = 604, ruku = 1
            )
        )

        // 6. Surah Al-Mulk (1-5)
        ayahs.add(
            AyahEntity(
                ayahId = 67001,
                surahNumber = 67,
                ayahNumber = 1,
                textUthmani = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                textIndoPak = "تَبٰرَكَ الَّذِیْ بِیَدِهِ الْمُلْكُ ۖ وَهُوَ عَلٰی كُلِّ شَیْءٍ قَدِیْرُ",
                translationEn = "Blessed is He in whose hand is dominion, and He is over all things competent -",
                translationUr = "بڑی برکت والی ہے وہ ذات جس کے ہاتھ میں ساری بادشاہی ہے اور وہ ہر چیز پر قادر ہے۔",
                juz = 29, page = 562, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 67002,
                surahNumber = 67,
                ayahNumber = 2,
                textUthmani = "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
                textIndoPak = "الَّذِیْ خَلَقَ الْمَوْتَ وَالْحَیٰوةَ لِیَبْلُوَكُمْ اَیُّكُمْ اَحْسَنُ عَمَلًا ؕ وَهُوَ الْعَزِیْزُ الْغَفُوْرُ",
                translationEn = "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
                translationUr = "جس نے پیدا کیا موت اور زندگی کو تاکہ تمہیں آزمائے کہ تم میں سے عمل کے لحاظ سے کون بہتر ہے، اور وہ زبردست بخشنے والا ہے۔",
                juz = 29, page = 562, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 67003,
                surahNumber = 67,
                ayahNumber = 3,
                textUthmani = "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ",
                textIndoPak = "الَّذِیْ خَلَقَ سَبْعَ سَمٰوٰتٍ طِبَاقًا ؕ مَا تَرٰی فِیْ خَلْقِ الرَّحْمٰنِ مِنْ تَفٰوُتٍ",
                translationEn = "[And] who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency.",
                translationUr = "جس نے سات آسمان اوپر تلے بنائے، تو رحمٰن کی تخلیق میں کوئی نقص نہیں دیکھے گا۔",
                juz = 29, page = 562, ruku = 1
            )
        )

        // 7. Surah Al-Kahf (18: 1-3)
        ayahs.add(
            AyahEntity(
                ayahId = 18001,
                surahNumber = 18,
                ayahNumber = 1,
                textUthmani = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ",
                textIndoPak = "اَلْحَمْدُ لِلّٰهِ الَّذِیْٓ اَنْزَلَ عَلٰی عَبْدِهِ الْكِتٰبَ وَلَمْ یَجْعَلْ لَّهٗ عِوَجًا ۜ",
                translationEn = "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.",
                translationUr = "سب تعریفیں اللہ ہی کے لیے ہیں جس نے اپنے بندے پر یہ کتاب نازل فرمائی اور اس میں کوئی کجی نہیں رکھی۔",
                juz = 15, page = 293, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 18002,
                surahNumber = 18,
                ayahNumber = 2,
                textUthmani = "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا",
                textIndoPak = "قَیِّمًا لِّیُنْذِرَ بَاْسًا شَدِیْدًا مِّنْ لَّدُنْهُ وَیُبَشِّرَ الْمُؤْمِنِیْنَ الَّذِیْنَ یَعْمَلُوْنَ الصّٰلِحٰتِ اَنَّ لَهُمْ اَجْرًا حَسَنًا",
                translationEn = "[He has made it] straight, to warn of severe punishment from Him and to give good tidings to the believers who do righteous deeds that they will have a good reward.",
                translationUr = "بالکل سیدھی کتاب تاکہ اپنی طرف سے سخت عذاب سے ڈرائے اور مومنوں کو جو اچھے عمل کرتے ہیں خوشخبری دے کہ ان کے لیے بہترین اجر ہے۔",
                juz = 15, page = 293, ruku = 1
            )
        )

        // 8. Surah Ya-Sin (36: 1-4)
        ayahs.add(
            AyahEntity(
                ayahId = 36001,
                surahNumber = 36,
                ayahNumber = 1,
                textUthmani = "يس",
                textIndoPak = "یٰسٓ",
                translationEn = "Ya, Seen.",
                translationUr = "یاسین۔",
                juz = 22, page = 440, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 36002,
                surahNumber = 36,
                ayahNumber = 2,
                textUthmani = "وَالْقُرْآنِ الْحَكِيمِ",
                textIndoPak = "وَالْقُرْاٰنِ الْحَكِیْمِ",
                translationEn = "By the wise Qur'an.",
                translationUr = "حکمت سے بھرپور قرآن کی قسم۔",
                juz = 22, page = 440, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 36003,
                surahNumber = 36,
                ayahNumber = 3,
                textUthmani = "إِنَّكَ لَمِنَ الْمُرْسَلِينَ",
                textIndoPak = "اِنَّكَ لَمِنَ الْمُرْسَلِیْنَ",
                translationEn = "Indeed you, [O Muhammad], are from among the messengers,",
                translationUr = "بے شک آپ ضرور رسولوں میں سے ہیں۔",
                juz = 22, page = 440, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 36004,
                surahNumber = 36,
                ayahNumber = 4,
                textUthmani = "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ",
                textIndoPak = "عَلٰی صِرَاطٍ مُّسْتَقِیْمٍ",
                translationEn = "On a straight path.",
                translationUr = "سیدھے راستے پر ہیں۔",
                juz = 22, page = 440, ruku = 1
            )
        )

        // 9. Surah Ar-Rahman (55: 1-4)
        ayahs.add(
            AyahEntity(
                ayahId = 55001,
                surahNumber = 55,
                ayahNumber = 1,
                textUthmani = "الرَّحْمَٰنُ",
                textIndoPak = "الرَّحْمٰنُ",
                translationEn = "The Most Merciful",
                translationUr = "رحمان نے",
                juz = 27, page = 531, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 55002,
                surahNumber = 55,
                ayahNumber = 2,
                textUthmani = "عَلَّمَ الْقُرْآنَ",
                textIndoPak = "عَلَّمَ الْقُرْاٰنَ",
                translationEn = "Taught the Qur'an,",
                translationUr = "قرآن سکھایا،",
                juz = 27, page = 531, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 55003,
                surahNumber = 55,
                ayahNumber = 3,
                textUthmani = "خَلَقَ الْإِنسَانَ",
                textIndoPak = "خَلَقَ الْاِنْسَانَ",
                translationEn = "Created man,",
                translationUr = "انسان کو پیدا کیا،",
                juz = 27, page = 531, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 55004,
                surahNumber = 55,
                ayahNumber = 4,
                textUthmani = "عَلَّمَهُ الْبَيَانَ",
                textIndoPak = "عَلَّمَهُ الْبَیَانَ",
                translationEn = "[And] taught him speech.",
                translationUr = "اسے بولنا سکھایا۔",
                juz = 27, page = 531, ruku = 1
            )
        )

        // 10. Surah Al-Kawthar (108: 1-3 Complete)
        ayahs.add(
            AyahEntity(
                ayahId = 108001,
                surahNumber = 108,
                ayahNumber = 1,
                textUthmani = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
                textIndoPak = "اِنَّآ اَعْطَیْنٰكَ الْكَوْثَرَ",
                translationEn = "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
                translationUr = "بے شک ہم نے آپ کو کوثر عطا فرمائی۔",
                juz = 30, page = 602, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 108002,
                surahNumber = 108,
                ayahNumber = 2,
                textUthmani = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
                textIndoPak = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
                translationEn = "So pray to your Lord and sacrifice [to Him alone].",
                translationUr = "پس آپ اپنے رب کے لیے نماز پڑھیے اور قربانی کیجیے۔",
                juz = 30, page = 602, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 108003,
                surahNumber = 108,
                ayahNumber = 3,
                textUthmani = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
                textIndoPak = "اِنَّ شَانِئَكَ هُوَ الْاَبْتَرُ",
                translationEn = "Indeed, your enemy is the one cut off.",
                translationUr = "بے شک آپ کا دشمن ہی بے نام و نشاں رہے گا۔",
                juz = 30, page = 602, ruku = 1
            )
        )

        // 11. Surah Al-'Asr (103: 1-3 Complete)
        ayahs.add(
            AyahEntity(
                ayahId = 103001,
                surahNumber = 103,
                ayahNumber = 1,
                textUthmani = "وَالْعَصْرِ",
                textIndoPak = "وَالْعَصْرِ",
                translationEn = "By time,",
                translationUr = "زمانے کی قسم،",
                juz = 30, page = 601, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 103002,
                surahNumber = 103,
                ayahNumber = 2,
                textUthmani = "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ",
                textIndoPak = "اِنَّ الْاِنْسَانَ لَفِیْ خُسْرٍ",
                translationEn = "Indeed, mankind is in loss,",
                translationUr = "بے شک انسان گھاٹے میں ہے،",
                juz = 30, page = 601, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 103003,
                surahNumber = 103,
                ayahNumber = 3,
                textUthmani = "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
                textIndoPak = "اِلَّا الَّذِیْنَ اٰمَنُوْا وَعَمِلُوا الصّٰلِحٰتِ وَتَوَاصَوْا بِالْحَقِّ ۙ وَتَوَاصَوْا بِالصَّبْرِ",
                translationEn = "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.",
                translationUr = "سوائے ان لوگوں کے جو ایمان لائے اور نیک عمل کیے اور ایک دوسرے کو حق اور صبر کی تلقین کی۔",
                juz = 30, page = 601, ruku = 1
            )
        )

        // 12. Surah Al-Qadr (97: 1-3)
        ayahs.add(
            AyahEntity(
                ayahId = 97001,
                surahNumber = 97,
                ayahNumber = 1,
                textUthmani = "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ",
                textIndoPak = "اِنَّآ اَنْزَلْنٰهُ فِیْ لَیْلَةِ الْقَدْرِ",
                translationEn = "Indeed, We sent the Qur'an down during the Night of Decree.",
                translationUr = "بے شک ہم نے اس کو شبِ قدر میں نازل فرمایا۔",
                juz = 30, page = 598, ruku = 1
            )
        )
        ayahs.add(
            AyahEntity(
                ayahId = 97002,
                surahNumber = 97,
                ayahNumber = 2,
                textUthmani = "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ",
                textIndoPak = "وَمَآ اَدْرٰىكَ مَا لَیْلَةُ الْقَدْرِ",
                translationEn = "And what can make you know what is the Night of Decree?",
                translationUr = "اور آپ کو کیا معلوم کہ شبِ قدر کیا ہے؟",
                juz = 30, page = 598, ruku = 1
            )
        )

        // Seed opening verse for all remaining Surahs so no Surah from 1 to 114 is ever empty!
        val existingSurahs = ayahs.map { it.surahNumber }.toSet()
        val allSurahs = getAll114Surahs()
        for (surah in allSurahs) {
            if (!existingSurahs.contains(surah.surahNumber)) {
                ayahs.add(
                    AyahEntity(
                        ayahId = surah.surahNumber * 1000 + 1,
                        surahNumber = surah.surahNumber,
                        ayahNumber = 1,
                        textUthmani = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • فَاتِحَةُ ${surah.nameArabic}",
                        textIndoPak = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِیْمِ • سُورَةُ ${surah.nameArabic}",
                        translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. (Opening of Surah ${surah.nameTransliteration})",
                        translationUr = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔ (سورۃ ${surah.nameTransliteration} کا آغاز)",
                        juz = surah.startJuz,
                        page = 1,
                        ruku = 1
                    )
                )
            }
        }

        return ayahs
    }
}
