# Quran Learning Companion — Complete Android Master Plan

**Document type:** Product vision, UX/UI design system, technical architecture, online/offline feature specification, AI integration, Cloudflare deployment plan, QA criteria, and copy-paste build prompts  
**Target platform:** Native Android phone app  
**Primary implementation:** Kotlin + Jetpack Compose, generated/iterated with Google AI Studio Android Build mode  
**Backend and AI gateway:** Cloudflare Workers  
**AI provider assumption:** Google Gemini API for optional online AI learning assistance; provider keys must stay server-side  
**Core product rule:** Quran reading and essential learning must remain useful without an internet connection and without mandatory user registration.

---

## 1. Product Vision

Build a polished, accessible, trustworthy Quran learning app for children, teenagers, adults, beginners, regular readers, and people memorizing the Quran. The app must combine verified Quran content, reading tools, translations, recitation audio, structured lessons, memorization and revision, and an AI-supported recitation practice experience.

The app must work in **three operating conditions**:

1. **Offline mode:** The user can read available Quran content, use saved translations, listen to downloaded recitations, learn downloaded lessons, use bookmarks, continue reading, track progress, and use the on-device recitation model if it is installed and compatible.
2. **Online mode:** The app can download licensed content packs, check for updates, use the Cloudflare Worker API for Gemini-backed learning explanations, and access only those online AI services that have been explicitly configured.
3. **Mixed mode:** Offline features remain available while the network is absent. Online-only features show a clear status and retry option, then become available again when the connection returns. Never block the Quran reader because an API is unavailable.

### Product promise

> Learn, read, listen, revise, and practise the Quran in one respectful app—with optional translations, offline-first functionality, and clearly explained AI assistance.

### Non-negotiable product principles

- The complete Quran must come from a verified, properly licensed source. Do not generate, paraphrase, reconstruct, or “correct” Quranic Arabic with a general-purpose LLM.
- Quran text, ayah numbering, Arabic diacritics, waqf symbols, and page mappings must remain intact according to the selected, verified source.
- Translation and tafsir must be sourced, attributed, and treated as published content—not as text that the AI may silently rewrite.
- AI feedback must be humble and confidence-aware. Do not claim certainty when audio is unclear or model confidence is low.
- Reading, bookmarks, basic progress, lessons already downloaded, and downloaded audio must not require login or internet.
- No API key, secret, provider credential, or privileged token may be embedded in Android source code, assets, BuildConfig, or the APK/AAB.
- Do not fake a working AI feature with a mock response in a production build. Any prototype/mock implementation must be clearly marked as demo-only.
- Prefer a modular, testable codebase with clear repositories, ViewModels, state handling, and separation between UI, data, AI, and networking.

---

## 2. Platform and Deployment Reality

### Google AI Studio Android Build mode

Use Google AI Studio Build mode with **Android** selected to generate a native Kotlin + Jetpack Compose Android project. AI Studio's Android output is a client-side Android application; it does not become the Cloudflare server/backend. Export/download the Android project ZIP and use it as the native app source. Test important hardware-dependent features on real phones, because a browser-based emulator does not fully reproduce microphone behaviour, performance, thermal constraints, or all Play services.

### Cloudflare's role

Cloudflare will host the **backend API and optional supporting assets**, not run the installed Android app itself.

- **Cloudflare Workers:** HTTPS API gateway, input validation, Gemini API proxy, usage limits, content-update metadata, health endpoint, and optional secure provider proxying.
- **Cloudflare R2 (optional):** downloadable audio packs, model packages, translation/content bundles, release metadata, and large assets where licensing and Cloudflare terms permit.
- **Cloudflare Pages (optional):** public landing page, privacy policy, terms, help pages, release notes, and documentation.
- **Cloudflare D1 (optional):** non-sensitive content metadata, app configuration, or anonymous aggregate operational records if needed. Do not use it as a reason to upload a user's private reading history or raw voice recordings by default.
- **Google Play Console:** production Android publishing and app listing. Cloudflare can host a download page or assets, but it is not a substitute for Google Play's Android app publishing workflow.

### Recommended high-level flow

```text
Android Kotlin + Jetpack Compose app
  ├── Room local database: Quran content, bookmarks, progress, lessons
  ├── Local audio player + downloaded audio packs
  ├── On-device Quran recitation model (when installed and supported)
  └── HTTPS calls only for online functions
         │
         ▼
Cloudflare Worker API (public HTTPS URL)
  ├── validate endpoint, request size, schema, and allowed action
  ├── rate-limit and enforce usage budgets
  ├── read secrets from Cloudflare's secret bindings
  ├── call Gemini API for allowed educational assistance
  ├── optionally call approved Quran-content services through a server adapter
  └── return a small, structured response
         │
         ▼
Gemini API / approved content provider

Optional static/content downloads: Cloudflare R2 or approved content-provider sync endpoint
```

**Important:** A Worker should not be treated as a GPU inference host for large speech models. Run the first offline recitation model directly on a compatible phone. If remote recitation inference is added later, use a separately validated service designed for that workload and keep it behind a secure API; do not try to run a large AI model inside a standard Worker request handler.

---

## 3. Target Users and Learning Paths

The onboarding flow should allow the user to choose a goal, but skipping onboarding must remain possible.

1. **New reader:** Arabic letters, harakat, joining letters, basic reading, Qaida lessons.
2. **Quran reader:** Quran navigation, translation preferences, audio, bookmarks, daily goals.
3. **Tajweed learner:** rule lessons, listening examples, supported pronunciation exercises, teacher-reviewed content.
4. **Hifz learner:** new lesson, previous lesson (sabqi), revision/manzil, hidden-text tests, mistake review.
5. **Translation learner:** Arabic-only or Arabic + Urdu/English translation, word-by-word meanings, approved tafsir references.
6. **Family / child-guided learning:** simple screens, age-appropriate lesson flow, no public social features by default, parent-guided settings without making an account mandatory.
7. **Accessibility-focused user:** large text, high contrast, screen-reader support, simplified layout, low-distraction reading mode.

Do not assume a user's gender, sect, age, skill, or religious background. Let people pick their reading script and language preferences respectfully.

---

## 4. Brand and Mobile UI Design System

### Visual direction

Use a calm, premium Islamic learning style: elegant, focused, warm, and modern. Avoid copying another app's UI. Do not use excessive ornament, busy backgrounds behind Arabic text, unnecessary animation, or AI-generated calligraphy as Quran content.

### Suggested palette

| Token | Suggested value | Use |
|---|---|---|
| Deep Emerald | `#123C32` | Primary brand colour, top bars, selected navigation |
| Forest Green | `#1E624D` | Primary buttons and learning actions |
| Soft Jade | `#DDEDE4` | Quiet cards, progress backgrounds |
| Warm Ivory | `#FAF7EF` | Default light surface / reading background |
| Muted Gold | `#C9A45C` | Subtle premium accent and progress highlights |
| Charcoal | `#202522` | Primary text |
| Secondary Grey | `#67716B` | Secondary text and metadata |
| Error Red | `#B42318` | Only meaningful errors and possible recitation issues |
| Dark Background | `#101713` | Dark mode base |
| Dark Surface | `#18231D` | Dark mode cards and toolbars |

