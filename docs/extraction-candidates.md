# Extraction Candidates

Audit source: `~/Developer/novalingo/mobile/Android`. Do not copy app code into this library. Use these candidates as product-tested references, strip Novalingo-specific naming, strings, models, analytics, and business rules, then re-implement with this design system's tokens, previews, tests, screenshots, and docs.

## Status

| Status | Meaning |
|---|---|
| ✅ Done | Shipping in this library as a generic component. |
| 🟡 Partial | Generic primitive shipped; consumer keeps a thin domain wrapper. |
| ⏳ Pending | Identified but not yet extracted. |

## Candidate Components

| Status | Candidate | Library home | Source reference | Notes |
|---|---|---|---|---|
| ✅ Done | Pill chip | `components/PillChip.kt` | `core-ui/PillChip.kt`, `feature/home/HomeFilters.kt` | Generic selectable capsule; selected/unselected colors are themed tokens with per-call overrides for tinted variants (filter rows). |
| ✅ Done | Overlay card | `components/OverlayCard.kt` | `core-ui/OverlayCard.kt` | Subtle rounded surface for media overlays. Defaults to `AppTheme.colors.overlaySubtle` + `AppTheme.shapes.large`. |
| ✅ Done | Adaptive content container | `components/AdaptiveContentContainer.kt` | `core-ui/AdaptiveContentContainer.kt` | Caps content width on tablets, pass-through on phones. Consumers pick a semantic max width from their own layout-dimension tokens. |
| ✅ Done | Section header | `components/SectionHeader.kt` | `core-ui/SectionHeader.kt` | Title + optional trailing text action. Reads `AppTheme.typography.titleSmall` and `AppTheme.colors.primary`. |
| ✅ Done | Content row | `components/ContentRow.kt` | `core-ui/WordCard.kt`, `feature/savedwords/SavedWordCard.kt` | Slot-based list row (title, optional secondary text, supporting text, leading + trailing content). Word-card-style usage is now a thin consumer wrapper. |
| 🟡 Partial | Level badge | `components/PillChip.kt` (tier overload) + `theme/LevelPalette.kt` | `core-ui/SkillLevelBadge.kt`, `core-design/SkillLevelDesign.kt` | Generic tiered-badge component takes a `LevelTier` from a `LevelPalette`. Apps own the tier→domain mapping (e.g., skill level → palette index). |
| 🟡 Partial | Sized skeleton block | `components/FeedbackComponents.kt::SkeletonBlock` | `core-ui/GhostLoadingBlock.kt` | Parameterized height/width/shape variant of `Skeleton`. Shimmer animation is left to the consumer to keep `core` dependency-free. |
| ✅ Done | Branded button variants | `components/PrimaryButton.kt`, `components/ActionComponents.kt::{SecondaryButton, TextButton}` | `core-ui/NovalingoButton.kt` | Three-style enum collapses 1:1 into the library's three button components — no extra wrapper needed in consumers. |
| ⏳ Pending | Search bar | — | `core-ui/SearchBar.kt`, `feature/home/HomeSearchBar.kt` | Library already ships `SearchField`. Audit Novalingo's variant against it before adding a second component. |
| ⏳ Pending | Empty state | — | `core-ui/EmptyStateView.kt` | Library already ships `EmptyState`. Audit for icon/action parity before adding a second component. |
| ⏳ Pending | Error retry state | — | `core-ui/ErrorRetryView.kt`, `feature/player/PlayerErrorOverlay.kt` | Library already ships `ErrorState`. Audit for icon/action-button styling parity. |
| ✅ Done | Typewriter text reveal | `compose-utils/TypewriterReveal.kt` | `core-ui/aiassistant/AIAssistantChatLog.kt::AIMarkdownBubble` | `rememberTypewriterReveal` state hook: progressively reveals a growing target string (e.g. token-stream chunks) instead of popping in whole chunks; catches up on extension, restarts on divergence. Rendering-agnostic — returns the revealed prefix as `State<String>`, caller renders it with any text/markdown composable. |
| ⏳ Pending | Chat bubble | — | `core-ui/aiassistant/AIAssistantChatLog.kt::{AIUserBubble, AIAssistantBubble}` | Novalingo shares left/right-aligned message bubbles (fraction-width `Row` + `Card`, generic over a chat-turn interface) between its word and grammar AI assistants. Audit before extracting: needs a rendering-agnostic content slot (the app mixes plain `Text` and third-party Markdown rendering per state) and a turn-type contract this library shouldn't own. |

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
