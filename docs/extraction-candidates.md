# Extraction Candidates

Audit source: `~/Developer/novalingo/mobile/Android`. Do not copy app code into this library. Use these candidates as product-tested references, strip Novalingo-specific naming, strings, models, analytics, and business rules, then re-implement with this design system's tokens, previews, tests, screenshots, and docs.

## Status

| Status | Meaning |
|---|---|
| ✅ Done | Shipping in this library as a generic component. |
| 🟡 Partial | Generic primitive shipped; consumer keeps a thin domain wrapper. |
| 🔄 In review | Implemented in an open pull request. |
| ⏳ Pending | Identified but not yet extracted. |
| ❌ Not a candidate | Reviewed and intentionally left in the app. |

## Candidate Components

| Status | Candidate | Library home | Source reference | Notes |
|---|---|---|---|---|
| ✅ Done | Pill chip | `components/PillChip.kt` | `core-ui/PillChip.kt`, `feature/home/HomeFilters.kt` | Generic selectable capsule; selected/unselected colors are themed tokens with per-call overrides for tinted variants (filter rows). |
| ✅ Done | Overlay card | `components/OverlayCard.kt` | `core-ui/OverlayCard.kt` | Subtle rounded surface for media overlays. Defaults to `AppTheme.colors.overlaySubtle` + `AppTheme.shapes.large`. |
| ✅ Done | Adaptive content container | `components/AdaptiveContentContainer.kt` | `core-ui/AdaptiveContentContainer.kt` | Caps content width on tablets, pass-through on phones. Consumers pick a semantic max width from their own layout-dimension tokens. |
| ✅ Done | Section header | `components/SectionHeader.kt` | `core-ui/SectionHeader.kt` | Title + optional trailing text action. Reads `AppTheme.typography.titleSmall` and `AppTheme.colors.primary`. |
| ✅ Done | Content row | `components/ContentRow.kt` | `core-ui/WordCard.kt`, `feature/savedwords/SavedWordCard.kt` | Slot-based list row (title, optional secondary text, supporting text, leading + trailing content). Word-card-style usage is now a thin consumer wrapper. |
| 🟡 Partial | Level badge | `components/PillChip.kt` (tier overload) + `theme/LevelPalette.kt` | `core-ui/SkillLevelBadge.kt`, `core-design/SkillLevelDesign.kt` | Generic tiered-badge component takes a `LevelTier` from a `LevelPalette`. Apps own the tier→domain mapping (e.g., skill level → palette index). |
| ✅ Done | Sized skeleton block | `components/FeedbackComponents.kt::SkeletonBlock`, `components/SkeletonShimmer.kt` | `core-ui/GhostLoadingBlock.kt` | Parameterized height/width/shape variant of `Skeleton`, plus the root-applied `Modifier.skeletonShimmer`. The consumer file is now a thin wrapper over both. |
| ✅ Done | Branded button variants | `components/PrimaryButton.kt`, `components/ActionComponents.kt::{SecondaryButton, TextButton}` | `core-ui/NovalingoButton.kt` | Three-style enum collapses 1:1 into the library's three button components — no extra wrapper needed in consumers. |
| 🟡 Partial | Search bar | `components/InputComponents.kt::SearchField` | `core-ui/SearchBar.kt`, `feature/home/HomeSearchBar.kt` | The consumer now wraps `SearchField` with app copy. No second component needed. |
| 🟡 Partial | Empty state | `components/ListComponents.kt::EmptyState` | `core-ui/EmptyStateView.kt` | The consumer now wraps `EmptyState`. No second component needed. |
| 🟡 Partial | Error retry state | `components/ListComponents.kt::ErrorState` | `core-ui/ErrorRetryView.kt`, `feature/player/PlayerErrorOverlay.kt` | The consumer now wraps `ErrorState`. The media-overlay variant stays app-owned. |
| ✅ Done | Typewriter text reveal | `compose-utils/TypewriterReveal.kt` | `core-ui/aiassistant/AIAssistantChatLog.kt::AIMarkdownBubble` | `rememberTypewriterReveal` state hook: progressively reveals a growing target string (e.g. token-stream chunks) instead of popping in whole chunks; catches up on extension, restarts on divergence. Rendering-agnostic — returns the revealed prefix as `State<String>`, caller renders it with any text/markdown composable. |
| ✅ Done | Chat bubble | `components/ChatLog.kt` | `core-ui/aiassistant/AIAssistantChatLog.kt::{AIUserBubble, AIAssistantBubble}` | `ChatLog` owns sender alignment, typing, and error bubbles, and takes a rendering-agnostic `messageContent` slot for Markdown. Apps map their turn models to `ChatMessage` at the UI boundary. |
| 🔄 In review | Typing dots | `components/TypingIndicator.kt` | `core-ui/AssistantTypingDots.kt` | Three-dot wave for a pending reply. Draw-phase only and static under Remove animations. `ChatLog`'s typing bubble uses it. |
| 🔄 In review | Blocking loading overlay | `components/LoadingOverlay.kt` | `core-ui/LoadingOverlay.kt`, `core-ui/LoadingOverlayStyle.kt` | Scrim that swallows touches, swaps content semantics for a caller-supplied live-region announcement, and has an optional spinner card. The app's two-style sealed type maps to `showIndicator`. |
| ⏳ Pending | Labeled divider | — | `core-ui/auth/AuthOrDivider.kt` | Rule + centered caption + rule, as in the "or" between sign-in methods. Good fit for `DividerComponents.kt` with a caller-supplied label. |
| ⏳ Pending | Thumbs feedback row | — | `core-ui/FeedbackRow.kt` | One-shot up/down rating with an optional retry-on-down affordance. Needs a caller-owned rating enum or boolean, caller-supplied descriptions, and a 48dp target audit (the app uses 24dp targets). |
| ❌ Not a candidate | Form error text | — | `core-ui/auth/FormErrorText.kt` | Covered by `TextField(isError = …, supportingText = …)`. A standalone error line has no reusable behaviour beyond a token-styled `Text`. |
| ❌ Not a candidate | Branded loading indicator | — | `core-ui/NovalingoLoadingIndicator.kt` | Product-branded illustration. Generic loading uses `ProgressIndicator`, `LoadingState`, or `LoadingOverlay`. |