Use accessible contrast. Red should not be the only way to communicate a recitation issue—pair it with an icon, label, and optional vibration. Provide a high-contrast option.

### Typography

- Use a legible Arabic Quran font with a verified distribution licence and proper diacritic/ligature rendering. Validate both Uthmani and Indo-Pak scripts if both are offered.
- Use a separate clean UI font for navigation and explanatory text (for example, Roboto or Noto Sans; confirm available font licence and rendering).
- Arabic Quran text should be noticeably larger than surrounding UI copy, with adjustable size and comfortable line spacing.
- Preserve right-to-left layout for Arabic. Urdu UI should support RTL; English UI should use LTR. Avoid mixing the two directions incorrectly in verse cards.
- Never draw the Quran page as a decorative image generated by an image model. Use real, verified text/rendering assets.

### Component style

- Rounded cards with restrained corners (approximately 16–22 dp), calm surfaces, subtle separators, clear touch targets, and little or no heavy shadow.
- Native Material 3 components adapted to the brand.
- Motion should be subtle and optional; respect Android animation/accessibility settings.
- Make microphone/listening state unmistakable, including a persistent stop/pause control.
- Use skeleton/loading states only for online requests; local Quran text should appear immediately when available.

### Phone-first navigation

Use a bottom navigation bar with five clear destinations:

1. **Home**
2. **Quran**
3. **Learn**
4. **AI Teacher**
5. **Progress**

Place Settings, content downloads, help, privacy controls, language, and about information under the top app bar/profile or More menu. Maintain support for smaller Android screens and font scaling.

### Home screen

