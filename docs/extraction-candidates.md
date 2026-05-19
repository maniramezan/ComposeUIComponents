# Extraction Candidates

Audit source: `~/Developer/novalingo/mobile/Android`. Do not copy app code into this library. Use these candidates as product-tested references, strip Novalingo-specific naming, strings, models, analytics, and business rules, then re-implement with this design system's tokens, previews, tests, screenshots, and docs.

## Candidate Components

| Candidate | Source reference | Why it is useful | Extraction notes |
|---|---|---|---|
| Branded button variants | `core-ui/NovalingoButton.kt` | Confirms the common need for primary, secondary, and tertiary actions with shared sizing and typography. | Already mostly covered by `PrimaryButton`, `SecondaryButton`, and `TextButton`; keep future variants token-driven rather than enum-driven. |
| Search bar | `core-ui/SearchBar.kt`, `feature/home/HomeSearchBar.kt` | Shows a compact search input with leading icon, placeholder, IME action, and full-width layout. | Generalize as `SearchField`; decorative search icon can stay hidden from semantics while the field exposes label/placeholder text. |
| Empty state | `core-ui/EmptyStateView.kt`, `feature/home/HomeEmptyErrorStates.kt` | Repeated centered title/body/icon pattern for zero-data screens. | Keep icon optional and decorative by default; require readable title/message text. |
| Error retry state | `core-ui/ErrorRetryView.kt`, `feature/player/PlayerErrorOverlay.kt` | Common recoverable failure pattern with message and retry action. | Preserve slot/action flexibility; avoid product-specific copy and player/network assumptions. |
| Loading skeletons | `core-ui/GhostLoadingBlock.kt`, `feature/home/HomeLoadingSkeletons.kt`, `feature/videodetails/VideoDetailsLoadingSkeleton.kt` | Validates skeleton placeholders for list and detail loading states. | Keep animation and dimensions tokenized; provide reduced-motion-safe behavior through motion tokens. |
| Adaptive content container | `core-ui/AdaptiveContentContainer.kt` | Prevents form/list/player content from stretching on tablets and desktops. | Candidate for `:compose-utils`; expose semantic max widths through tokens instead of Novalingo-specific names. |
| Section header | `core-ui/SectionHeader.kt` | Common list/detail section labeling pattern. | Re-implement as a small slot-based header that works with `Section`. |
| Pill chip | `core-ui/PillChip.kt`, `feature/home/HomeFilters.kt` | Useful for filters, tags, and compact selectable states. | Consider after Phase 1 as a dedicated chip family with selected/disabled states and accessibility labels. |
| Skill/badge pattern | `core-ui/SkillLevelBadge.kt`, `core-design/SkillLevelDesign.kt` | Demonstrates semantic badge colors and text contrast requirements. | Keep as generic `Badge`/status badge tokens; do not import language-learning skill models. |
| Word/card pattern | `core-ui/WordCard.kt`, `feature/savedwords/SavedWordCard.kt`, `feature/home/HomeVideoCard.kt` | Real-world card layouts validate spacing, nested text hierarchy, and trailing actions. | Use as inspiration for richer card/list examples, not a direct component unless repeated outside product domains. |

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
