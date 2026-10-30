# 03 — App Flow (User and Admin Experience)

**Conventions:** `→` next screen · `[Button]` action · `(state)` UI state · ⚠ edge case
Core rule in every flow: **reading never depends on network, login, or AI.**

---

# PART A — USER APP: Quran Learning Companion

## A1. Navigation Map

```text
Launch
 ├─ First run → Onboarding (skippable) ─┐
 └─ Returning ───────────────────────────┴─► Main (Bottom Nav)
                                              ├─ Home
                                              ├─ Quran ──► Reader · Search · Bookmarks
                                              ├─ Learn ──► Qaida · Tajweed · Quiz
                                              ├─ AI Teacher ──► Practice · Assistant
                                              └─ Progress ──► Goals · Hifz stats
Top bar / More: Settings · Downloads · Privacy · Help · Content Sources · About
```

## A2. First Launch & Onboarding

1. **Splash** → local DB check (content installed? migrations done?).
2. **Welcome** — [Get Started] / **[Skip]** (Skip lands on Home with defaults).
3. **Language** — Urdu / English.
4. **Goal** — New reader · Quran reader · Tajweed · Hifz · Translation · Family-guided (single or "decide later").
5. **Script** (if multiple validated) — Uthmani / Indo-Pak.
6. **Optional downloads** — translation / Qari packs with sizes; [Download now] / [Later].
7. **Privacy explanation** — no account needed; mic only when you choose; audio not stored by default.
8. → **Home**.

⚠ No network → steps 6 shows "Available when online"; flow continues.
⚠ No bundled Quran content (dev build) → Home shows "Demo data" banner; never claims a complete Quran.

## A3. Home

- Greeting (optional) · mode chip (Offline/Online)
- **Continue Reading** card (last Surah/ayah/page from Room) → Reader at saved position
- Quick actions: Read · Listen · Learn · Hifz · Ask Assistant
- Daily goal ring (local) · optional daily ayah (verified + attributed; hideable)
- Downloads shortcut

States: (first-run, no history) → "Start reading" card · (offline) → chip + Online-only actions show "Needs internet" · (empty) never blank.

## A4. Quran Reader Flow

```text
Quran tab → Surah list / Juz list / Page jump / Go to Surah:Ayah
   └─► Reader
        ├─ Tap ayah → sheet: Bookmark · Note · Copy/Share · Play from here · Translation/Tafsir
        ├─ Long-press word → word meaning (only if data installed)
        ├─ Top bar: script · translation selector · text size/spacing · theme
        ├─ Audio bar: Play/Pause · Prev/Next · Repeat · Speed · Qari
        └─ [Practice] → AI Teacher (separate screen, not mixed into text)
```
- Last-read saved automatically (debounced) to Room.
- Translation not installed → inline card "Install Urdu translation (xx MB)" → Downloads.
- Audio not downloaded → "Download this Surah (xx MB)" or stream if online.
- ⚠ Never shows AI explanations on the Mushaf view; tafsir/lessons open in a bottom sheet/screen.

### Search
Search field → results grouped (Surah · Ayah reference · Arabic text · Translation) → tap → Reader at ayah. Works fully offline over installed data. Empty → "No results in installed content" + suggestion to install a translation.

### Bookmarks / Notes / History
Quran tab → Bookmarks (folders optional) · Notes · Recent. Swipe to delete with undo.

## A5. Audio / Recitation Library

Audio tab area (inside Quran) → Qari list → Surah list with download state → Player.
- Download: [Download] → size + storage check → progress notification → verified → (Downloaded).
- Player: queue, loop range, repeat count, speed (visible), sleep timer optional.
- ⚠ Interrupted download → state "Paused/Failed" with [Retry]; partial files never played.
- ⚠ No autoplay; background playback only after user starts it.

## A6. Learn Flow (Qaida & Tajweed)

