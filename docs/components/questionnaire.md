# Questionnaire

`QuestionnaireView` presents one question at a time with progress, back/next navigation,
required-answer validation, optional skipping, and normalized submission. Answers are
caller-owned and keyed by stable question IDs.

The models and pure validation helpers live in
`io.github.maniramezan.compose.foundation.questionnaire`; the view and navigation labels
live in `io.github.maniramezan.compose.components`. `:components` exposes `:foundation`
transitively. Date models use `java.time.LocalDate` on the JVM and Android API 26+.

## Basic usage

```kotlin
var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }

QuestionnaireView(
    questions = listOf(
        QuestionnaireQuestion(
            id = "choice",
            prompt = choicePrompt,
            isRequired = true,
            kind = QuestionnaireQuestionKind.SingleChoice(
                listOf(
                    QuestionnaireOption("a", firstOptionTitle),
                    QuestionnaireOption("b", secondOptionTitle),
                ),
            ),
        ),
    ),
    answers = answers,
    onAnswersChange = { answers = it },
    labels = QuestionnaireLabels(
        back = backLabel,
        next = nextLabel,
        submit = submitLabel,
        increment = increaseValueLabel,
        decrement = decreaseValueLabel,
        skip = skipLabel,
        optional = optionalLabel,
    ),
    onSubmit = { normalizedAnswers -> saveAnswers(normalizedAnswers) },
    onSkip = { closeQuestionnaire() },
    progressDescription = { page, count -> localizedProgress(page, count) },
    modifier = Modifier.fillMaxSize(),
)
```

Supply localized values for every prompt, option title, detail, placeholder, unit, and
navigation label. No English strings or library string resources are supplied. Blank
optional captions are omitted. Skip appears only when both a nonblank skip label and an
`onSkip` callback are supplied. It invokes the callback without clearing or submitting.

## Question and answer types

| Question kind | Answer | Behavior |
| --- | --- | --- |
| `SingleChoice(options)` | `Choices(ids)` | One selection; tapping it again clears the answer. |
| `MultipleChoice(options)` | `Choices(ids)` | Toggles independently; submission uses option order. |
| `Boolean(trueTitle, falseTitle)` | `Boolean(value)` | Starts unanswered. Both `true` and `false` satisfy required validation. |
| `Number(range, step, unitLabel)` | `Number(value)` | Decimal field and stepper; localized digits and decimal separator. |
| `Date(range)` | `Date(value)` | Calendar day picker, restricted to the inclusive range. |
| `Text(placeholder)` | `Text(value)` | Multiline text; surrounding whitespace is removed when advancing/submitting. |
| `Custom` | Any answer type | Caller supplies the renderer and writes null while incomplete. |

`QuestionnaireChoiceStyle.Automatic` uses bullet rows for single and boolean questions,
and checkmark rows for multiple choices. `Bullets`, `Checkmarks`, and wrapping `Chips`
change presentation independently of selection cardinality.

## Custom content

The trailing renderer receives the question, current answer, and a setter. The library
owns paging and required-answer validation; the caller owns the custom control and its
accessibility. For example, a rating slider can return a numeric answer:

```kotlin
customInput = { question, answer, onAnswerChange ->
    Slider(
        value = (answer as? QuestionnaireAnswer.Number)?.value?.toFloat() ?: 3f,
        onValueChange = { onAnswerChange(QuestionnaireAnswer.Number(it.toDouble())) },
        valueRange = 1f..5f,
        label = question.prompt,
    )
}
```

A displayed custom default does not count as answered until the renderer records an
answer. Return null when the custom input becomes incomplete or invalid. Custom choice
IDs are deduplicated in supplied order, preserving rankings. Custom text is trimmed and
custom numbers must be finite. Questions of kind `Custom` are omitted without a renderer.

## Validation and state

Required questions disable Next/Submit until valid. Optional questions can remain blank.
The final submission rechecks every required question and returns to the first incomplete
page, including when a caller removed an earlier answer while on a later page.

Numbers must be finite. Advancing/submitting clamps them into the range and snaps to the
nearest step measured from its lower bound. Nonpositive or non-finite steps disable
snapping; the stepper uses a delta of one. Its first press on an empty field records the
lower bound. Pasted numeric input retains only decimal digits, one locale decimal
separator, and a leading minus when the range permits negatives.

Dates initially display `today` clamped into the range. That displayed default counts as
answered and is recorded when advancing or submitting. The `today` parameter can be
fixed for previews/tests. Dates represent calendar days without a time-zone conversion
in the public API; the Material picker uses UTC internally.

Unknown option IDs, answers of the wrong type, empty choices, whitespace-only text, and
unknown answer keys are removed on submission. `QuestionnaireFlow` exposes the same
pure rules for consumers needing validation outside composition.

Question IDs must be unique and option IDs unique within their question. Validation
keeps the first occurrence, drops choices with no options and invalid ranges, and never
mutates caller lists. Date ranges must be ordered and fall within years 1–9999. Empty
questionnaires render no navigation or submit action.

Keep answers in observable state. The view preserves its page across recreation; callers
own answer persistence. To restart the page as well as clear answers, replace the view's
Compose `key`. A changed question list clamps the current page into the available range.

## Layout and accessibility

Use a bounded height for a scrollable input with persistent navigation. Inside an
unbounded scrolling parent the view expands naturally. The sample browser bounds its
questionnaire preview and already consumes safe-drawing and IME insets; other hosts must
handle those insets themselves. The date picker starts in localized text-input mode in narrow panes and at 200% font
scale. Its mode toggle also offers a calendar, which scrolls horizontally when needed
to preserve seven complete columns with 48dp day targets.

Prompts expose heading semantics. Choice rows merge their text and selected state into
one target with radio/checkbox roles. Single-choice groups expose group semantics;
indicator controls are decorative. Chips reuse `PillChip` selection semantics. Navigation,
choices, and steppers have at least 48dp touch targets. An optional caller-localized
progress formatter supplies a polite live-region caption in addition to the progress bar.
Navigation buttons stack vertically to accommodate long labels and large text.

The sample includes independently reachable examples for every type, plus a complete
flow, a custom rating control, and live configuration. Light/dark previews and screenshot
coverage include each input, with additional RTL/200% font-scale coverage.
