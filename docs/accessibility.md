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

## Automated Coverage

- `AccessibilityComponentsTest` verifies representative action and input components keep at least a 48dp touch target.
- Icon-only actions are covered by semantics assertions for meaningful content descriptions.
- `checkComponentTokenUsage` rejects nullable or null icon descriptions in component APIs and implementations.
- Empty and error states are covered by readable text assertions so feedback is not color-only.
