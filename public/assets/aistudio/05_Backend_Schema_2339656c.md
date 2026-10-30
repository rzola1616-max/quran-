# 05 — Backend Schema

Covers three data stores:

1. **User App local DB (Room / SQLite)** — source of truth on device, no login, never uploaded.
2. **Cloudflare D1 (SQLite)** — non-sensitive content metadata, config, reports, admin/audit, aggregate usage.
3. **Cloudflare R2** — pack files. Plus API contracts at the end.

**Privacy rule:** the backend holds **no** user reading history, progress, bookmarks, or voice audio.

---

## Part 1 — User App Room Schema

**Stable IDs:** `ayahId = surah * 1000 + ayah` (INTEGER). Used everywhere so bookmarks/progress survive content updates.

### 1.1 Content tables (canonical content is immutable at runtime)

**surah**
| Column | Type | Notes |
|---|---|---|
| surahNumber | INTEGER PK | 1–114 |
| nameArabic | TEXT | from verified source |
| nameTransliteration | TEXT | |
| nameTranslated | TEXT? | per language pack |
| ayahCount | INTEGER | validated by tests |
| revelationType | TEXT | per source |
| sourceId | TEXT | → content_source |

**ayah**
| Column | Type | Notes |
|---|---|---|
| ayahId | INTEGER PK | surah*1000+ayah |
| surahNumber | INTEGER FK | |
| ayahNumber | INTEGER | |
| script | TEXT | `uthmani` / `indopak` (composite unique with ayahId) |
| textArabic | TEXT | **verbatim from source**, never edited |
| juz, hizb, ruku, manzil, page | INTEGER | per source boundaries |
| sajdah | INTEGER | 0/1 (+ type) |
| sourceId, sourceVersion | TEXT | |
Indexes: (surahNumber, ayahNumber), (juz), (page). FTS table `ayah_fts(textArabicNormalized)` for search.

**word** *(only if licensed)*: `wordId PK, ayahId FK, position, textArabic, transliteration, meaningEn, meaningUr, packVersionId`

**translation**: `translationId PK, ayahId FK, languageCode, editionId, text, packVersionId`
**translation_edition**: `editionId PK, languageCode, translator, publisher, edition, version, licenseName, licenseUrl, sourceId`
**tafsir_reference**: `id PK, ayahId FK, editionId, title, bodyOrPointer, packVersionId`

**navigation_boundary**: `type (juz|hizb|ruku|manzil|page), number, startAyahId, endAyahId, sourceId`

**content_source**: `sourceId PK, name, publisher, licenseName, licenseUrl, version, sha256, installedAt` (mirrors `CONTENT_SOURCES.md`)

### 1.2 User data tables

**bookmark**: `bookmarkId PK, ayahId, folder?, createdAt` · unique(ayahId, folder)
**note**: `noteId PK, ayahId, text, createdAt, updatedAt`
**recent_reading**: `id PK, ayahId, page, openedAt` (keep last N)
**last_read**: `id=1 PK, ayahId, page, scriptMode, updatedAt`
**reading_session**: `sessionId PK, startedAt, endedAt, ayahsRead, minutes`

**lesson**: `lessonId PK, pathId, orderIndex, titleEn, titleUr, bodyJson, audioRef?, isDemo, packVersionId`
**lesson_progress**: `lessonId PK, status (not_started|in_progress|completed), completedAt, lastOpenedAt`
**quiz_attempt**: `attemptId PK, lessonId, scorePercent, answersJson, attemptedAt`

**hifz_plan**: `planId PK, startAyahId, endAyahId, dailyTarget, createdAt, paused`
**revision_item**: `itemId PK, planId FK, ayahId/rangeId, type (new|sabqi|manzil), dueAt, lastResult (easy|okay|hard), intervalDays`
**mistake_note**: `id PK, ayahId, wordPosition?, note, createdAt` *(local, tied to verified IDs)*

**recitation_practice_summary**: `id PK, startAyahId, endAyahId, startedAt, durationSec, flaggedWordsCount, lowConfidenceCount, modelVersion` — **summary only; no audio**.

### 1.3 System tables

