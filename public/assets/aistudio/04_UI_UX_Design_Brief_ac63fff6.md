# 04 — UI/UX Design Brief

**Apps:** Quran Learning Companion (User) · Quran Learning Admin (Admin)
**Platform:** Native Android phone, Jetpack Compose + Material 3
**Version:** 1.0 (draft) · **Date:** 29 Oct 2026

---

## 1. Design Intent

A calm, premium, respectful Islamic learning experience: elegant, focused, warm, modern. Do **not** copy another app's UI. No heavy ornament, no busy backgrounds behind Arabic, no unnecessary animation, no AI-generated calligraphy or Quran page images — real, verified text rendered with a licensed font only.

**Feeling:** quiet · trustworthy · unhurried · encouraging (never shaming).
**Admin app:** same brand family but denser, utilitarian, and visually distinct enough (e.g., a "ADMIN" environment chip) that nobody confuses it with the public app.

## 2. Colour System

| Token | Light | Dark | Use |
|---|---|---|---|
| Primary / Deep Emerald | `#123C32` | `#DDEDE4` on dark surfaces | Top bars, selected nav |
| Action / Forest Green | `#1E624D` | `#1E624D` | Primary buttons, learning actions |
| Soft Jade | `#DDEDE4` | `#18231D` tint | Quiet cards, progress background |
| Surface / Warm Ivory | `#FAF7EF` | `#101713` | Reading + default background |
| Dark Surface | — | `#18231D` | Cards, toolbars (dark) |
| Accent / Muted Gold | `#C9A45C` | `#C9A45C` | Subtle accents, progress highlights |
| Text Primary / Charcoal | `#202522` | `#F2F5F2` (proposed) | Body text |
| Text Secondary | `#67716B` | `#A9B4AD` (proposed) | Metadata |
| Error Red | `#B42318` | `#F0867A` (proposed) | Errors / possible recitation issue **only** |

Rules: meet WCAG AA contrast (verify each pair); red is **never the only signal** — always icon + label (+ optional haptic). Provide High-Contrast mode and a Sepia reading surface.
*Admin accent:* use Gold sparingly for status; use neutral greys for dense tables.

## 3. Typography

| Role | Font | Notes |
|---|---|---|
| Quran Arabic | Licensed Quran font (Uthmani; Indo-Pak if offered) | Verify diacritics/ligatures; verified distribution licence |
| UI text | Roboto or Noto Sans (confirm licence/rendering) | Urdu UI needs a proper Nastaliq/Naskh-compatible font — test RTL |
| Scale | Arabic ≥ 28–32sp default (adjustable 20–48sp), translation 16sp, UI 14–16sp | Respect system font scaling to at least 200% |
- Generous line spacing for Arabic; adjustable.
- Arabic RTL; Urdu RTL; English LTR. In verse cards, keep Arabic block and translation block directionally separate (never mix mid-line).

## 4. Shape, Spacing, Motion
- Corner radius 16–22dp for cards; 12dp for chips/inputs.
- 8dp spacing grid; screen padding 16dp; touch targets ≥ 48×48dp.
- Little/no shadow; subtle separators.
- Motion: short fades/slides; honour system "remove animations"; no looping decorative motion.
- Local content appears immediately; skeleton loaders **only** for network content.

## 5. Navigation

### User App — bottom bar (5)
Home · Quran · Learn · AI Teacher · Progress. Settings/Downloads/Privacy/Help/Content Sources/About under top-bar menu.
Small screens: icon + short label; labels never truncated at large font (switch to icon-only with content descriptions if necessary).

### Admin App
Phones: bottom bar (Dashboard, Packs, Reports, More) with More → Sources, Lesson Review, Config, Audit, Admins (role-filtered). Optional tablet/landscape: navigation rail.

## 6. Core Components (Design System)

`AppTopBar` · `BottomNav` · `ContinueReadingCard` · `AyahCard` (Arabic + translation + actions) · `TranslationSelector` · `AudioMiniPlayer` / `AudioPlayerBar` · `PackCard` (status, size, action) · `ProgressRing` · `QuizOptionCard` · `StatusChip` (Offline/Online) · `ModelStatusBanner` · `ListeningIndicator` · `IssueBadge` (icon + label) · `AiLabel` ("AI-generated explanation") · `EmptyState` · `ErrorState` (with Retry) · `ConfirmDialog` · *(Admin)* `RoleBadge` · `StatusPill` (draft/in_review/approved/published/retired) · `DiffView` · `AuditRow` · `MetricCard`.

## 7. User App — Screen Specs

### 7.1 Onboarding
Full-screen cards, one decision per screen, progress dots, **Skip** always visible (top-right). Illustration: simple geometric patterns only (no figures, no Quran imagery). Privacy screen uses plain bullets with icons.

### 7.2 Home
- Top: greeting + status chip + overflow menu.
- **Continue Reading** hero card (Soft Jade, gold progress hairline).
- Quick action grid (2×2 + 1): Read, Listen, Learn, Hifz, Ask Assistant.
- Daily goal ring card; optional daily ayah card (hide button).
- Downloads shortcut row. No ads, no sign-in wall.

