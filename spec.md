# Compose UI Design System — Specification

A reusable, Kotlin-first Jetpack Compose design system providing theming, tokens, and high-quality components for building Android apps with consistent UX. Inspired by the sibling [SwiftUIComponents](https://github.com/maniramezan/swiftuicomponents) project (conceptual parity, not API parity), with patterns extracted from production code at `~/Developer/novalingo/mobile/Android`.

---

## 1. Foundational Decisions

These are locked in before any module is scaffolded.

| Decision | Choice | Notes |
|---|---|---|
| Platform | **Android-only (Phase 1)**; structure modules to be KMP-ready for Phase 2 | Avoids premature complexity; uses pure-Kotlin where possible. |
| Compose | **Compose BOM (latest stable)**, Material 3 | Pin via version catalog; re-check each release cycle. Use KSP for annotation processors where supported. |
| SDK targets | `minSdk = 26`, `compileSdk = latest stable`, `targetSdk = latest stable` | Covers >95% of devices; keeps behavior aligned with current Android requirements. |
| Kotlin | Latest stable, K2 enabled | |
| Theming model | **Extend Material 3** with additional semantic tokens (spacing, elevation, motion) layered via `CompositionLocal` | Don't fork M3; ride its updates. |
| Extensibility primitive | **Immutable `data class` tokens + slot APIs + `CompositionLocal`** — *not* OOP interfaces/abstract classes | Idiomatic Compose; preserves `@Stable`/`@Immutable` recomposition; matches M3's own design. |
| Distribution | **Maven Central** via `gradle-maven-publish-plugin` + GPG signing | Group: `io.github.maniramezan.compose` (auto-accepted by Sonatype for GitHub-owned namespaces; no domain purchase required). |
| Versioning | **SemVer**; explicit deprecation policy (≥1 minor before removal) | Binary compatibility enforced via Kotlin Gradle binary compatibility validation when stable enough; otherwise `binary-compatibility-validator`. |
| License | **MIT** | `LICENSE` file at repo root; SPDX header optional. |
| Catalog browser | **Showkase** (Airbnb) | Auto-generates browsable catalog from `@Preview` / `@ShowkaseComposable`, `@ShowkaseColor`, and `@ShowkaseTypography` annotations. Saves building nav/search/filter UI. |
| Icons | **Small curated default icon set**; consumers can override via `AppTheme(icons = MyIcons)` | `IconToken` and `AppIcons` live in `:theme`; `:icons` provides default implementations. Do not bundle the full `material-icons-extended` artifact into the core path. |
| Motion tokens | **Included in Phase 1** | Durations, easings, and standard transition specs as semantic tokens (`AppMotion.short`, `AppMotion.emphasizedEasing`). |

---

## 2. Module Layout

```
:foundation              // pure Kotlin helpers (no Compose, no Android)
:tokens                  // raw design tokens (palette, type scale, dimens, motion)
:theme                   // semantic theme, CompositionLocals, AppTheme { }, IconToken/AppIcons contracts
:icons                   // curated default icon implementations
:components              // buttons, text fields, cards, lists, sheets, dialogs…
:compose-utils           // Modifier extensions, preview annotations, semantics helpers
:testing                 // Compose test utilities, screenshot infra, semantic matchers
:catalog                 // exhaustive component showcase app (every variant/state)
:sample                  // realistic mini-product demonstrating composition
```

**Dependency rules (enforced via Gradle):**

- `:foundation` → no deps.
- `:tokens` → `:foundation` only.
- `:theme` → `:tokens`, Compose runtime/material3.
- `:icons` → `:theme`, minimal icon dependencies only.
- `:components` → `:theme`, `:icons`, `:compose-utils`.
- `:compose-utils` → Compose runtime only.
- `:testing` → Compose UI test + `:compose-utils`; no `:components` dependency.
- `:catalog`, `:sample` → everything above.

> Rationale: tokens (raw values) are separated from theme (semantic mapping), mirroring Material's `ref` vs `sys` split. The original "utilities" module is intentionally split into `:foundation` + `:compose-utils` to avoid a god-module. Icon contracts live in `:theme` so `AppTheme(icons = …)` does not create a `:theme` ↔ `:icons` dependency cycle.

---

## 3. Theming & Extensibility

### Token layers

1. **Reference tokens** (`:tokens`): raw palette (`Blue40`), type scale (`DisplayLarge`), dimens (`Space4 = 4.dp`).
2. **System tokens** (`:theme`): semantic mapping (`AppColors.surface`, `AppSpacing.md`, `AppElevation.raised`).
3. **Component tokens** (`:components`): per-component derived values (e.g., `ButtonTokens.containerColor`).

### API shape

```kotlin
@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    // …
)

@Immutable
data class AppSpacing(val xs: Dp, val sm: Dp, val md: Dp, val lg: Dp, val xl: Dp)

@Composable
fun AppTheme(
    colors: AppColors = AppColors.light(),
    typography: AppTypography = AppTypography.default(),
    shapes: AppShapes = AppShapes.default(),
    spacing: AppSpacing = AppSpacing.default(),
    icons: AppIcons = AppIcons.default(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
)

object AppTheme {
    val colors: AppColors @Composable get() = LocalAppColors.current
    val spacing: AppSpacing @Composable get() = LocalAppSpacing.current
    val icons: AppIcons @Composable get() = LocalAppIcons.current
    // …
}
```

**Overrides**: consumers call `AppTheme(colors = AppColors.light().copy(primary = MyBrand))` — no inheritance required. Components customize via **slot APIs**, not subclassing.

**Catalog token exposure**: Showkase does not discover arbitrary values inside theme objects or `CompositionLocal`s. Export explicit annotated wrappers for catalog coverage: `@ShowkaseColor` for palette/semantic colors, `@ShowkaseTypography` for type styles, and preview composables for spacing, elevation, shapes, motion, and icon tokens.

### Required behaviors

- Light + dark variants for every token set.
- Optional Android 12+ **dynamic color** support, opt-in via `AppTheme(dynamicColor = true)`.
- **RTL** support: all components use `start/end`; mirror-aware icons declared via `IconToken(autoMirror = true)`.
- **Edge-to-edge readiness**: catalog/sample activities call `enableEdgeToEdge()`, screens consume `Scaffold`/window insets correctly, scrollables receive insets through `contentPadding`, and text-input screens handle IME insets without double padding.
- **Accessibility** baked in:
  - Minimum 48dp touch target on all interactive components.
  - `Modifier.semantics` and `contentDescription` enforced via custom lint.
  - Tested with TalkBack and font-scale 200%.

---

## 4. Components (Phase 1 scope)

| Category | Components |
|---|---|
| Actions | `PrimaryButton`, `SecondaryButton`, `TextButton`, `IconButton`, `FAB`, `SegmentedControl` |
| Inputs | `TextField`, `PasswordField`, `SearchField`, `Checkbox`, `RadioGroup`, `Switch`, `Slider` |
| Containers | `Card`, `Surface`, `Section`, `BottomSheet`, `Dialog`, `Snackbar` |
| Lists | `ListItem`, `LazyList` wrappers, `EmptyState`, `LoadingState`, `ErrorState` |
| Navigation | `TopAppBar`, `BottomBar`, `TabRow`, `NavRail` |
| Feedback | `ProgressIndicator`, `Skeleton`, `Toast` |
| Typography | `AppText` with semantic styles |

Each component must ship with: light/dark previews, multi-state previews (`@PreviewLightDark`, `@PreviewFontScale`), catalog entry, unit test, screenshot test, Dokka doc.

---

## 5. Quality Bar

| Concern | Tooling / Rule |
|---|---|
| Static analysis | `ktlint` + `detekt` (zero warnings on CI) |
| Custom lint | Enforce token usage (no hardcoded `Color(0xFF…)`, no raw `dp` in components) |
| API stability | Kotlin Gradle binary compatibility validation when stable enough, otherwise `binary-compatibility-validator`; `@ExperimentalApi` opt-in for unstable APIs |
| Unit tests | JUnit 5 + Truth/Kotest for JVM/domain tests |
| UI tests | `compose-ui-test-junit4` with `:testing` matchers for Compose UI/instrumented tests |
| Screenshot tests | **Roborazzi** (JVM, fast on CI) |
| Accessibility tests | `AccessibilityChecks` + manual TalkBack checklist per component |
| Performance | Macrobenchmarks and baseline profiles from release-like `:sample` flows; Compose compiler metrics/reports; recomposition counters in debug builds |

---

## 6. Sample vs Catalog

- **`:catalog`** — exhaustive component showcase (every variant/state). For contributors and consumers browsing the system. Built first.
- **`:sample`** — realistic mini-product (e.g., a settings + list + detail flow + form) that demonstrates *composition* of components and theming overrides.
- **Performance ownership** — `:catalog` validates discoverability and visual coverage; `:sample` owns consumer-like startup, navigation, and interaction benchmarks.

---

## 7. Documentation

- **Dokka** generates API reference per module.
- **MkDocs Material** site published to GitHub Pages with:
  - Getting started / install
  - Theming guide with concrete override examples
  - Per-component pages (screenshots + code + a11y notes)
  - Migration guide & changelog
  - Contributing guide + ADR index
- **ADRs** (Architecture Decision Records) in `/docs/adr/` for non-obvious choices (theming model, module boundaries, testing strategy, etc.).
- Root README links to docs site, catalog APK, and sample APK.

---

## 8. CI/CD

**PR checks** (GitHub Actions):
- `assemble`, `lint`, `detekt`, `ktlintCheck`
- Unit tests + Roborazzi screenshot tests
- `apiCheck` (binary compatibility)
- Compose compiler metrics/report task, uploaded as artifact when enabled
- Build `:catalog` debug APK as artifact

**Release workflow** (on tag `vX.Y.Z`):
- Publish to Maven Central via `gradle-maven-publish-plugin`
- Generate Dokka, build MkDocs, deploy to GitHub Pages
- Attach `:catalog` and `:sample` APKs to GitHub Release
- Generate/update baseline profile from release-like `:sample` benchmark flow when UI startup/navigation paths change

**Maintenance**:
- Renovate (or Dependabot) for dependency updates
- Re-verify pinned versions in version catalog each release cycle

---

## 9. Extracting from Novalingo

Process to avoid copy-paste rot:

1. Audit `~/Developer/novalingo/mobile/Android` and list candidate components in `/docs/extraction-candidates.md` with one-line justification each.
2. For each candidate: strip product-specific naming, strings, branding, and dependencies.
3. Re-implement in this repo with full test + preview + catalog entry.
4. Record any non-obvious decision as an ADR.
5. Do **not** import code wholesale.

---

## 10. Roadmap

Implementation will be done in **one continuous run**, but with **one commit per phase** so history reflects the staged build-up.

**Phase 0 — Foundations** _(commit: `chore: scaffold modules and CI`)_
- Scaffold module layout (§2) with empty modules + `libs.versions.toml` version catalog.
- Write ADRs: theming model, module boundaries, testing strategy, Showkase choice.
- CI skeleton (lint + tests + `apiCheck`).
- Compose compiler metrics wiring behind a Gradle property.
- MIT `LICENSE` + root `README.md`.

**Phase 1 — Vertical Slice** _(commit: `feat: vertical slice — theme + PrimaryButton`)_
- `:foundation`, `:tokens` (colors, typography, shapes, spacing, **motion**), `:theme` with `AppTheme { }` + `CompositionLocal`s.
- `:theme` defines `IconToken`/`AppIcons`; `:icons` supplies a small curated default icon implementation with override hook.
- One full component (`PrimaryButton`) end-to-end: catalog entry (Showkase) → unit test → Roborazzi screenshot test → Dokka page.
- Publish `0.1.0-alpha` to Maven Central under `io.github.maniramezan.compose`.
- Validates the whole pipeline before scaling.

**Phase 2 — Component Expansion** _(one commit per category in §4, e.g. `feat(components): actions`, `feat(components): inputs`, …)_
- Build out components in §4, each meeting the §5 quality bar.

**Phase 3 — Polish & 1.0** _(commit: `feat: sample app, dynamic color, docs site`)_
- `:sample` app, dynamic color, edge-to-edge audit, full a11y audit, MkDocs site deployment, macrobenchmarks + baseline profiles.
- Cut `1.0.0` with API stability guarantee.

**Phase 4 — KMP (optional, future)**
- Promote `:foundation`, `:tokens`, and parts of `:theme` to Compose Multiplatform.

---

## 11. Resolved Decisions

All prior open questions are resolved:

- **Group ID**: `io.github.maniramezan.compose` — Sonatype auto-accepts the `io.github.<gh-username>` namespace, so no domain ownership or DNS verification is needed. Can migrate to a custom domain later without breaking consumers (publish under both for one minor release, then deprecate).
- **License**: MIT.
- **Catalog**: Showkase (Airbnb) — annotation-driven; discovers annotated previews, colors, and typography. Non-color/non-typography tokens need explicit preview wrappers.
- **Icons**: small curated default icon implementation in `:icons`; `IconToken`/`AppIcons` contracts live in `:theme`; consumers override via `AppTheme(icons = …)`.
- **Motion tokens**: included in Phase 1.