**download_pack**: `packId PK, type, title, languageCode?, version, sizeBytes, sha256, status (NOT_INSTALLED|DOWNLOADING|VERIFYING|INSTALLED|FAILED|UPDATE_AVAILABLE), progressBytes, localPath, licenseUrl, installedAt, errorCode?`
**local_settings**: key-value (or DataStore): language, script, defaultTranslationId, theme, fontSizeSp, lineSpacing, highContrast, reducedMotion, keepScreenOn, dailyGoalMinutes, reminderTime, selectedQariId, aiMode.
**model_status** (or DataStore): `modelId, version, status, runtime, installedAt, deviceCompatible`.
**cached_config**: `json, fetchedAt` (last `/v1/config`).

Files (not in Room): audio, fonts, model files live in app-private storage; Room stores only metadata/paths.

### 1.4 Migration rules
- Content packs install via staging tables → verify → atomic swap in a transaction.
- Updates must never delete `bookmark`, `note`, progress tables.
- Room migrations tested with `MigrationTestHelper`; export schema JSON to VCS.

---

## Part 2 — Admin App Local Storage

- **DataStore (encrypted via Keystore):** access token, refresh token, role, tokenExpiry.
- **Optional read-only cache** (Room or in-memory): last dashboard metrics + lists with `fetchedAt`.
- No secrets, no provider keys, no signing keys stored.

---

## Part 3 — Cloudflare D1 Schema

