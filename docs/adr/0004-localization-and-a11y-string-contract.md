# ADR 0004: Localization and Accessibility String Contract

## Status

Accepted

## Context

Reusable library components must be locale-agnostic and product-agnostic. Any English
string baked into a component default surfaces directly in production UI for every
consumer, regardless of their target locale. Android string resources in libraries also
carry namespace and override risks when merged into a consuming app's resource table.

Separately, accessibility (a11y) labels like `contentDescription` and `stateDescription`
must be localized for the same reason — TalkBack reads them aloud, so an English default
is still a broken experience for non-English users.

## Decision

1. **No Android string resources in `:components`.** The library ships no `strings.xml`
   or other string resource files. There is nothing for consumers to override or collide
   with.

2. **All user-visible and accessibility strings are caller-supplied.** Every component
   parameter that contributes text to the UI or the accessibility tree accepts a `String`
   (or nullable `String?`) from the caller. No parameter defaults to an English word or
   phrase.

3. **Neutral defaults, never silent failures.** When a string parameter is optional:
   - Use `String = ""` when omitting the string leaves the component fully functional
     (e.g., a search field without a placeholder is still usable).
   - Use `String? = null` or a guard on `isNotBlank()` when a blank string would produce
     an empty-but-visible UI node (e.g., a "no results" label that renders nothing when
     the string is blank).

4. **Decorative icons use `contentDescription = null`.** Icons that duplicate information
   already present in adjacent text or in a semantic property (e.g., `selected`,
   `stateDescription`) must be marked decorative so TalkBack skips them and avoids
   double-announcing.

5. **Semantic state, not icon descriptions, carries interactive state.** Use
   `Modifier.selectable(selected = …)` for selection, `semantics { stateDescription = … }`
   for expand/collapse, `semantics { heading() }` for section titles. Callers supply the
   localized `stateDescription` strings; the library wires them into the semantics tree.

6. **KDoc on every string parameter.** Each string parameter that the caller must
   localize carries a KDoc `@param` note stating "supply a localized string" and the
   behaviour when blank or null.

## Consequences

- Callers are responsible for passing localized strings for every user-visible parameter.
  The default empty/null value is a safe fallback, not a finished experience.
- Previews and Showkase entries may use hard-coded English strings because they are
  developer tooling, not shipped UI.
- When adding a new component or parameter, the author must: (a) choose `String = ""`
  or `String? = null` as the default, never an English word; (b) guard rendering on
  `isNotBlank()` or a null check; (c) write a KDoc `@param` note.
- The `docs/accessibility.md` Component Accessibility Contracts section is the living
  record of which parameters each component requires from the caller for full a11y.
