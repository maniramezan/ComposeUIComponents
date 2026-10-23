# Extraction Candidates

Audit scope: patterns observed in consuming Android apps. Do not copy app code into this library. Remove product naming, strings, models, analytics, and business rules, then re-implement reusable behavior with this design system’s tokens, previews, tests, screenshots, and docs.

## Status

| Status | Meaning |
|---|---|
| ✅ Done | Shipping in this library as a generic component. |
| 🟡 Partial | Generic primitive shipped; consumer keeps a thin domain wrapper. |
| 🔄 In review | Implemented in an open pull request. |
| ⏳ Pending | Identified but not yet extracted. |
| ❌ Not a candidate | Reviewed and intentionally left in the app. |

## Candidate Components

| Status | Candidate | Library home | Notes |
|---|---|---|---|
| ✅ Done | Pill chip | `components/PillChip.kt` | Generic selectable capsule; selected/unselected colors are themed tokens with per-call overrides for tinted variants (filter rows). |
| ✅ Done | Overlay card | `components/OverlayCard.kt` | Subtle rounded surface for media overlays. Defaults to `AppTheme.colors.overlaySubtle` + `AppTheme.shapes.large`. |
| ✅ Done | Adaptive content container | `components/AdaptiveContentContainer.kt` | Caps content width on tablets, pass-through on phones. Consumers pick a semantic max width from their own layout-dimension tokens. |
| ✅ Done | Section header | `components/SectionHeader.kt` | Title + optional trailing text action. Reads `AppTheme.typography.titleSmall` and `AppTheme.colors.primary`. |
| ✅ Done | Content row | `components/ContentRow.kt` | Slot-based list row (title, optional secondary text, supporting text, leading + trailing content). Consumers map their own row data at the UI boundary. |
| 🟡 Partial | Level badge | `components/PillChip.kt` (tier overload) + `theme/LevelPalette.kt` | Generic tiered-badge component takes a `LevelTier` from a `LevelPalette`. Apps own the tier→domain mapping (e.g., status value → palette index). |
| ✅ Done | Sized skeleton block | `components/FeedbackComponents.kt::SkeletonBlock`, `components/SkeletonShimmer.kt` | Parameterized height/width/shape variant of `Skeleton`, plus the root-applied `Modifier.skeletonShimmer`. The consumer file is now a thin wrapper over both. |
| ✅ Done | Button styles | `components/PrimaryButton.kt`, `components/ActionComponents.kt::{SecondaryButton, TextButton}` | Primary, secondary, and text actions cover three common emphasis levels without a consumer-specific button type. |
| 🟡 Partial | Search bar | `components/InputComponents.kt::SearchField` | The consumer now wraps `SearchField` with app copy. No second component needed. |
| 🟡 Partial | Empty state | `components/ListComponents.kt::EmptyState` | The consumer now wraps `EmptyState`. No second component needed. |
| 🟡 Partial | Error retry state | `components/ListComponents.kt::ErrorState` | The consumer now wraps `ErrorState`. The media-overlay variant stays app-owned. |
| ✅ Done | Typewriter text reveal | `compose-utils/TypewriterReveal.kt` | `rememberTypewriterReveal` state hook: progressively reveals a growing target string (e.g. token-stream chunks) instead of popping in whole chunks; catches up on extension, restarts on divergence. Rendering-agnostic — returns the revealed prefix as `State<String>`, caller renders it with any text/markdown composable. |
| ✅ Done | Chat bubble | `components/ChatLog.kt` | `ChatLog` owns sender alignment, typing, and error bubbles, and takes a rendering-agnostic `messageContent` slot for Markdown. Apps map their turn models to `ChatMessage` at the UI boundary. |
| ✅ Done | Typing dots | `components/TypingIndicator.kt` | Three-dot wave for a pending reply. Draw-phase only and static under Remove animations. `ChatLog`'s typing bubble uses it. |
| ✅ Done | Blocking loading overlay | `components/LoadingOverlay.kt` | Scrim that swallows touches, swaps content semantics for a caller-supplied live-region announcement, and has an optional spinner card. Consumers choose whether to show the indicator. |
| 🔄 In review | Labeled divider | `components/DividerComponents.kt` ([PR #117](https://github.com/maniramezan/ComposeUIComponents/pull/117)) | Rule + centered caption + rule. A caller-supplied label keeps section and alternative separators generic. |
| ⏳ Pending | Thumbs feedback row | — | State-hoisted positive/negative rating with caller-supplied descriptions and 48dp minimum targets. Submission rules and retry decisions stay with consumers. |
| ❌ Not a candidate | Form error text | — | Covered by `TextField(isError = …, supportingText = …)`. A standalone error line has no reusable behaviour beyond a token-styled `Text`. |
| ❌ Not a candidate | Branded loading indicator | — | Product-branded illustration. Generic loading uses `ProgressIndicator`, `LoadingState`, or `LoadingOverlay`. |

## Cross-Platform Parity

The sibling SwiftUI library (`SwiftUIComponents`) ships the same design language for iOS. Where it has a component this library lacks, the Compose counterpart is a candidate, provided it's still generic.

| Status | SwiftUI component | Compose counterpart | Notes |
|---|---|---|---|
| ✅ Done | `Badge`, `AvatarView` | `Badge`, `CountBadge`, `Avatar` | New **Indicators** category. |
| ✅ Done | `TypingDotsView`, `TypingIndicatorBubbleView` | `TypingIndicator`, `ChatLog` typing bubble | |
| ⏳ Pending | `NoticeCard`, `ErrorBanner`, `AssistantUnavailableBanner` | — | An inline, full-width tonal banner (info / success / warning / error) with an optional icon, action, and dismiss. `AssistantStatusBanner` is assistant-specific; a generic banner would let it delegate. |
| ⏳ Pending | `AsyncContentView` | — | Loading / empty / error / content switcher over the existing `LoadingState`, `EmptyState`, and `ErrorState`, so screens stop hand-writing the same `when`. |
| ⏳ Pending | `MenuPicker` | — | A themed dropdown or exposed-menu single-choice picker. Today callers fall back to raw Material 3 `ExposedDropdownMenuBox`. |
| ⏳ Pending | `SelectableCardSurface` | — | A card with selectable semantics and a selected outline, for plan or option pickers. `Modifier.selectableRole` already provides the semantics. |
| ⏳ Pending | `ProportionalHStackLayout` | — | A row that splits width by fractions rather than weights of measured content. Check whether `Row` + `weight` already covers real usages before adding it. |
| ✅ Done | `CarouselRow`, `CarouselBoard` | `ShowcaseRow`, `ShowcaseFeed` | |
| ✅ Done | `TitledPageView` | `PaginatedContent` | |
| ✅ Done | `SegmentedPicker`, `PillChip`, `ActionPill`, `FlipCard`, `DisclosureCard`, `SelectionListView`, `GhostShimmer`, `ToastView`, `LoadMoreFooter`, `CachedAsyncImage`, `FlowLayout`, `SectionHeader` | Same names (`SegmentedContent` / `SegmentedControl`, `Modifier.skeletonShimmer`, `ToastHost`) | |

## Token Review

- Spacing: reuse the existing scale, adaptive widths, corner aliases, and 48dp interaction helpers. Add new semantic dimensions only when a reusable component requires them.
- Colors: use semantic backgrounds, text colors, interactive colors, status colors, overlays, and tier palettes from `AppTheme`.
- Motion: validate durations and easing against the existing motion tokens before introducing new values.

## Re-Implementation Rules

- Do not import consuming-app package names, strings, model types, analytics, services, or business state.
- Replace raw dimensions/colors with `AppTheme` tokens or component metrics.
- Keep public APIs state-hoisted and slot-based.
- Add light/dark previews, 200% font-scale previews, unit tests, screenshot coverage, and docs for every extracted component.
- Record any non-obvious extraction decision as an ADR before expanding scope.