```sql
-- Admin & access
CREATE TABLE admin_users (
  id TEXT PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  display_name TEXT,
  role TEXT NOT NULL CHECK (role IN ('owner','content_manager','teacher_reviewer','support')),
  status TEXT NOT NULL DEFAULT 'active' CHECK (status IN ('invited','active','disabled')),
  created_at TEXT NOT NULL DEFAULT (datetime('now')),
  last_login_at TEXT
);

CREATE TABLE admin_sessions (
  id TEXT PRIMARY KEY,
  admin_id TEXT NOT NULL REFERENCES admin_users(id),
  refresh_hash TEXT NOT NULL,          -- hash only, never the token
  expires_at TEXT NOT NULL,
  revoked_at TEXT,
  created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- Governance of content
CREATE TABLE content_sources (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  type TEXT NOT NULL,                  -- quran_text|translation|tafsir|audio|lessons|model|font
  publisher TEXT,
  license_name TEXT,
  license_url TEXT,
  license_status TEXT NOT NULL DEFAULT 'pending' CHECK (license_status IN ('pending','verified','rejected')),
  notes TEXT,
  created_at TEXT NOT NULL DEFAULT (datetime('now')),
  updated_at TEXT
);

CREATE TABLE packs (
  id TEXT PRIMARY KEY,
  type TEXT NOT NULL CHECK (type IN ('quran_text','translation','word_by_word','tafsir','audio','lessons','model')),
  title TEXT NOT NULL,
  language_code TEXT,
  source_id TEXT NOT NULL REFERENCES content_sources(id),
  status TEXT NOT NULL DEFAULT 'draft' CHECK (status IN ('draft','in_review','approved','published','retired')),
  created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

CREATE TABLE pack_versions (
  id TEXT PRIMARY KEY,
  pack_id TEXT NOT NULL REFERENCES packs(id),
  version TEXT NOT NULL,
  size_bytes INTEGER NOT NULL CHECK (size_bytes > 0),
  sha256 TEXT NOT NULL CHECK (length(sha256) = 64),
  r2_key TEXT NOT NULL,
  min_app_version INTEGER NOT NULL DEFAULT 1,
  rollout_percent INTEGER NOT NULL DEFAULT 100 CHECK (rollout_percent BETWEEN 0 AND 100),
  changelog TEXT,
  status TEXT NOT NULL DEFAULT 'draft' CHECK (status IN ('draft','in_review','approved','published','rolled_back','retired')),
  approved_by TEXT REFERENCES admin_users(id),
  published_at TEXT,
  created_by TEXT NOT NULL REFERENCES admin_users(id),
  created_at TEXT NOT NULL DEFAULT (datetime('now')),
  UNIQUE (pack_id, version)
);

CREATE TABLE model_meta (
  pack_version_id TEXT PRIMARY KEY REFERENCES pack_versions(id),
  model_version TEXT NOT NULL,
  runtime TEXT NOT NULL,               -- e.g. onnx-mobile
  abis TEXT NOT NULL,                  -- JSON array
  min_ram_mb INTEGER,
  license_confirmed INTEGER NOT NULL DEFAULT 0,
  evaluation_note TEXT,
  evaluation_passed INTEGER NOT NULL DEFAULT 0
);

-- Lessons governance (content itself ships inside lesson packs)
CREATE TABLE lessons_meta (
  id TEXT PRIMARY KEY,
  path_id TEXT NOT NULL,
  order_index INTEGER NOT NULL,
  title_en TEXT NOT NULL,
  title_ur TEXT,
  review_status TEXT NOT NULL DEFAULT 'draft' CHECK (review_status IN ('draft','in_review','changes_requested','approved')),
  reviewed_by TEXT REFERENCES admin_users(id),
  review_notes TEXT,
  reviewed_at TEXT,
  pack_version_id TEXT REFERENCES pack_versions(id)
);

-- User-submitted AI feedback (explicit consent, no identifiers)
CREATE TABLE ai_feedback_reports (
  id TEXT PRIMARY KEY,
  request_id TEXT NOT NULL,            -- from the assistant response
  category TEXT NOT NULL CHECK (category IN ('incorrect','harmful','unsourced','unclear','other')),
  language TEXT,
  app_version INTEGER,
  consented_excerpt TEXT,              -- only what user agreed to send; size-capped
  user_note TEXT,
  status TEXT NOT NULL DEFAULT 'new' CHECK (status IN ('new','triaged','resolved')),
  assigned_to TEXT REFERENCES admin_users(id),
  resolution_note TEXT,
  created_at TEXT NOT NULL DEFAULT (datetime('now')),
  resolved_at TEXT
);

-- Config & flags
CREATE TABLE app_config (
  key TEXT PRIMARY KEY,
  value_json TEXT NOT NULL,
  updated_by TEXT REFERENCES admin_users(id),
  updated_at TEXT NOT NULL DEFAULT (datetime('now'))
);
-- Example keys: assistant_enabled, online_recitation_enabled, min_app_version,
-- maintenance_message, assistant_max_input_chars, assistant_max_output_tokens

-- Audit (append-only)
CREATE TABLE audit_log (
  id TEXT PRIMARY KEY,
  admin_id TEXT NOT NULL REFERENCES admin_users(id),
  action TEXT NOT NULL,                -- e.g. pack.publish, config.update, role.change
  entity_type TEXT NOT NULL,
  entity_id TEXT,
  diff_json TEXT,                      -- no secrets
  request_id TEXT,
  created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- Aggregate operational metrics (no user identifiers)
CREATE TABLE usage_daily (
  day TEXT NOT NULL,
  route TEXT NOT NULL,
  request_count INTEGER NOT NULL DEFAULT 0,
  error_count INTEGER NOT NULL DEFAULT 0,
  rate_limited_count INTEGER NOT NULL DEFAULT 0,
  timeout_count INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (day, route)
);

CREATE INDEX idx_pack_versions_pack ON pack_versions(pack_id, status);
CREATE INDEX idx_reports_status ON ai_feedback_reports(status, created_at);
CREATE INDEX idx_audit_created ON audit_log(created_at);
```

**Role permissions (enforced server-side)**

| Capability | owner | content_manager | teacher_reviewer | support |
|---|:-:|:-:|:-:|:-:|
| View dashboard | ✔ | ✔ | ✔ | ✔ |
| Manage sources/packs/versions (create/submit) | ✔ | ✔ | — | — |
| Approve pack version | ✔ | — | — | — |
| Publish / rollback / retire | ✔ | ✔ (publish only after approval) | — | — |
| Review lessons | ✔ | — | ✔ | — |
| Triage reports | ✔ | — | — | ✔ |
| Edit config / kill switch | ✔ | — | — | — |
| View audit | ✔ | — | — | — |
| Manage admins | ✔ | — | — | — |

**Publish guard (server):** publish requires pack source `license_status='verified'`, version `status='approved'`, R2 object present with matching size, valid sha256; for `model` packs also `license_confirmed=1` and `evaluation_passed=1`.

---

## Part 4 — Cloudflare R2 Layout

