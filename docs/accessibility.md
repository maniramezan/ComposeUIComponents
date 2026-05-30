# Accessibility Checklist

Run this checklist before a release and when adding components.

- Every interactive component has at least a 48dp touch target.
- Icon-only actions have a meaningful non-null `contentDescription`.
- Input screens use `android:windowSoftInputMode="adjustResize"` and IME-safe parent layout.
- Catalog and sample are manually checked with TalkBack.
- UI remains usable at 200% font scale.
- Components use `start` and `end` positioning for RTL compatibility.
- Error, loading, and empty states include readable text, not color-only feedback.

## Phase 3 Audit Notes

- `CatalogActivity` and `SampleActivity` call `enableEdgeToEdge()`.
- Both app manifests use `adjustResize` because the screens contain text fields.
- Both app screens consume `Scaffold` safe drawing insets and apply IME padding before scrolling.

## Manual Audit Matrix

Record results here before cutting a release. Use a physical device when possible because TalkBack focus order, keyboard visibility, and system gesture handling can differ from emulator behavior.

| Area | Status | Notes |
|---|---|---|
| Catalog TalkBack pass | Not run | Verify every component category screen. |
| Sample TalkBack pass | Not run | Verify realistic navigation, forms, dialogs, and error states. |
| Catalog 200% font scale pass | Not run | Check clipping, overlap, scrollability, and minimum touch targets. |
| Sample 200% font scale pass | Not run | Check settings, list/detail, and form flows. |
| RTL layout pass | Not run | Enable a right-to-left locale and verify start/end alignment plus mirrored navigation icons. |
| Edge-to-edge and IME pass | Not run | Verify status/navigation bars, gesture navigation, and keyboard resizing on text-input screens. |

## TalkBack Review Steps

- Swipe through actions and confirm `PrimaryButton`, `SecondaryButton`, `TextButton`, `IconButton`, `FAB`, and `SegmentedControl` announce their label, role, enabled state, and selected state where applicable.
- Swipe through inputs and confirm text fields announce label, value, error state, supporting text, and password visibility controls without exposing sensitive values.
- Swipe through lists and state components and confirm empty, loading, and error states are readable without relying on color or animation.
- Open dialogs, bottom sheets, and snackbar flows and confirm focus moves into transient UI, dismiss actions are discoverable, and focus returns to a sensible origin.
- Review bottom bars, tabs, and navigation rail items and confirm the current destination is announced as selected.
- Increase font scale to 200%, repeat core flows, and confirm all content is reachable by scrolling instead of clipped.
- Enable RTL and confirm horizontal layouts use start/end spacing and mirror-aware icons behave correctly.

## Component Accessibility Contracts

Some components require the caller to supply localized accessibility text, so the
library stays string-free and product-agnostic:

- `Slider` announces only a raw percentage by default. Pass `label` (what it
  controls) and `valueDescription` (a meaningful current value) for context.
- `PaginatedContent` exposes page position only when given `pagePositionDescription`
  (e.g. `{ index, count -> "Page ${index + 1} of $count" }`); the footer is
  decorative and silent otherwise.
- `NavigationItem.badgeContentDescription` gives a badge a meaningful spoken label
  (e.g. `"5 unread"`) instead of the bare badge text.
- `PillChip` and `SegmentedControl` expose selected state to screen readers; the
  active option announces as "selected", not just a tinted button.
- `Toast`, `Snackbar`, `LoadingState`, and `ErrorState` are polite live regions, so
  they are announced when they appear without stealing focus.
- `Skeleton` / `SkeletonBlock` are removed from the accessibility tree (decorative
  loading placeholders carry no information).
- `Section` and `SectionHeader` titles are marked as headings for heading navigation.

## Automated Coverage

- `AccessibilityComponentsTest` verifies representative action and input components keep at least a 48dp touch target, and that `PillChip`, `SegmentedControl`, and `Slider` expose selection state and accessibility labels.
- `:testing` exposes `assertMinimumTouchTarget` and `onNodeWithRequiredContentDescription` helpers for downstream app tests.
- `:compose-utils` exposes `minimumTouchTarget` and `minimumTouchTargetHeight` modifiers for shared component sizing.
- Icon-only actions are covered by semantics assertions for meaningful content descriptions.
- `checkComponentTokenUsage` rejects nullable or null icon descriptions in the public component surface (`*Preview.kt`/`*Showkase.kt` demo files are excluded, since decorative icons there legitimately use `contentDescription = null`).
- Empty and error states are covered by readable text assertions so feedback is not color-only.
- Component preview entry points include `PreviewLightDark` and `PreviewFontScale` coverage for visual review at dark mode and 200% font scale.