## Cross-Platform Parity

The sibling SwiftUI library (`SwiftUIComponents`) ships the same design language for iOS. Where it has a component this library lacks, the Compose counterpart is a candidate, provided it's still generic.

| Status | SwiftUI component | Compose counterpart | Notes |
|---|---|---|---|
| 🔄 In review | `Badge`, `AvatarView` | `Badge`, `CountBadge`, `Avatar` | New **Indicators** category. |
| 🔄 In review | `TypingDotsView`, `TypingIndicatorBubbleView` | `TypingIndicator`, `ChatLog` typing bubble | |
| ⏳ Pending | `NoticeCard`, `ErrorBanner`, `AssistantUnavailableBanner` | — | An inline, full-width tonal banner (info / success / warning / error) with an optional icon, action, and dismiss. `AssistantStatusBanner` is assistant-specific; a generic banner would let it delegate. |
| ⏳ Pending | `AsyncContentView` | — | Loading / empty / error / content switcher over the existing `LoadingState`, `EmptyState`, and `ErrorState`, so screens stop hand-writing the same `when`. |
| ⏳ Pending | `MenuPicker` | — | A themed dropdown or exposed-menu single-choice picker. Today callers fall back to raw Material 3 `ExposedDropdownMenuBox`. |
| ⏳ Pending | `SelectableCardSurface` | — | A card with selectable semantics and a selected outline, for plan or option pickers. `Modifier.selectableRole` already provides the semantics. |
| ⏳ Pending | `ProportionalHStackLayout` | — | A row that splits width by fractions rather than weights of measured content. Check whether `Row` + `weight` already covers real usages before adding it. |
| ✅ Done | `CarouselRow`, `CarouselBoard` | `ShowcaseRow`, `ShowcaseFeed` | |
| ✅ Done | `TitledPageView` | `PaginatedContent` | |
| ✅ Done | `SegmentedPicker`, `PillChip`, `ActionPill`, `FlipCard`, `DisclosureCard`, `SelectionListView`, `GhostShimmer`, `ToastView`, `LoadMoreFooter`, `CachedAsyncImage`, `FlowLayout`, `SectionHeader` | Same names (`SegmentedContent` / `SegmentedControl`, `Modifier.skeletonShimmer`, `ToastHost`) | |

## Token References

- `core-design/Spacing.kt`: 8-point spacing scale, adaptive max widths, corner aliases, and tap target constants. This repo already uses semantic spacing tokens and 48dp minimum targets; future adaptive max-width tokens should be added deliberately.
- `core-design/AppColors.kt`: semantic backgrounds, text colors, interactive colors, status colors, overlays, and badge colors. This repo already maps semantic colors through `AppTheme`; extra overlay/status/badge tokens should be added only when a component requires them.
- `core-design/Animations.kt`: motion constants are a useful reference for validating naming and duration coverage against this repo's motion tokens.

## Re-Implementation Rules

- Do not import Novalingo package names, strings, model types, analytics, services, or business state.
- Replace raw dimensions/colors with `AppTheme` tokens or component metrics.
- Keep public APIs state-hoisted and slot-based.
- Add light/dark previews, 200% font-scale previews, unit tests, screenshot coverage, and docs for every extracted component.
- Record any non-obvious extraction decision as an ADR before expanding scope.