```text
Learn tab → Path list (Alphabet · Harakat · Qaida · Makharij · Tajweed rules)
   └─► Lesson list (locked/unlocked not enforced as shame—just order)
         └─► Lesson: explanation (UR/EN) → listen examples → practice → mini-quiz → complete
```
- Completion and quiz attempts saved locally.
- Content not downloaded → "Download lesson pack" card.
- Demo content is labelled "Sample — not verified" until real curriculum is integrated.
- Tajweed lessons labelled **"Learning a rule"**; AI detection only shown where that exact rule/model has been evaluated.

## A7. Hifz Flow

```text
Progress/Learn → Hifz → Choose range
   ├─ New lesson: reveal → hide → recall → hints (first word / partial)
   ├─ Sabqi: previous lesson recall
   └─ Manzil: scheduled longer revision
 → Result: self-marked (Easy/Okay/Hard) or spoken test (if model Ready)
 → Mistakes saved to Mistake Notebook (by ayahId) → schedule next revision
```
Supportive copy only; user can pause/reschedule goals.

## A8. AI Teacher (Live Recitation) Flow

```text
AI Teacher tab
 1. Pick Surah/ayah range
 2. Mode: On-device (Ready?) / Online (if configured & consented)
 3. Model status check
      NotInstalled → [Download model (size, licence note)] or back
      Incompatible → explain; keep other features
      Failed → [Retry]
      Ready → continue
 4. Mic permission rationale → [Allow]  (Denied → explain + Settings link; app stays usable)
 5. Listening screen: visible mic indicator, word-by-word progress, [Pause] [Stop]
 6. Candidate issue (only if confidence passes policy):
       icon + label (not colour alone) + optional vibration
       [Hear Again] [Try Again] [Continue Anyway]
    Low confidence → "I could not confidently assess this part—please repeat"
 7. End → summary (local, no audio) → [Save summary] / [Discard]
```
⚠ Model not integrated → "AI model not installed/configured". No simulated output.
⚠ App backgrounded → listening stops automatically.

## A9. AI Learning Assistant Flow

```text
AI Teacher → Assistant (or Home quick action)
 → Check: Worker configured? Online? Feature flag enabled?
     No → "Assistant unavailable offline" (reader still available)
 → Ask question / pick approved translation to explain / request study plan
 → (loading, cancellable) → Answer card:
       AI-generated label · references · disclaimer
       [Report this answer] [Open referenced ayah/lesson]
 → insufficient_reference → "I can't find a verified source. Try [trusted source suggestion]."
 → rate_limited → friendly retry-later; error → retry
```

## A10. Downloads Manager

Downloads → tabs (Translations · Audio · Lessons · Models) → each pack: size, version, licence link, status (Not installed / Downloading % / Verifying / Installed / Update available / Failed) → actions: [Download] [Pause] [Resume] [Retry] [Delete].
Checksum failure → auto-delete temp + message + [Retry]. Low storage → blocked with explanation.

## A11. Progress

Progress tab → today's goal · week chart · ayahs read · lessons completed · hifz revisions · weak sections → [Set goals] · [Backup/Export] · [Reset my learning data] (double-confirm).

## A12. Settings & Info

Language · script · default translation · theme · font/spacing · high contrast · reduced motion · reminders · privacy (mic explanation, data summary) · backup/restore · reset · About · Content Sources · AI limitations · Help.

## A13. Offline/Online State Handling (global)

| Situation | Behaviour |
|---|---|
| Airplane mode | Reader, bookmarks, search, installed lessons/audio, hifz, settings all work |
| Worker down | Assistant shows unavailable; everything else unchanged |
| Download fails | Safe rollback; Retry |
| Permission denied | Explain; feature disabled, app continues |
| Storage low | Warn before downloads; never corrupt existing data |
| App update | Progress/bookmarks preserved via stable ayah IDs + migrations |

---

# PART B — ADMIN APP: Quran Learning Admin

## B1. Navigation Map

```text
Launch → Sign-in → (role check) → Dashboard
Bottom Nav / Drawer (items shown by role):
  Dashboard · Packs · Content Sources · Lesson Review · Reports · Config · Audit · Admins (Owner)
```