- Greeting and current learning mode (optional; don't require an account).
- A prominent **Continue Reading** card showing the last Surah/ayah/page saved locally.
- Quick actions: Read Quran, Listen, Learn Qaida/Tajweed, Hifz Practice, Ask Quran Learning Assistant.
- Daily goal and locally calculated progress.
- Optional daily ayah card using pre-verified content and an attributed translation; allow the user to hide it.
- Offline/online status chip and an easy route to Download Manager.
- No intrusive ads over Quran content and no compulsory sign-in wall.

### Quran reader screen

- Clear Surah header, Bismillah where appropriate according to verified content rules, ayah/page navigation, and bookmarks.
- Script selector where supported: Uthmani / Indo-Pak.
- Translation selector: none, Urdu, English; optional word-by-word panel.
- Audio controls: play/pause, repeat, previous/next ayah, speed options if supported, Qari selector, and clear download status.
- Reading controls: font size, spacing, theme, keep-screen-on option, and page/ayah mode.
- Tap/long press on a word: show word meaning/pronunciation only when that data is available and verified.
- Optional AI practice controls kept separate from normal reading: **Start Listening**, **Pause**, **Repeat This Part**, **Continue Anyway**.
- Do not clutter the Mushaf screen with generated AI explanations. Keep tafsir and lesson details in a separate sheet or screen.

### AI Teacher screen

- Choose the current Surah/ayah or Hifz range before microphone capture.
- Explain the mode: local/offline model or online service.
- Show microphone permission and privacy status before the first session.
- Display listening state, word-level progression, candidate issue, confidence, retry, play-correct-audio, continue, and stop controls.
- If offline model is not installed, show an honest download/setup state. Never show fictional live analysis.
- If analysis is uncertain, say “I could not confidently assess this part—please repeat” rather than labelling a correct recitation wrong.

### Accessibility and usability

- Screen reader labels for all controls, meaningful content descriptions, logical focus traversal, sufficient touch target size, scalable fonts, reduced motion, contrast modes, and visible focus state.
- Do not rely on colour alone.
- Do not autoplay audio without user consent.
- Provide clear mic on/off indicators and controls.

---

## 5. Complete Feature Catalogue

### A. Complete Quran reader

- All 114 Surahs and verified ayah text.
- 30 Juz, Ruku, Hizb, Manzil, page and ayah navigation, with source-appropriate boundaries.
- Uthmani and Indo-Pak script options where validated data and fonts are available.
- Arabic-only mode.
- Optional Urdu translation and English translation.
- Word-by-word translation, pronunciation/transliteration, and word details when licensed and sourced.
- Search for Surah, ayah, Arabic words, translation text, or stored content.
- Bookmarks, favourites, last-read position, notes, reading sessions, and recent history stored locally.
- Adjustable Arabic text size, UI language, line spacing, light/dark/sepia reading surfaces, and full-screen mode.
- Ayah copy/share with source reference, while retaining correct Arabic text and optional attribution.
- Optional Sajdah markers, pause signs, and navigation aids according to the selected source.
- Quick navigation to a specific Surah:Ayah.

### B. Translations and tafsir

- Arabic with no translation, Arabic + Urdu, Arabic + English, and optional word-by-word meanings.
- Allow the user to set a default translation and change it at any time.
- Display translator/publisher/source attribution and translation edition/version.
- Offline translation packs after licence approval and installation.
- Downloaded tafsir can be browsed offline with source references.
- AI may explain a selected approved translation in simple language, but must identify this as an AI-generated learning explanation and never replace the original translation.
- Do not present AI-generated religious rulings as authoritative fatwas. Direct complex or disputed questions to qualified scholarship and referenced materials.

### C. Audio recitation

- A library of licensed/approved Qaris.
- Download a full recitation or selected Surahs/ayah ranges for offline playback.
- Surah playback, ayah playback, repeat count, selected-range loop, previous/next ayah, pause/resume, and queue controls.
- Optional adjustable playback speed, implemented without materially distorting the learning sound; show the selected speed.
- Background playback and Android media notification controls.
- Text highlight synced to audio only where timing/alignment is available and verified.
- Download manager with pack size, progress, pause/resume where supported, delete, and re-download.
- No unlicensed redistribution. Confirm the permitted use for each recitation and any downloadable files.

### D. Qaida and foundational course

- Arabic letters and letter recognition.
- Harakat, tanween, sukoon, shaddah, madd basics, joining letters, syllables and words.
- Noorani Qaida or another appropriately licensed/curriculum-approved course; do not copy proprietary pages or audio without permission.
- Stepwise lessons with examples, repeat listening, practice, locally stored completion, and review.
- Beginner-friendly language and optional Urdu/English explanations.
- Local quizzes: choose the correct sound, match a letter, identify an example, or repeat after a recording.

### E. Tajweed education

- Teacher-reviewed lessons on rules including Noon Sakin/Tanween, Meem Sakin, Izhar, Idgham, Iqlab, Ikhfa, Ghunnah, Madd, Qalqalah, and other curriculum-approved topics.
- Clearly distinguish **teaching a rule** from **automatically detecting that rule in live speech**.
- Audio examples should come from properly licensed and teacher-verified recordings.
- AI feedback must be limited to the exact rule/model/version that has been evaluated.
- Provide practice examples, revision quizzes, and “listen again” controls.
- A human-teacher review or referral option can be added later; do not claim the app replaces a qualified Quran teacher.

### F. AI live recitation correction

**Primary learning goal:** As a user reads a selected ayah, the app listens in real time, identifies likely word-level issues, gives concise feedback, and lets the user retry or continue.

Possible issue categories:

- A word may have been omitted.
- A word may have been substituted.
- An extra word may have been spoken.
- The recitation may be too unclear to assess.
- A supported pronunciation or tajweed feature may need practice.
- Recitation progress may be aligned to the selected verse.

Recommended interaction sequence:

1. User chooses a Surah/ayah range and a mode (offline AI or online AI if configured).
2. App explains that it is listening and obtains microphone permission.
3. Audio is processed by the selected model; the app compares recognition/alignment results with the canonical selected text.
4. The app displays a likely issue only if it passes calibrated confidence rules.
5. Offer the correct reference audio for the relevant word/phrase, if a verified audio segment is available.
6. Offer **Try Again**, **Hear Again**, **Continue Anyway**, and **Stop**.
7. Store the practice result locally only if appropriate and explain what is saved.
8. On low confidence, background noise, unsupported model, or a mismatch between selected text and recitation, request another attempt instead of accusing the user of an error.

Technical guardrails:

- Do not send raw audio to Gemini text generation and treat its opinion as canonical Quranic error detection.
- Use a Quran-specific speech-recognition/alignment system for word/phoneme recognition. A candidate such as a Quran recitation model with ONNX/mobile support may be evaluated, but its current model licence, Android runtime, model quality, memory/CPU needs, and permitted commercial use must be checked before shipping.
- Use a canonical verse text to align recognized phonemes/words; never allow an LLM to rewrite the source Quran text.
- Low-confidence results must not block the learner from continuing.
- Track and show the model version in internal diagnostics so quality regressions can be tested.
- Test multiple reciters, accents, age groups, recitation speeds, device microphones, quiet/noisy settings, and different phone performance classes with suitable consent and privacy protection.

### G. Hifz and memorization

- Hide/reveal ayah text for recall tests.
- Repeat a selected ayah or ayah range.
- New lesson, previous lesson (sabqi), and longer revision/manzil workflows.
- Hint options such as reveal first word or show a small portion of the text.
- Hifz test by speaking when a suitable on-device model is available.
- Error/mistake notebook tied to verified Surah/ayah references.
- Local revision schedule, goals, streaks, and progress summaries.
- “Review my weak sections” based only on locally saved practice information.
- No shame-based scoring or rigid ranking. Let the user pause or adjust goals.

### H. AI Quran learning assistant (online and optional local)

Allowed high-value tasks:

- Explain the meaning of an approved translation in accessible Urdu or English.
- Explain a learning term or a teacher-approved tajweed lesson.
- Suggest a study routine based on user-selected goals.
- Help the user navigate the app and find relevant verified content.
- Summarize the topic of a Surah only when grounded in an approved source and clearly labelled as a summary.
- Answer questions by quoting or referencing approved, locally available content and stating the source.
- If no verified source is available, say so, and suggest a trusted source rather than inventing an answer.

Online AI must follow these constraints:

- Use Gemini through the Cloudflare Worker, never call Gemini using a secret embedded in the APK.
- Use structured request/response schemas and a strict system instruction requiring verified source references.
- Do not generate new Quranic verses, edit Arabic Quran text, make up hadith citations, or fabricate tafsir references.
- Do not treat an LLM as a Quran text source or an authority for recitation correctness.
- Show an AI-generated label where appropriate.
- Provide a feedback/report button for incorrect or concerning answers.
- Apply prompt-injection resistance: retrieved content is reference data, not instructions to override the app's safety and accuracy rules.

### I. Personal learning and progress

- Reading minutes, ayahs read, Surahs visited, lessons completed, audio sessions, and Hifz revisions.
- Daily and weekly goals stored locally.
- Optional local reminders/notifications set by the user.
- Local streaks without punitive messaging.
- Export/restore a user-owned backup of bookmarks and progress (exclude private voice recordings by default).
- Optional future cloud backup, explicitly opt-in. It is not required for the initial release.
- Clear “reset my learning data” action.

### J. Accessibility, safety and family-friendly features

- Large type, high contrast, screen reader support, RTL/LTR correctness, reduced motion, and simple mode.
- Microphone permission requested only when needed, with a clear reason.
- Audio recording defaults to transient processing; no upload or storage without clear consent for a feature that requires it.
- If online voice processing is added, disclose that audio leaves the device, its purpose, provider, retention behaviour, and how to disable it. Prefer not to retain raw audio.
- Optional child-friendly UI, but do not add behavioural tracking or ads targeted at children.
- Avoid intrusive advertisements during reading, listening, and practice.
- Clear source, licence, privacy, AI limitation and help screens.

### K. Optional later enhancements

These must not delay the first usable release:

- On-device small language model for basic offline educational answers, grounded in bundled verified content.
- More interface languages (Arabic, Indonesian, Bengali, Turkish, etc.) after Urdu and English quality is strong.
- A teacher-reviewed learning curriculum, teacher mode, parent-guided lesson list, and classroom group codes.
- Optional account/cloud-sync features with strong privacy safeguards.
- Optional prayer-time and Qibla utilities, clearly separate from the Quran reader and using appropriate permission/data sources.
- Accessibility-focused simplified reading mode and low-bandwidth update downloads.
- Optional PWA/website for public information and content help, without replacing the native Android app.

---

## 6. Online vs Offline Feature Matrix

| Feature | Offline | Online | Rules |
|---|---:|---:|---|
| Bundled verified Quran text | Yes | Update check only | App reader never requires network |
| Installed translations/word meanings | Yes | Download/update | Publisher, edition and licence recorded |
| Bookmarks, notes, last-read | Yes | Optional future backup | Local-first, no account required |
| Search installed content | Yes | Optional remote discovery later | Local search works in airplane mode |
| Downloaded Qari audio | Yes | Download/update | Respect audio permissions/licence |
| Lessons and quizzes installed | Yes | Download/update | Core learning remains local |
| Hifz progress and reminders | Yes | Optional sync later | Store locally by default |
| AI word-level correction | Yes, when compatible model is installed | Optional cloud engine only if validated | Never fake model output |
| AI tajweed assessment | Only for supported validated rules | Optional approved service | Do not claim universal detection |
| AI learning explainer | If a suitable local model and content pack exist | Yes through Worker + Gemini | Ground answers and cite sources |
| Cloud content catalogue | Cached basics only | Yes | Reader works if endpoint is down |
| New audio/model/content packs | No initial download | Yes | Resume/retry and show storage required |
| User login | Not required | Not required for core features | Any future account is optional |
| Cloud backup | Local export remains possible | Optional | Explicit user consent |
| App/API updates | Current installed version works | Yes | Updates never delete user progress |

---

## 7. Offline Content and Local Storage Strategy

### Local database

Use Room for structured local data. Proposed entities:

- `SurahEntity`
- `AyahEntity`
- `WordEntity` (only if word-level data is licensed/available)
- `TranslationEntity`
- `TafsirReferenceEntity` (metadata and/or properly licensed content)
- `JuzBoundaryEntity`, `NavigationBoundaryEntity`
- `BookmarkEntity`
- `RecentReadingEntity`
- `ReadingSessionEntity`
- `LessonEntity`, `LessonProgressEntity`, `QuizAttemptEntity`
- `HifzPlanEntity`, `RevisionItemEntity`
- `DownloadPackEntity`
- `LocalSettingsEntity`
- `RecitationPracticeSummaryEntity` (summary only unless voice storage is explicitly requested)

Keep downloaded audio, fonts, and model files in app-private files/cache directories with metadata in Room. Do not store audio blobs as database rows.

### First-run strategy

The initial install must provide a genuinely usable baseline offline package:

- Quran text and core navigation metadata.
- At least one translation only if its licence allows bundling; otherwise provide an approved first-run download flow and accurately explain what is available offline.
- Bookmarks, progress storage, and starter lessons.
- Core UI fonts and required Quran font assets with appropriate licence.
- Offline Quran reader, local search, and settings.

For large audio, additional translations, tafsir, and AI model files, use selectable download packs. Make it clear which packs are present and which need a one-time download.

### Content integrity and update flow

- Every content pack must record its source, language, publisher/translator, version, licence, content checksum, size, and update timestamp.
- Verify downloaded pack hash/checksum before installing it.
- Use atomic replacement/migrations; do not leave the app with half a Quran pack if a download fails.
- Preserve bookmarks/progress across content updates using stable Surah/ayah identifiers.
- Never silently modify a canonical Quran text pack.
- If using the Quran Foundation Content Sync service, follow its current developer terms and designated sync workflow for resources that require it; do not build offline copies from ordinary API responses when the provider says Content Sync is the required path. Re-check sync/update obligations against the current terms.
- Content source/license changes must be documented in `CONTENT_SOURCES.md`.

---

## 8. Technical Architecture — Android

### Recommended stack

- Kotlin.
- Jetpack Compose + Material 3.
- Single-activity application with clear feature packages.
- ViewModel + StateFlow for screen state.
- Kotlin Coroutines and Flow.
- Room for local structured data.
- DataStore for preferences.
- WorkManager for non-urgent content download/sync and retryable background work.
- Android Media3 / ExoPlayer for playback and media controls where appropriate.
- Retrofit or Ktor Client for HTTPS calls to the Cloudflare Worker.
- Kotlin serialization or Moshi for strict typed JSON.
- Dependency injection can start with a simple manual container; use Hilt/Koin only if the generated project can add and test it reliably.
- On-device ML runtime behind an interface such as `RecitationRecognitionEngine`; choose a compatible ONNX/mobile runtime only after validating actual model files, licence, model size, supported ABIs, CPU/RAM, latency and accuracy.

### Architecture principles

- Presentation/UI layer: Compose screens and reusable components.
- Domain layer: use cases for reading, bookmark management, downloads, revision plans, and recitation sessions.
- Data layer: Room repositories, content-pack repositories, audio/download repository, settings repository, and remote API adapter.
- AI layer: interfaces for local recitation recognition, optional online-recitation provider, confidence calibration, word alignment, and Gemini education API client.
- Network layer: a small API client that knows only the Cloudflare Worker base URL and typed routes.
- Never let screen composables directly access the database, Gemini API, or file downloads.
- Make local Room data the source of truth for reading progress and installed content.
- Network data updates local content via controlled sync; network errors must not erase cached data.

### Suggested package layout

```text
app/src/main/java/<package>/
  app/                 # Application, single Activity, NavHost
  core/designsystem/   # Color, typography, components, icons
  core/network/        # WorkerApiClient, DTOs, connectivity status
  core/database/       # Room database, DAO, migrations
  core/files/          # Pack manager, checksum and storage helpers
  core/audio/          # Media3 player, audio session and notifications
  core/ai/             # AI interfaces, model status, confidence rules
  data/quran/          # Quran local data sources and repositories
  data/translation/    # Translation and word-by-word sources
  data/learning/       # Lesson, quiz, hifz and progress repositories
  domain/              # Shared use cases and domain models
  feature/home/        # Home screen
  feature/quran/       # Quran reader, search, bookmarks
  feature/audio/       # Recitation library and player
  feature/learn/       # Qaida and tajweed lessons
  feature/ai_teacher/  # Live recitation workflow
  feature/assistant/   # AI learning assistant
  feature/hifz/        # Hifz and revision
  feature/progress/    # Goals and statistics
  feature/downloads/   # Pack manager
  feature/settings/    # Language, theme, privacy, accessibility
```

AI Studio may generate a single-module app. Keep the logical boundaries above within that module if multi-module configuration is not supported in the chosen builder. Avoid creating unnecessary modules merely to imitate this diagram.

---

## 9. AI Design: Assign the Right Job to Each Model

Do not treat all AI tasks as one generic Gemini prompt. Use separate components for different responsibilities.

### Local Quran recitation engine

Responsibilities:

- Streaming audio feature extraction / phoneme recognition using a suitable, validated Quran-specific model.
- Alignment of recognized output to the user-selected canonical ayah.
- Candidate word/phoneme issue detection.
- Local confidence scoring and quality/status signals.
- Optional on-device inference with no data leaving the device.

Requirements:

- Model licence explicitly permits the intended distribution and use.
- Verify Android artefacts and runtime support rather than assuming that an iOS package or research checkpoint automatically works on Android.
- Verify performance on low-, mid-, and high-range devices.
- Offer a model download only from an approved, integrity-checked source.
- Provide `ModelStatus`: not installed, downloading, ready, incompatible, failed, or update available.

### Cloudflare Worker + Gemini education assistant

Responsibilities:

- Learning explanations tied to verified reference content.
- Simple language explanation of an existing, approved translation.
- Tajweed lesson Q&A for teacher-reviewed material.
- Learning-plan suggestions and app help.
- Structured response with references and uncertainty markers.

Not responsible for:

- Generating Quranic Arabic text.
- Replacing the official Quran source.
- Being the sole judge of a user's tajweed or Quran pronunciation.
- Inventing a translation or tafsir.
- Making unreferenced religious rulings.

### Optional online recitation service

Add only if a separate Quran-specific cloud recognition/alignment provider is selected and evaluated. It must be a replaceable adapter, disabled by default until its data handling, cost, latency, accuracy, and retention policy are known. If voice leaves the device, obtain clear consent and provide a local/offline alternative where available.

### Unified AI interface

Create interfaces such as:

- `RecitationRecognitionEngine`
- `AyahAlignmentService`
- `RecitationFeedbackPolicy`
- `LearningAssistantRepository`
- `OnlineAiAvailabilityProvider`
- `ModelPackageRepository`

This keeps app features stable if the chosen model/provider changes.

---

## 10. Cloudflare Backend and API-Key Security

### Recommended backend design

Create a separate TypeScript Cloudflare Worker project, for example `quran-learning-api`.

Suggested repository structure:

```text
cloudflare-worker/
  src/index.ts
  src/routes/health.ts
  src/routes/learning-assistant.ts
  src/routes/content-metadata.ts
  src/services/gemini.ts
  src/services/content-provider.ts
  src/validation/schemas.ts
  src/security/rate-limit.ts
  wrangler.jsonc
  package.json
  tsconfig.json
  .gitignore
  README.md
```

### Initial Worker routes

- `GET /v1/health` — non-sensitive service status, API version, and availability only.
- `GET /v1/config` — safe public config such as supported features, minimum app version, and available pack metadata. No secrets.
- `POST /v1/assistant/ask` — a constrained educational assistant request with selected user language, question, and optional references/IDs to approved content.
- `POST /v1/assistant/explain-translation` — explain an existing translation supplied by stable content ID/reference, not generate a replacement translation.
- `POST /v1/assistant/study-plan` — create a general study plan based on user-selected goals and schedule.
- Optional `GET /v1/packs` — list approved audio/content/model pack metadata.
- Optional `GET /v1/packs/:id/manifest` — return version, size, checksum, licence, and download source for a validated pack.

Do not implement an unrestricted `/proxy?url=` endpoint or an endpoint that accepts arbitrary provider URLs. Do not accept arbitrary prompts, enormous payloads, or user-supplied model names without validation. Keep routes narrow and typed.

### Gemini API key setup

1. Create a Gemini API key in Google AI Studio/Google AI for Developers.
2. In the Cloudflare Worker project, store it as a secret, not a normal public variable:

   ```bash
   npx wrangler secret put GEMINI_API_KEY
   ```

3. In Worker code, read the secret from the Worker `env` binding. Never return the value to the client, write it to logs, or include it in a JSON response.
4. Use the Gemini API from the Worker with `GEMINI_API_KEY` only on the server side.
5. Keep local development secrets in an ignored `.dev.vars` file if needed. Add `.dev.vars*`, `.env*` (except a documented `.env.example` with placeholders), local build outputs, and signing files to `.gitignore`.
6. Rotate a key immediately if exposure is suspected. Configure usage/billing alerts and quotas in the Google AI project where available.

### Android app configuration

- The Android app may contain the public Cloudflare Worker base URL; that is not a secret.
- Keep URLs/environment selection in a clear configuration object or build-time non-secret config.
- Do not add `GEMINI_API_KEY`, provider credentials, or another secret to `BuildConfig`, `strings.xml`, assets, remote config, or the APK.
- Never ask the user to paste their production API key into a public prompt, repository, issue, or generated source file.
- The Cloudflare Worker must be the only place that uses the Gemini key for the online assistant.

### Security protections

- Validate all input with a schema and reject malformed requests.
- Cap prompt length, response tokens, request body size, processing time, and supported actions.
- Use rate limits, abuse monitoring, and per-route usage budgets. Because a no-login app has anonymous traffic, do not assume a client-generated device ID is strong authentication.
- Consider stronger app/device attestation and/or signed short-lived anonymous sessions before expanding expensive AI features; do not add mandatory accounts just to avoid designing rate limits.
- Restrict CORS to any companion web origins that actually need access. Native Android apps do not use browser CORS in the same way, so CORS is not a substitute for server-side abuse protection.
- Do not log raw audio, complete personal questions, API keys, or unnecessary stable identifiers. Log request IDs, status, latency, provider error categories, and aggregated usage where possible.
- Set safe timeout and failure handling. Return helpful, non-sensitive error messages.
- Add an explicit service status and graceful offline fallback.
- Ensure the Worker cannot be abused as an open Gemini proxy.

### Optional Cloudflare resources

- R2 for versioned packs, large downloads, and manifests after licence review. Use checksums, explicit versions, resumable/retryable downloads where feasible, and no secrets in public object metadata.
- D1 for optional public content metadata or aggregate app operational data only if necessary.
- Pages for a static landing page, help, terms, privacy notice, AI limitations, and a content-source directory.
- Use separate development and production Worker environments and separate provider keys where practical.

### Important Cloudflare limitation

Do not assume Workers Free/Paid quotas are unlimited or constant. Check the current Cloudflare plan, request/body-size/CPU limits, rate-limiting behaviour, R2 pricing and egress/storage details before release. A speech-recognition model should not be executed inside a normal Worker merely because Workers can call external APIs.

---

## 11. Data Contracts and API Response Rules

Use typed request/response DTOs. A response should not be a free-form text blob when a stable schema is possible.

Example assistant response structure:

```json
{
  "requestId": "server-generated-id",
  "status": "ok",
  "language": "ur",
  "answer": "User-facing learning explanation.",
  "references": [
    {
      "type": "translation_or_lesson",
      "sourceName": "Verified source name",
      "reference": "Stable content/verse reference"
    }
  ],
  "isAiGeneratedExplanation": true,
  "confidence": "limited",
  "disclaimer": "This explanation supports learning and is not a substitute for qualified scholarship."
}
```

This is a shape example, not a claim that a source or reference already exists. Only include references retrieved from real, approved data. Use a server-generated request ID, strict language enum, allowed input size, and controlled output schema. Parse failures must be handled safely in the app.

Example content pack manifest:

```json
{
  "packId": "qari-or-content-pack-id",
  "version": "version-string",
  "sizeBytes": 0,
  "sha256": "verified-checksum",
  "licenseUrl": "https://example.invalid/license",
  "downloadUrl": "approved-download-location"
}
```

The example values above are placeholders. Production manifests must use actual validated metadata and real URLs; never ship the `.invalid` address as an active link.

---

## 12. Build, Test and Deployment Workflow

### Android project workflow

1. Open Google AI Studio Build mode.
2. Select **Android**, not Web.
3. Paste the Android app master prompt from Section 17.
4. Generate the initial native Kotlin + Jetpack Compose app with UI, navigation, local data layer, sample-safe placeholders, and no embedded secrets.
5. Inspect the generated code and run the available emulator/preview.
6. Test on a real physical Android phone, especially microphone permission, audio playback, storage, background playback and local AI inference.
7. Download/export the Android ZIP and open/build it in Android Studio as required.
8. Add the real, properly licensed Quran content and verified content pipeline.
9. Add the Cloudflare Worker base URL as public configuration only.
10. Build debug/release variants, run automated tests, and verify release signing and Play policy requirements.

### Cloudflare Worker workflow

1. Create a separate TypeScript Worker project.
2. Add narrow routes, validation, error handling, quota/limits, and Gemini service adapter.
3. Add `GEMINI_API_KEY` via `wrangler secret put GEMINI_API_KEY` or the Cloudflare Dashboard secret editor.
4. Run local tests without committed production keys.
5. Deploy the Worker and verify `/v1/health`.
6. Test invalid input, oversized input, quota limit, provider outage, and request timeout behaviour.
7. Configure a stable HTTPS URL and any required custom domain.
8. Add monitoring and a documented key-rotation process.
9. Use a separate Worker/key for staging and production when feasible.

### Android publishing

- Use Google Play Console for production distribution where appropriate.
- Maintain version codes, release notes, privacy policy, data-safety disclosure, AI/microphone disclosure, and a support contact.
- Cloudflare Pages may host the privacy policy and public help pages, but it does not replace Android app signing or Play publishing.
- Never claim the app is “fully offline” unless the release passes an airplane-mode acceptance test for every feature labelled offline.

---

## 13. Quality Assurance and Acceptance Criteria

### Quran content integrity

- Verify all 114 Surahs and every ayah against the chosen authoritative source.
- Verify boundaries and counts, diacritics, script differences, page mappings, special markers and any imported word-level content.
- Automated tests must detect missing/duplicate ayah IDs, unexpected count changes, broken font glyphs, RTL layout errors, and content-checksum mismatches.
- No AI-generated Quran text in source content files.

### Offline acceptance

With airplane mode enabled and all designated core packs installed, confirm:

- App opens and navigates normally.
- Quran reader opens immediately and switches Surahs/Juz/ayahs.
- Installed translations and word meanings work.
- Search works over installed data.
- Bookmarks, last-read position, notes, goals, lessons and Hifz progress can be created/updated.
- Downloaded audio plays and repeats.
- Local reminders/settings remain available.
- Local recitation AI works only if a compatible model is installed and device passes capability checks.
- No endless loading spinner, repeated network error, crash, or fake AI result appears when offline.

### Online acceptance

- Worker health endpoint works.
- Correctly shaped learning-assistant request works with server-side key.
- No API key exists in APK, source repository, generated UI strings, build logs, or server responses.
- Invalid/oversized requests are rejected.
- Rate limiting and budgets are tested.
- Gemini errors/timeouts map to user-friendly fallback messages.
- Online-only tools clearly show when internet is required.
- Offline reading remains usable during API downtime.

### AI recitation acceptance

- Test correct recitation, omitted words, substitutions, extra words, pauses, self-corrections, background noise, low microphone quality, fast/slow recitation, and unsupported audio conditions.
- Measure false positives (correct recitation incorrectly flagged), false negatives (real errors missed), latency, battery drain, heat, memory, and crash rate.
- Have qualified Quran teachers define the rubric and review representative outputs.
- Use confidence thresholds calibrated from an evaluation set; never hard-code a confidence threshold without measurements.
- Low-confidence results request repetition instead of false correction.
- User can stop listening at any time; no audio continues unexpectedly in the background.
- Do not describe an experimental model as a certified or complete tajweed teacher.

### Usability, privacy, and device coverage

- Test small-screen, low-memory, mid-range and current devices; large font settings; portrait/landscape; dark mode; Arabic/Urdu RTL; English LTR; talkback/screen reader; permission denied; insufficient storage; download failure; and app upgrade.
- Check data-use disclosures and verify whether any audio or question is sent online.
- Verify no private recitation audio is stored by default.
- Verify the user can remove downloads and clear local app data/progress.
- Ensure app does not require an account for core learning.

---

## 14. Implementation Roadmap (Build in Phases)

Do not attempt to implement every complex subsystem in one generation prompt. Use these phases and test each deliverable before continuing.

### Phase 0 — Discovery and source approval

- Decide initial UI languages (Urdu + English recommended).
- Select canonical Quran source, script(s), translation(s), tafsir source(s), fonts, Qari audio source and Qaida curriculum.
- Verify licences and any offline caching/sync conditions.
- Define Android minimum supported version and representative device tiers.
- Approve privacy and audio-processing rules.
- Evaluate one Quran-specific local recitation model for model licence, Android runtime, accuracy, storage and performance.

**Exit criteria:** `CONTENT_SOURCES.md`, data schema, core feature scope, and model evaluation checklist approved.

### Phase 1 — UX/UI prototype and native foundation

- Generate Kotlin + Jetpack Compose project.
- Implement theme, navigation, onboarding/skip, Home, Quran reader shell, Learn, AI Teacher shell, Progress, Settings, Downloads and About/privacy.
- Include UI states for offline/online, loading, empty/error, permission denied, and model unavailable.
- No false production content and no fake AI behaviour.

**Exit criteria:** navigation is usable; theme is consistent; compile/build passes; app can open offline.

### Phase 2 — Quran core and local data

- Integrate canonical Quran content and metadata.
- Build Room schema/repositories/migrations.
- Implement Surah/Juz/ayah/page navigation, bookmarks, recent reading, local search, font and theme settings.
- Add data integrity scripts/tests and source attribution.

**Exit criteria:** complete Quran is verified and usable in airplane mode.

### Phase 3 — Translation, audio, and content packs

- Add selectable verified translations and word-by-word content.
- Add licensed Qari audio, player, repeat/loop, background controls and download manager.
- Add pack manifests, checksums, storage checks and safe update/migration flow.

**Exit criteria:** installed translation/audio packs work offline and downloads fail safely.

### Phase 4 — Qaida, Tajweed lessons, and quizzes

- Implement beginner-to-advanced learning paths.
- Store lessons, examples, quiz results and progress locally.
- Use teacher-reviewed material and licensed audio.

**Exit criteria:** lesson workflows remain fully usable offline.

### Phase 5 — Hifz, revision, and progress

- Add hidden text, recall tests, local revision plan, weak-section notes, daily goals and non-punitive progress dashboard.

**Exit criteria:** progress persists across app restart and upgrades.

### Phase 6 — On-device AI recitation proof of concept

- Integrate selected local model through an abstraction/interface.
- Implement microphone capture, streaming inference, text alignment, candidate issue handling, confidence thresholding, replay and continue controls.
- Benchmark supported devices and document the model licence.

**Exit criteria:** defined evaluation corpus and qualified review show acceptable performance for the supported features. If not, keep it clearly experimental and do not claim robust live correction.

### Phase 7 — Cloudflare Worker and Gemini assistant

- Create a separate Worker project and typed API endpoints.
- Add `GEMINI_API_KEY` as Worker secret.
- Implement content-grounded explanations, limits, monitoring, timeouts, safe errors and tests.
- Connect Android through the public Worker URL.

**Exit criteria:** no key in APK, endpoints are validated/rate-limited, and offline reader still works if Worker is down.

### Phase 8 — Optional online AI recitation and content sync

- Add only if a suitable provider is approved.
- Explicit consent for transmitting audio, documented retention and costs, network-state handling, and online/offline choice.
- Implement approved content-pack update process.

**Exit criteria:** privacy, accuracy, licensing, quota, and failure behaviour are verified.

### Phase 9 — Accessibility, performance, security, and release

- Run offline/online acceptance tests, Quran integrity checks, accessibility review, API security tests, performance/battery tests, Play release checklist, privacy policy and content attributions.

**Exit criteria:** no critical defects, no fake AI claims, app works offline as labelled, and keys/content are handled safely.

---

## 15. Definition of Done

A release is not complete merely because the app looks good or compiles. It is complete only when:

- The Android app is native Kotlin + Jetpack Compose.
- Core navigation, Quran reader, lessons and progress work without mandatory sign-in.
- The installed Quran package is complete and verified.
- Offline functionality has been tested in airplane mode.
- Content sources, licences and attributions are documented.
- Downloads have integrity checks and safe failures.
- AI capabilities are split by task; the LLM does not invent or edit Quran text.
- Live correction is confidence-aware, testable, and honest about limitations.
- Gemini keys exist only as server-side Cloudflare secrets.
- Worker inputs are validated and expensive endpoints are protected by sensible limits.
- Privacy and microphone behaviour are transparent.
- App responds gracefully if the network, model, audio, storage, or API is unavailable.
- The repository contains setup instructions, test commands, environment examples, and deployment instructions.

---

## 16. Copy-Paste Prompt for Google AI Studio (Native Android App)

> **Use this prompt in Google AI Studio Build mode with Android selected.** The Android project and Cloudflare Worker are separate deliverables. Do not ask the Android builder to embed a server or secret in the APK.

```text
Build a native Android application named “Quran Learning Companion” using Kotlin and Jetpack Compose with Material 3. This is a phone-first, offline-first Quran learning product. Do not build a web app, React app, PWA, or WebView wrapper. Keep a clean, maintainable Kotlin Android project using the supported single-activity architecture and clear internal package boundaries.

PRODUCT GOAL
Create one respectful Quran app for beginners, regular readers, tajweed learners, and people memorizing Quran. It must combine a complete verified Quran reader, optional Urdu/English translation, audio, lessons, Hifz/revision, local progress and a future-ready AI recitation experience. Core reading and learning must not require login or an internet connection once required content is installed.

VISUAL DESIGN
Use a premium, calm Islamic learning design, not a copy of another product. Palette: deep emerald #123C32, forest green #1E624D, soft jade #DDEDE4, warm ivory #FAF7EF, muted gold #C9A45C, charcoal #202522, and dark-mode #101713/#18231D. Use accessible contrast, restrained rounded cards, comfortable spacing, clear typography, subtle motion, and native Material 3 components. Use a properly licensed Arabic Quran font asset when one is supplied; never generate Quran text as a decorative image. Support Arabic and Urdu RTL layouts and English LTR layouts. Arabic ayah text must be large, legible, adjustable, and visually separate from translations.

NAVIGATION
Create bottom navigation for Home, Quran, Learn, AI Teacher, and Progress. Put Settings, Downloads, Privacy, Help, Content Sources, and About in the top-level menu/settings area. All main screens must be reachable, and navigation must work—not just look clickable.

SCREENS
1. Onboarding with language choice (Urdu/English), learning goal, reading script preference if supported, optional audio downloads, privacy explanation, and a Skip option. Never require account creation.
2. Home with Continue Reading, quick actions, daily goal, optional verified daily ayah, offline/online status, and downloads shortcut.
3. Quran reader with Surah/Juz/ayah navigation, verified Arabic text from a repository, optional Urdu/English translation, optional word-by-word section, bookmarks, local search, last-read position, font size, dark mode, audio controls, and separate AI practice controls.
4. Search screen for installed content.
5. Recitation library/player with play/pause, repeat, loop range, previous/next, available Qari packs, download state, and background playback support where implemented.
6. Learn screen with Arabic alphabet, Qaida, makharij, tajweed lessons, practice, quizzes, and local progress. Use clearly labelled sample/demo lesson data only until verified content is integrated; never present sample content as authoritative Quran material.
7. Hifz mode with hidden text, hints, repeat practice, new lesson/sabqi/manzil concepts, local revision planning, and mistake notes tied to stable ayah IDs.
8. AI Teacher screen with microphone permission, listening state, model status (not installed/downloading/ready/incompatible/failed), word-level issue placeholders backed by an interface, confidence-aware feedback, Hear Again, Try Again, Continue Anyway, Pause, and Stop. Do not simulate real AI results. If the model is not integrated, show an explicit “AI model not installed/configured” state and keep other features useful.
9. AI learning assistant screen for online, source-grounded learning explanations. Show offline/unavailable state when Cloudflare Worker is unreachable; never block Quran reading.
10. Progress screen with locally stored reading, lesson and Hifz progress. Use supportive, non-punitive language.
11. Downloads screen for content/audio/model packs with size, progress, checksum status, retry and delete actions.
12. Settings for language, script, translation, theme, accessibility, privacy, microphone explanation, local data reset, notification reminder, and optional backup/export.
13. About, Content Sources and Privacy pages.

LOCAL DATA AND ARCHITECTURE
Use Room for structured Quran content, translations, bookmarks, history, lessons, quiz attempts, Hifz progress, and download metadata. Use DataStore for preferences. Use ViewModels, StateFlow, coroutines, repositories, and a unidirectional UI state flow. Keep UI separate from storage, AI, audio, and networking. Create sensible interfaces including QuranRepository, TranslationRepository, AudioRepository, DownloadPackRepository, RecitationRecognitionEngine, LearningAssistantRepository, and WorkerApiClient. Use WorkManager for non-urgent content download/update work where suitable. Use Media3/ExoPlayer if supported and appropriate for playback.

OFFLINE-FIRST BEHAVIOUR
App launch and the Quran reader must work without internet when local content exists. Do not call the network merely to display locally stored verses. Bookmarks, recent reading, progress, settings, lessons already installed, search over installed content, and downloaded audio must work offline. Network errors must never delete local data. Provide clear empty states when a translation/audio/model pack has not been installed. Implement airplane-mode handling and graceful retry. Do not claim a content pack is installed unless the files and database records are actually present and verified.

QURAN CONTENT INTEGRITY
Do not invent or generate Quranic Arabic, translations, tafsir, hadith, verse numbers, or audio. Implement source adapters and database schemas that can accept verified, licensed data. Track source, edition, licence, version, and content checksum. Keep canonical Quran text immutable. Any temporary placeholder/sample dataset must be marked as demo-only and cannot be described as a complete Quran. Never use AI-generated images to represent Quran pages or calligraphy.

AI POLICY
Use a Quran-specific local speech-recognition/alignment model for recitation correction only after actual integration, Android compatibility and licence review. A general LLM must not be the canonical judge of Quranic pronunciation. Do not invent AI detections or use random/mock corrections. Low-confidence audio must trigger “Please repeat this part” rather than a false error. Give users Try Again, Hear Reference Audio, Continue Anyway, and Stop options. Keep raw audio transient and private by default. Use Gemini only through a Cloudflare Worker API; do not add a Gemini API key to Android source, assets, resources, BuildConfig, APK or AAB.

CLOUDFLARE CONNECTION
Create a WorkerApiClient that accepts a public HTTPS base URL through non-secret configuration (placeholder such as https://YOUR-WORKER-DOMAIN.example). Implement typed DTOs for GET /v1/health, GET /v1/config, POST /v1/assistant/ask, POST /v1/assistant/explain-translation, and POST /v1/assistant/study-plan. Use safe timeouts, structured response parsing, cancellation, and user-friendly failure states. Do not implement an open proxy or arbitrary endpoint URL. Do not add any real API key. If backend endpoints are not live yet, show a clear unconfigured state rather than returning a fake AI answer.

PRIVACY AND ACCESSIBILITY
Ask for microphone permission only when the user starts a relevant feature and explain why. Show a clear recording/listening indicator and a stop button. Do not store or upload audio by default. Do not require sign-in. Implement screen reader labels, scalable text, accessible touch targets, high contrast, reduced motion, RTL/LTR correctness, and permission-denied states.

QUALITY REQUIREMENTS
Generate complete functional navigation and useful empty/loading/error states. Avoid giant single-file code and duplicated business logic. No dead buttons. Do not claim a feature is complete when it is a placeholder. Add unit tests for repositories/use cases and UI tests for primary navigation where supported. Ensure the project builds. Explain exactly which features are implemented, which require verified content packages, which need a local AI model, and which need the separate Cloudflare Worker.

FIRST MILESTONE
Prioritize a beautiful, compiling native app foundation, functional navigation, Room/DataStore architecture, Quran reader data contracts, settings, offline states, downloads UI, and interfaces for real content/AI integrations. Do not pretend that the complete Quran, audio catalogue, or AI model has been populated unless real licensed assets have actually been included.
```

---

## 17. Copy-Paste Prompt for the Separate Cloudflare Worker Backend

> Use this in a separate code-generation session or repository. Do not combine Worker runtime code into the Android module.

```text
Create a production-minded TypeScript Cloudflare Worker project named quran-learning-api for a native Kotlin Android Quran learning app. This is a backend/API project—not a web UI and not an Android app.

ARCHITECTURE
- Use Cloudflare Workers with TypeScript and Wrangler.
- Configure a public HTTPS API with versioned routes under /v1.
- Store GEMINI_API_KEY as a Cloudflare Worker secret. Read it only from the Worker env binding. Never hardcode it, commit it, return it to clients, or log it.
- Use Google Gemini only for constrained educational assistance grounded in verified reference content. Do not use Gemini as the canonical Quran text source or sole judge of Quran recitation correctness.
- Keep the Worker independent of any particular Android UI framework.

ROUTES
- GET /v1/health
- GET /v1/config
- POST /v1/assistant/ask
- POST /v1/assistant/explain-translation
- POST /v1/assistant/study-plan
- Optional GET /v1/packs if a real, approved content-pack manifest source is configured.

IMPLEMENTATION REQUIREMENTS
- Create modular route handlers, Gemini service adapter, schema validation, consistent JSON responses, request IDs, error mapping, safe timeouts, and tests.
- Use strict allowlisted request schemas and maximum input/output/token limits.
- Do not create an arbitrary URL proxy. Do not accept arbitrary Gemini model names or raw provider requests from the client.
- If no verified Quran content/reference is available for an answer, return a clear insufficient-reference response. Do not fabricate Quran text, translations, tafsir or hadith citations.
- Put rate limiting/abuse-protection hooks on expensive routes. A no-login public API requires abuse safeguards; client-generated IDs alone are not authentication.
- Never log API keys, raw audio, complete sensitive questions, or unnecessary personal identifiers.
- Provide CORS only for explicitly allowed browser origins if a web client actually needs it; don't treat CORS as authentication.
- Add health checks, useful logs without secrets, safe error responses, and environment separation for development/staging/production.
- Include wrangler.jsonc, package.json, tsconfig.json, .gitignore, README, .dev.vars.example with placeholders only, unit/route tests, local development instructions, and deployment instructions.
- Document how to set the secret with: npx wrangler secret put GEMINI_API_KEY
- Do not output a real API key. Do not deploy or claim to deploy the project unless actual credentials and deployment results are available.

SECURITY AND QUOTAS
- Validate request bodies, cap size, set per-route cost budgets, protect against automated abuse, and include configurable limits.
- Avoid open-proxy behaviour. Add a TODO/decision point for stronger anonymous session or app attestation as traffic grows.
- Ensure online AI failure never affects offline Quran reading in the Android client.

DELIVERABLE
Return the complete Worker source tree and README. Clearly separate working functionality from provider/content integration still requiring credentials, approved data, or production configuration. Include tests for valid request, invalid schema, too-large request, missing key, provider timeout, provider error, and rate-limit handling.
```

---

## 18. Production Launch Checklist

- [ ] Product name, package ID, icon, store listing, and support contact confirmed.
- [ ] UI and Quran font licences checked.
- [ ] Complete Quran source, script(s), text checksums and ayah counts independently verified.
- [ ] Translation, word-by-word, tafsir, Qaida, and recitation licences documented.
- [ ] Offline install and airplane-mode tests pass.
- [ ] Downloads verify size/checksum and recover safely from interrupted transfers.
- [ ] AI recitation model licence, model format, Android runtime, device coverage, accuracy, and limitations documented.
- [ ] Teacher-reviewed recitation evaluation completed for every claimed correction feature.
- [ ] Gemini key stored only as Cloudflare Worker secret.
- [ ] Worker rate limits, request validation, timeouts, usage/billing alerts and monitoring configured.
- [ ] Privacy policy explains microphone permission, offline vs online processing, data retention, and deletion controls.
- [ ] No raw audio is stored by default.
- [ ] Accessibility, RTL/LTR, low-storage, low-memory, dark-mode, and screen-reader checks pass.
- [ ] Release signing and Google Play testing/review steps are completed.
- [ ] Cloudflare Worker deployment and Android Play Store deployment are described as separate processes.

---

## 19. Official and Technical References

These references should be re-checked during implementation because APIs, pricing, model licences, platform restrictions, and service limits can change.

1. Google AI Studio — Build Android apps with Kotlin and Jetpack Compose: https://ai.google.dev/gemini-api/docs/aistudio-android
2. Google AI Studio — Build mode overview and platform-specific limitations: https://ai.google.dev/gemini-api/docs/aistudio-build-mode
3. Google Gemini API key security: https://ai.google.dev/gemini-api/docs/api-key
4. Cloudflare Workers secrets: https://developers.cloudflare.com/workers/configuration/secrets/
5. Cloudflare Workers limits: https://developers.cloudflare.com/workers/platform/limits/
6. Cloudflare Workers rate limiting: https://developers.cloudflare.com/workers/runtime-apis/bindings/rate-limit/
7. Android architecture recommendations: https://developer.android.com/topic/architecture/recommendations
8. Quran Foundation Content Sync: https://api-docs.quran.foundation/docs/tutorials/content-sync/getting-started/
9. Quran Foundation API reference: https://api-docs.quran.com/docs/api-reference/
10. Tanzil text licence (only if using Tanzil): https://tanzil.net/docs/Text_License
11. Candidate Quran-specific on-device speech model catalogue (verify current licence and compatibility before production use): https://quranlab.ai/models

---

## Final Direction

Build the product around one simple rule: **the Quran reader and installed learning content always come first; online AI is an enhancement, never a dependency for reading.** Generate the Android app in Google AI Studio's Android mode, export it for normal Android engineering and release, and keep Cloudflare Worker code as a separate backend project. Keep Gemini API credentials server-side, use verified Quran content, and ship only the AI recitation capabilities that have passed licence, performance, and qualified-review checks.