```text
bucket: quran-learning-packs-{env}
  packs/{packType}/{packId}/{version}/pack.zip          (or per-file objects)
  packs/{packType}/{packId}/{version}/manifest.json
  models/{modelId}/{version}/model.bin + manifest.json
  releases/notes/{appVersion}.md
```
Rules: immutable versioned keys (never overwrite a published object); checksum recorded in D1 and manifest; no secrets in object metadata; downloads via Worker-issued manifest/URL; confirm licensing and Cloudflare terms before hosting audio/model files.

---

## Part 5 — API Contracts

### 5.1 Public (User App)

**GET /v1/health**
```json
{ "status": "ok", "apiVersion": "1", "time": "ISO-8601" }
```

**GET /v1/config**
```json
{
  "apiVersion": "1",
  "minAppVersion": 1,
  "features": { "assistantEnabled": true, "onlineRecitationEnabled": false },
  "maintenanceMessage": null
}
```

**GET /v1/packs** → `{ "packs": [ { "packId", "type", "title", "languageCode", "latestVersion", "sizeBytes" } ] }`

**GET /v1/packs/:id/manifest**
```json
{ "packId": "…", "version": "…", "sizeBytes": 0, "sha256": "64-hex", "licenseUrl": "https://…", "downloadUrl": "https://…", "minAppVersion": 1 }
```
(placeholders only — real values must come from validated records)

**POST /v1/assistant/ask**
```json
// request
{ "language": "ur|en", "question": "≤ N chars", "referenceIds": ["ayahId or lessonId", "…"] }
// response: see TRD §5.5 (status ok | insufficient_reference | rate_limited | error)
```
**POST /v1/assistant/explain-translation** → `{ "language", "translationEditionId", "ayahId" }`
**POST /v1/assistant/study-plan** → `{ "language", "goal", "minutesPerDay", "daysPerWeek" }`

**POST /v1/reports**
```json
{ "requestId": "…", "category": "incorrect", "language": "en", "appVersion": 1,
  "consentedExcerpt": "optional, capped", "userNote": "optional, capped" }
```
→ `{ "requestId": "…", "status": "received" }`

### 5.2 Admin (token + role required)

| Method | Path | Roles |
|---|---|---|
| POST | /v1/admin/session | public (auth start) |
| GET | /v1/admin/dashboard | all |
| GET/POST/PATCH | /v1/admin/sources | owner, content_manager |
| GET/POST | /v1/admin/packs | owner, content_manager |
| POST | /v1/admin/packs/:id/versions | owner, content_manager |
| POST | /v1/admin/packs/:id/versions/:v/submit | owner, content_manager |
| POST | /v1/admin/packs/:id/versions/:v/approve | owner |
| POST | /v1/admin/packs/:id/versions/:v/publish | owner, content_manager |
| POST | /v1/admin/packs/:id/versions/:v/rollback | owner, content_manager |
| GET/PATCH | /v1/admin/lessons/review | owner, teacher_reviewer |
| GET/PATCH | /v1/admin/reports | owner, support |
| GET/PUT | /v1/admin/config | owner |
| GET | /v1/admin/audit | owner |
| GET/POST/PATCH | /v1/admin/admins | owner |
| GET/POST | /v1/admin/models | owner, content_manager |

Every mutating admin call writes an `audit_log` row in the same logical operation.

### 5.3 Error format
```json
{ "requestId": "…", "status": "error", "code": "VALIDATION_FAILED | UNAUTHORIZED | FORBIDDEN | NOT_FOUND | RATE_LIMITED | PROVIDER_TIMEOUT | PROVIDER_ERROR | PAYLOAD_TOO_LARGE | INTERNAL", "message": "user-safe text" }
```
HTTP: 400/401/403/404/413/429/502/504/500 accordingly.

### 5.4 Limits (configurable defaults to be tuned)
- Max request body: small (e.g., a few KB for assistant routes).
- Max question length, max output tokens, per-route timeouts.
- Rate limit per anonymous client + global per-route budget; admin routes have separate stricter limits.

### 5.5 Secrets & bindings (Worker)
`GEMINI_API_KEY` (secret) · `DB` (D1) · `PACKS` (R2) · `RATE_LIMITER` (binding) · `ADMIN_JWT_SECRET` or auth-provider settings (secrets) · `ENVIRONMENT` (var). None of these appear in the apps.