## B2. Sign-in & Session
1. Sign-in screen (method per final auth decision).
2. Worker verifies → returns role + short-lived token (stored Keystore-encrypted).
3. Optional biometric unlock on resume; auto sign-out on timeout/token revocation.
⚠ Unauthorized/disabled user → clear message; no data shown.
⚠ Offline → "Admin needs internet"; may show last cached dashboard read-only.

## B3. Dashboard (all roles, content varies)
Worker health + API version · aggregate request/error/rate-limit counts · pending lesson reviews · open AI reports · latest pack publishes · kill-switch status. Pull-to-refresh; stale-data timestamp.

## B4. Content Sources Flow (Owner, Content Manager)
List → [Add source] → name, type, publisher, licence name/URL, status (pending/verified/rejected), notes → Save (audit entry).
A pack cannot be **published** if its source is not `verified`.

## B5. Pack Management Flow (Owner, Content Manager)
```text
Packs list (filter by type/status)
 → [New pack] type · title · language · source
 → Pack detail → [New version]
       version · sizeBytes · sha256 · R2 key · minAppVersion · changelog
 → Status: draft → [Submit for review] → in_review → [Approve] (Owner/Reviewer) → approved
 → [Publish] (confirm; optional staged % rollout) → published
 → [Rollback] / [Retire] (confirm + reason) → audit entry
```
Validation in app + enforced in Worker: sha256 format, size > 0, licence verified, R2 object exists.

## B6. Lesson / Quiz Review (Teacher-Reviewer, Owner)
Queue → open item (lesson text UR/EN, examples, audio refs) → [Approve] / [Request changes] with notes → status + reviewer + timestamp stored → audit entry.
Only approved lessons can be included in a published lessons pack.

## B7. AI Feedback Reports (Support, Owner)
Reports list (status: new/triaged/resolved; category) → detail (requestId, language, app version, answer excerpt user consented to send, user note) → [Assign] · [Mark resolved + note] · [Flag source/prompt issue].
No personal identifiers are collected; none are shown.

## B8. Remote Config / Feature Flags (Owner)
Toggle: assistant enabled · online recitation provider enabled (default off) · min supported app version · maintenance banner text · per-route limits (view/edit within safe bounds) → [Save] (confirm) → audit entry.
**Kill switch** for AI assistant: one tap + confirm.

## B9. Audit Log (Owner; read-only for others if permitted)
Chronological list: admin, action, entity, time; filter by admin/entity/date; detail shows changed fields (no secrets).

## B10. Admin Users (Owner)
List → [Invite] (email + role) → role change/disable with confirm → audit entry. Owner cannot remove the last Owner.

## B11. Model Catalogue (Owner, Content Manager)
Model pack entries: model version, runtime, supported ABIs, min RAM, licence-confirmed flag, evaluation-status note → can only be published when licence-confirmed and marked evaluated.

## B12. Admin Error & Safety States
Token expired → re-auth · 403 → "Your role cannot do this" · conflict (someone else changed) → reload prompt · destructive actions always confirm · no secret values ever displayed.

---

# PART C — End-to-End Cross-App Flows

### C1. Publishing a new translation pack
Content Manager (Admin) adds verified source → creates pack + version (sha256/size/R2 key) → submits → Owner approves → publish → Worker `/v1/packs` lists it → User app Downloads shows "Update available" → user downloads → checksum verify → atomic install → bookmarks unaffected.

### C2. Reporting a bad AI answer
User taps [Report this answer] → consent dialog showing exactly what will be sent → `POST /v1/reports` → Admin Reports list → Support triages → resolves/flags → (if source issue) Content Manager updates data → next answer improves. Reporter never required to identify themselves.

### C3. Emergency AI shutdown
Owner toggles kill switch in Admin → Worker config updated → User app fetches `/v1/config` (cached) → Assistant shows "Temporarily unavailable"; reader/offline learning unaffected.
