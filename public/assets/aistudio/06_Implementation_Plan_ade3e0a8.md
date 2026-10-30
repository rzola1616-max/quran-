# 06 — Implementation Plan

**Projects:** (1) User App · (2) Admin App · (3) Cloudflare Worker
**Principle:** build in small phases, test each exit criterion before moving on. Never fake content or AI results.
**Version:** 1.0 (draft) · **Date:** 29 Oct 2026

---

## 1. Workstreams & Repos

| # | Deliverable | Repo | Built with |
|---|---|---|---|
| 1 | Quran Learning Companion (User) | `quran-learning-companion` | Google AI Studio **Android** mode → export ZIP → Android Studio |
| 2 | Quran Learning Admin | `quran-learning-admin` | AI Studio Android mode (separate session) → Android Studio |
| 3 | Backend API | `quran-learning-api` | TypeScript + Wrangler (separate session/repo) |
| 4 | Docs/Static | `quran-learning-site` (optional) | Cloudflare Pages: privacy, terms, help, content sources |

Rules: AI Studio output is a client-side Android project only; it does not become the backend. Inspect generated code, test on **real phones** (mic, audio, storage, background playback, local AI).

## 2. Phase Overview

| Phase | Name | Main output | Apps affected |
|---|---|---|---|
| 0 | Discovery & approvals | Sources, licences, decisions | All |
| 1 | UX prototype + native foundation | Compiling apps with navigation/themes/states | User, Admin shell |
| 2 | Quran core + local data | Verified Quran offline | User |
| 3 | Translations, audio, packs | Download manager, pack pipeline | User + Worker + Admin |
| 4 | Qaida/Tajweed + quizzes | Lessons offline | User + Admin (review) |
| 5 | Hifz + progress | Revision, goals | User |
| 6 | On-device AI recitation PoC | Experimental engine | User |
| 7 | Worker + Gemini assistant | Online learning assistant | Worker + User |
| 8 | Admin app features complete | Packs, reports, config, audit | Admin + Worker |
| 9 | Optional online AI/content sync | Only if provider approved | All |
| 10 | Hardening + release | QA, accessibility, Play | All |