### 7.3 Quran Reader
- **Header:** Surah name (Arabic + transliteration), Juz/page info, Bismillah per source rules.
- **Body:** page/ayah mode; Arabic large and centred/RTL; translation below in smaller text with attribution on tap or footer; ayah-end marker per source.
- **Toolbar (collapsible):** script, translation, text size, spacing, theme, keep-screen-on.
- **Bottom audio bar:** play/pause, prev/next, repeat, speed, Qari; download status.
- **Tap ayah** → bottom sheet (Bookmark, Note, Copy/Share, Play, Translation, Tafsir).
- **Practice** button is separate from text; no AI clutter on the Mushaf.
- Fullscreen/low-distraction mode.

### 7.4 Search
Sticky search bar, grouped results with highlighted match, filters (Arabic / Translation / Reference). Empty state explains what is installed.

### 7.5 Learn
Path cards (Alphabet, Harakat, Qaida, Makharij, Tajweed). Lesson screen: big example, play button, translation toggle, "Listen again", Practice → Quiz. Rules show a "Learning" tag vs "AI-checked" tag (only when truly supported). Sample data labelled "Sample — not verified".

### 7.6 Hifz
Range picker → Hide/Reveal canvas with tap-to-reveal words, hint chips, self-rating (Easy/Okay/Hard), revision schedule list, Mistake Notebook. Friendly copy ("Let's strengthen this part").

### 7.7 AI Teacher
- Setup card: range, mode (On-device / Online), model status banner.
- Listening screen: large ayah text with word highlight, **unmissable mic indicator**, persistent Pause/Stop.
- Issue state: `IssueBadge` (icon + text "Possible issue on this word"), actions: Hear Again · Try Again · Continue Anyway.
- Low confidence: neutral card "I could not confidently assess this part—please repeat."
- Not installed/incompatible: honest explanation and path back; no fake analysis.

### 7.8 Assistant
Chat-like but structured: question box, suggested prompts (app help, explain this translation, study plan), answer card with `AiLabel`, references list, disclaimer, Report button. Unavailable/offline state with link back to reader.

### 7.9 Progress
Goal ring, weekly bars, counters, weak-section list, gentle streak ("You read 3 days this week"). Reset and Export in secondary area.

### 7.10 Downloads
Tabs; `PackCard` with size, version, licence link, state chip; progress bar with Pause/Resume; Delete with confirm.

### 7.11 Settings & Info
Grouped list with headers (Language, Reading, Audio, Privacy, Data, About). Privacy page includes a plain-language "What stays on your device" table.

## 8. Admin App — Screen Specs

| Screen | Layout notes |
|---|---|
| Sign-in | Centered card, method per auth decision, environment chip (Staging/Prod), no "remember password" |
| Dashboard | `MetricCard` grid (health, requests, errors, rate-limited), pending reviews/reports counts, last publishes list |
| Packs | Filterable list with `StatusPill`; detail with version timeline; version form with inline validation (sha256 64-hex, size > 0); publish/rollback confirm sheet showing impact |
| Sources | List with licence status pill; form with licence URL (tap to open), status selector |
| Lesson Review | Queue → split content view (UR/EN tabs) + decision bar (Approve / Request changes + notes) |
| Reports | List with category/status chips; detail shows requestId, consented excerpt, note; assign/resolve controls |
| Config | Toggle rows + numeric fields with safe ranges; red-outlined **Kill Switch** row with confirm |
| Audit | Dense list, filters, expandable diff |
| Admins | List with `RoleBadge`; invite/role change dialogs |
Admin never displays secrets; masked values only. Destructive actions use `ConfirmDialog` requiring explicit typed word for kill switch/retire.

## 9. Content & Tone Guidelines

- Respectful, simple, encouraging. Avoid shame/ranking language ("You missed…" → "Ready to revisit this part?").
- Always label AI: "AI-generated learning explanation".
- Honest uncertainty: "I could not confidently assess this part—please repeat."
- Never claim: "fully offline" (unless tested), "certified tajweed teacher", "complete Quran" (unless verified).
- Urdu and English copy written/reviewed by native speakers; keep sentences short.
- Don't assume gender, sect, age, or background.

## 10. Accessibility Checklist
- TalkBack: meaningful `contentDescription`, logical focus order, announce state changes (listening, download complete).
- Scalable text to 200%, no clipped Arabic.
- Contrast AA+; High-contrast theme; non-colour cues.
- Touch targets ≥ 48dp; visible focus indicator.
- Reduced motion; no autoplay audio; clear mic on/off and stop control.
- RTL/LTR mirroring correct for icons (back arrows, progress direction) except where directionality is semantic (e.g., media controls stay LTR).
- Simple mode: fewer elements, larger controls, hides secondary actions.

## 11. State Catalogue (design every screen for these)
Loading (network only) · Empty · Error with retry · Offline · Permission denied · Low storage · Pack not installed · Model unavailable · Feature disabled by admin · Content demo-only · Success/confirmation.

## 12. Deliverables for Design Handoff
1. Figma (or equivalent) with tokens, components, light/dark/sepia/high-contrast.
2. Screen flows for both apps with all states in §11.
3. RTL and large-font (200%) variants for Reader, Learn, AI Teacher.
4. App icons (adaptive) for both apps with distinct colour accents; Play listing assets.
5. Copy deck (UR/EN) and accessibility annotations.