*(The master plan's Phases 0–9 are preserved; the Admin App work is slotted into Phases 1, 3, 4, 8.)*

---

## 3. Phase Details

### Phase 0 — Discovery & Source Approval
**Tasks**
- [ ] Confirm: Admin = Android app (not web).
- [ ] Choose UI languages (Urdu + English recommended).
- [ ] Select canonical Quran source, script(s), translation(s), tafsir, fonts, Qari audio, Qaida curriculum.
- [ ] Verify licences + offline caching/sync terms (incl. Quran Foundation Content Sync terms if used).
- [ ] Decide min Android API + device tiers; approve privacy/audio rules.
- [ ] Decide admin authentication method.
- [ ] Evaluate one local recitation model (licence, Android runtime, size, accuracy).
- [ ] Create `CONTENT_SOURCES.md`, finalize schemas (doc 05), package IDs, app names.

**Exit:** `CONTENT_SOURCES.md`, schema, feature scope, model evaluation checklist approved.

### Phase 1 — Native Foundation (both apps)
**User App tasks**
- [ ] Generate project via AI Studio with the master prompt (plan §16).
- [ ] Theme (colours, type, shapes), bottom nav (5 tabs), onboarding with Skip.
- [ ] Screens as shells: Home, Quran, Learn, AI Teacher, Progress, Settings, Downloads, About/Privacy/Content Sources.
- [ ] State UIs: offline/online, loading, empty, error, permission-denied, model-unavailable.
- [ ] Interfaces + stub implementations (clearly "unconfigured", **no fake AI**).
- [ ] Room + DataStore skeleton, ViewModels/StateFlow.

**Admin App tasks**
- [ ] Generate shell with Admin prompt (§6 below).
- [ ] Theme, nav, role-aware menu, sign-in screen (mock **only** in a debug build, flagged DEMO), dashboard/list/detail scaffolds.

**Exit:** both apps compile; navigation works; theme consistent; User app opens offline; no secrets in either project.

### Phase 2 — Quran Core + Local Data (User)
- [ ] Integrate canonical Quran content + navigation metadata via importer script (not typed by AI).
- [ ] Room schema (doc 05 Part 1), repositories, migrations, FTS search.
- [ ] Surah/Juz/page/ayah navigation, bookmarks, notes, recent, last-read, jump-to-ref.
- [ ] Font/theme settings, script selection if validated.
- [ ] Integrity tests: 114 surahs, ayah counts, no missing/duplicate IDs, checksum.

**Exit:** complete Quran verified; usable in airplane mode.

### Phase 3 — Translations, Audio, Content Packs
**User:** translation selector + attribution; word-by-word (if licensed); Media3 player (repeat/loop/range, background, notification); Download Manager (WorkManager, checksum, atomic install, resume/retry, delete, storage check).
**Worker:** `/v1/config`, `/v1/packs`, `/v1/packs/:id/manifest`, R2 + D1 setup, health.
**Admin:** Content Sources + Packs screens (create/version/submit/approve/publish/rollback) against Worker admin routes; admin auth working end-to-end.

**Exit:** installed translation/audio packs work offline; failed downloads roll back safely; a pack published in Admin appears in User app Downloads.

### Phase 4 — Qaida, Tajweed, Quizzes
- [ ] Lesson/quiz schema + local progress; Qaida path; tajweed rules (teacher-reviewed).
- [ ] Licensed audio examples; quizzes (sound match, letter match, repeat-after).
- [ ] Admin: Lesson Review queue (approve/request changes); only approved lessons enter published packs.
- [ ] Label demo content "Sample — not verified" until real.

**Exit:** lessons fully usable offline; review workflow functioning.

### Phase 5 — Hifz, Revision, Progress
- [ ] Hide/reveal, hints, new/sabqi/manzil, revision scheduler, mistake notebook, weak sections.
- [ ] Goals, streaks (non-punitive), progress dashboard, local reminders, backup/export/restore, reset.

**Exit:** progress persists across restart and app upgrade.

### Phase 6 — On-Device AI Recitation PoC (User)
- [ ] Integrate model via `RecitationRecognitionEngine`; mic capture, streaming inference, alignment to canonical ayah.
- [ ] `RecitationFeedbackPolicy` with thresholds **calibrated from an evaluation set**.
- [ ] UI: Hear Again / Try Again / Continue Anyway / Stop; model status flows; model download via verified pack.
- [ ] Benchmark low/mid/high devices; document licence.

**Exit:** evaluation corpus + qualified teacher review shows acceptable performance for the *specific supported features*; otherwise ship as clearly **experimental** or hold back.

### Phase 7 — Worker + Gemini Assistant
- [ ] Worker routes: `/v1/assistant/ask|explain-translation|study-plan`, validation, rate limits, timeouts, safe errors, request IDs.
- [ ] `npx wrangler secret put GEMINI_API_KEY` (staging + prod separate).
- [ ] Gemini adapter with strict system instruction (verified references only; insufficient-reference path; prompt-injection resistance).
- [ ] `/v1/reports` endpoint.
- [ ] User app: Assistant screen, AI label, references, Report button, unconfigured/offline states.

**Exit:** no key in APK/repo/logs; invalid/oversize/rate-limited requests handled; reader works while Worker is down.

### Phase 8 — Admin App Completion
- [ ] Dashboard metrics, Reports triage, Remote config + kill switch, Audit log, Admin users, Model catalogue.
- [ ] RBAC verified server-side (try forbidden calls per role).
- [ ] Audit entries for every mutation.

**Exit:** each role can only do what doc 05 allows; kill switch disables Assistant in User app via config.

### Phase 9 — Optional Online AI Recitation / Content Sync
Only if a provider is approved: explicit audio-transmission consent, documented retention/cost, local alternative preserved, replaceable adapter, disabled by default.

### Phase 10 — Hardening & Release
- [ ] Offline acceptance, online acceptance, AI acceptance, Quran integrity, accessibility (TalkBack/200% font/RTL), low-storage/low-memory, dark/high-contrast, API security tests, battery/heat.
- [ ] Privacy policy, data-safety form, mic/AI disclosure, content attributions on Pages.
- [ ] Release signing, versioning, Play internal → closed → production for User app; internal/closed track for Admin app.
- [ ] Worker prod deploy, monitoring, billing alerts, key-rotation runbook.

**Exit:** no critical defects; no fake AI claims; app is "offline" only where airplane-mode tested.

---

## 4. Suggested Milestones & Order

1. **M1** — Phase 0 approvals.
2. **M2** — Both app shells compile (Phase 1).
3. **M3** — Verified offline Quran (Phase 2) → *first internal User App build*.
4. **M4** — Packs + audio + Admin publish pipeline (Phase 3) → *first Admin build*.
5. **M5** — Lessons + Hifz + Progress (Phases 4–5).
6. **M6** — Assistant live via Worker (Phase 7) + Admin completion (Phase 8).
7. **M7** — AI recitation (only if passed review) (Phase 6 result).
8. **M8** — Release candidate (Phase 10).

Parallelism: Worker work (Phases 3/7/8) can run alongside Android Phases 2–5; Admin app starts after Worker pack routes exist.

---

## 5. Work Breakdown by Team Role

| Role | Responsibilities |
|---|---|
| Product owner | Source/licence decisions, scope, copy approval |
| Android dev(s) | User + Admin apps, Room, Media3, WorkManager, AI integration |
| Backend dev | Worker, D1, R2, Gemini adapter, RBAC, rate limits |
| ML engineer | Model evaluation, calibration, benchmarks |
| Quran teachers/scholars | Content verification, tajweed rubric, lesson review, evaluation of AI output |
| Designer | Design system, RTL/large-font variants, assets |
| QA | Test plans, airplane-mode, accessibility, device matrix |
| Translators | UR/EN copy review |

---

## 6. AI Studio Prompts

### 6.1 User App
Use **plan §16** ("Copy-Paste Prompt for Google AI Studio") as-is in AI Studio Build mode → **Android**.

### 6.2 Admin App (copy-paste)
```text
Build a native Android application named “Quran Learning Admin” using Kotlin and Jetpack Compose with Material 3. This is an internal admin tool for the “Quran Learning Companion” product. Do not build a web app, PWA, or WebView wrapper. Use a clean single-activity architecture with clear packages (core/designsystem, core/network, core/auth, feature/*).

PURPOSE
Admins manage content sources, downloadable packs, teacher lesson reviews, AI feedback reports, remote config/feature flags, admin users, and view an audit log. It talks ONLY to a Cloudflare Worker API under /v1/admin/*. It must contain NO API keys or secrets.

ROLES
owner, content_manager, teacher_reviewer, support. Show/hide navigation by role, but assume the server enforces permissions.

SCREENS
1) Sign-in (placeholder implementation behind an AdminAuthRepository interface; show an explicit "auth not configured" state; any mock must be debug-only and labelled DEMO).
2) Dashboard (health, API version, aggregate request/error/rate-limit counts, pending reviews, open reports) with stale-data timestamp.
3) Content Sources (list/add/edit; licence status pending/verified/rejected).
4) Packs (list with status pills draft/in_review/approved/published/retired; pack detail; new version form validating sha256 = 64 hex, sizeBytes > 0, r2Key, minAppVersion; submit/approve/publish/rollback with confirmation).
5) Lesson Review queue (UR/EN content view; Approve / Request changes with notes).
6) AI Reports (list, filter, assign, resolve with note).
7) Remote Config (toggles, numeric limits with safe ranges, kill switch requiring typed confirmation).
8) Audit Log (read-only, filters, expandable diff).
9) Admin Users (owner only: invite, change role, disable).
10) Model Catalogue (runtime, ABIs, min RAM, licence-confirmed flag).

ARCHITECTURE
ViewModels + StateFlow, repositories, typed DTOs, AdminApiClient (base URL via non-secret config), tokens stored encrypted with Android Keystore-backed storage, short session timeout, FLAG_SECURE on sensitive screens. Handle 401 (re-auth), 403 (role denied), 409 (conflict/reload), network errors, and empty/loading states. Never display secret values. Never implement an arbitrary-URL endpoint.

DESIGN
Same brand family as the user app (emerald #123C32, forest #1E624D, jade #DDEDE4, ivory #FAF7EF, gold #C9A45C, charcoal #202522; dark mode #101713/#18231D) but denser and utilitarian, with a visible “ADMIN” environment chip (Staging/Production). Accessible contrast, TalkBack labels, scalable text.

QUALITY
Complete navigation, no dead buttons, no fake data presented as real, unit tests for repositories/validators, UI tests for navigation/role gating. Clearly state which features are implemented and which need the live Worker.
```

### 6.3 Worker
Use **plan §17** prompt, then extend with the admin routes, D1 schema (doc 05) and RBAC/audit requirements:
```text
Extend the quran-learning-api Worker with versioned /v1/admin/* routes per the provided schema: token verification middleware, server-side role checks (owner, content_manager, teacher_reviewer, support), D1 migrations for the supplied tables, publish guards (verified licence, approved version, R2 object exists and size matches, valid sha256; model packs need license_confirmed and evaluation_passed), audit_log write for every mutation, strict schemas, request IDs, safe errors, and tests for authorized, unauthorized, forbidden-role, invalid-body, and publish-guard-failure cases. No secrets in responses or logs.
```

---

## 7. Test & Acceptance Matrix (summary)

| Area | Must pass before release |
|---|---|
| Quran integrity | Counts/IDs/checksums/diacritics/RTL |
| Offline | Airplane-mode run of every "offline" feature |
| Downloads | Checksum, resume/retry, rollback, delete, low storage |
| Online | Health, assistant happy path, invalid/oversize, rate limit, provider timeout/error |
| Security | No secret in APK/repo/logs; no open proxy; RBAC bypass tests; token expiry |
| AI recitation | FP/FN, latency, battery, heat; teacher-reviewed; low-confidence behaviour |
| Accessibility | TalkBack, 200% font, contrast, RTL/LTR, reduced motion |
| Privacy | No audio stored by default; disclosures match behaviour |
| Upgrade | Bookmarks/progress survive app and content updates |

---

## 8. Definition of Done (both apps + backend)

- User App is native Kotlin/Compose; core features work without sign-in.
- Installed Quran package complete and verified; sources/licences documented.
- Offline behaviour proven in airplane mode.
- Downloads integrity-checked and fail safely.
- AI split by task; no LLM-generated Quran text; recitation feedback confidence-aware and honest.
- Gemini key exists only as a Worker secret; Worker validates input and limits cost.
- Admin App enforces roles server-side, logs all changes, shows no secrets.
- Privacy/mic behaviour transparent; Play data-safety and privacy policy complete.
- Repos include setup instructions, test commands, `.env.example`/`.dev.vars.example`, deployment docs.

## 9. Launch Checklist (condensed from plan §18)

- [ ] Names, package IDs, icons, store listings, support contact
- [ ] UI/Quran font licences
- [ ] Quran source + checksums + counts independently verified
- [ ] Translation/WBW/tafsir/Qaida/audio licences documented
- [ ] Airplane-mode + low-storage tests pass
- [ ] Model licence/runtime/device coverage/limitations documented
- [ ] Teacher-reviewed evaluation for every AI correction claim
- [ ] Gemini key only as Worker secret; limits, alerts, monitoring on
- [ ] Privacy policy (mic, offline vs online processing, retention, deletion)
- [ ] Accessibility, RTL/LTR, dark mode, screen-reader pass
- [ ] Release signing + Play review steps for User and Admin apps
- [ ] Cloudflare deployment and Play publishing documented as separate processes

## 10. Immediate Next Steps (this week)

1. Confirm the 7 open decisions in PRD §12.
2. Create the three repos + `CONTENT_SOURCES.md`.
3. Start Phase 1: run the User App prompt in AI Studio (Android mode); in parallel scaffold the Worker (`/v1/health`, `/v1/config`).
4. Start Admin shell once the pack routes are defined.
